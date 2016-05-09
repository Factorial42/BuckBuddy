package com.buckbuddy.api.donation;

import static spark.Spark.get;
import static spark.Spark.post;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

import com.buckbuddy.api.donation.data.DonationDataException;
import com.buckbuddy.api.donation.data.DonationModel;
import com.buckbuddy.api.donation.data.DonationModelImpl;
import com.buckbuddy.api.donation.data.model.Donation;
import com.buckbuddy.core.BuckBuddyResponse;
import com.buckbuddy.core.exceptions.BuckBuddyException;
import com.buckbuddy.core.payment.StripeUtil;
import com.buckbuddy.core.utils.AWSSESUtil;
import com.buckbuddy.core.utils.RESTClientUtil;
import com.buckbuddy.core.utils.TemplateUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DonationRouter {

	private static final Logger LOG = LoggerFactory
			.getLogger(DonationRouter.class);
	private static final String USER_SERVICE_BASE_GET_USER = "http://localhost:4567/users/bySlug/:userSlug";
	private static final String CAMPAIGN_SERVICE_BASE_GET_CAMPAIGN = "http://localhost:4568/campaigns/bySlug/:campaignSlug/minified";
	private static final String CAMPAIGN_SERVICE_BASE_UPDATE_CONTRIBUTIONS = "http://localhost:4568/campaigns/bySlug/:campaignSlug/updateContributions?donationAmount=";

	/** The Constant mapper. */
	private static final ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());
	private static final String EMAIL_THANK_YOU_TEMPLATE = "emails/thankyou.mustache";
	private static final String FROM = "hi@buckbuddy.com";
	private static final String SUBJECT = "Thank you for Bucking!";

	/** The donation model impl. */
	private DonationModel donationModelImpl;

	/**
	 * Instantiates a new donation router.
	 */
	public DonationRouter() {
		donationModelImpl = new DonationModelImpl();
	}

	public void initializeCRUDRoutes() {

		post("/donations", (req, res) -> {
			BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
			try {
				Donation donation = null;

				try {
					donation = mapper.readValue(req.body(), Donation.class);
				} catch (JsonMappingException | JsonParseException e) {
					res.status(400);
					buckbuddyResponse.setError(mapper.createObjectNode().put(
							"message", "Bad input?"));
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				}
				// check for user slug and campaign slug - mandatory
				// fields
				if (donation.getUserSlug() == null
						|| donation.getUserSlug().isEmpty()
						|| donation.getCampaignSlug() == null
						|| donation.getCampaignSlug().isEmpty()) {
					res.status(400);
					buckbuddyResponse
							.setError(mapper.createObjectNode().put("message",
									"userSlug & campaignSlug are mandatory"));
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				}
				if (donation.getPaymentToken() == null
						|| donation.getPaymentToken().isEmpty()) {
					res.status(400);
					buckbuddyResponse.setError(mapper.createObjectNode().put(
							"message", "paymentToken is mandatory"));
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				}
				if (donation.getCurrencyString() == null
						|| donation.getCurrencyString().isEmpty()) {
					res.status(400);
					buckbuddyResponse.setError(mapper.createObjectNode().put(
							"message", "currencyString  is mandatory"));
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				}

				if (donation.getAmountInCents() == null
						|| donation.getAmountInCents().compareTo(
								BigDecimal.ZERO) <= 0) {
					res.status(400);
					buckbuddyResponse
							.setError(mapper
									.createObjectNode()
									.put("message",
											"Donation amount should be greater than absolute 0"));
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				}

				// Verify user exists.
				JsonNode userJson = RESTClientUtil.sendGET(
						USER_SERVICE_BASE_GET_USER.replace(":userSlug",
								donation.getUserSlug()), null);
				if (userJson != null) {
					LOG.debug(
							"Found user with Slug:{}",
							userJson.get("userSlug") != null ? userJson.get(
									"userSlug").textValue() : "");
				} else {
					res.status(401);
					buckbuddyResponse
							.setError(mapper
									.createObjectNode()
									.put("message",
											"Could not find User. Either user service is down or user does not exist."));
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				}

				// verify campaign exists
				JsonNode campaignJson = RESTClientUtil.sendGET(
						CAMPAIGN_SERVICE_BASE_GET_CAMPAIGN.replace(
								":campaignSlug", donation.getCampaignSlug()),
						null);
				if (campaignJson != null) {
					LOG.debug(
							"Found campaign with slug:{}",
							campaignJson.get("campaignSlug") != null ? campaignJson
									.get("campaignSlug").textValue() : "");
				} else {
					res.status(401);
					buckbuddyResponse
							.setError(mapper
									.createObjectNode()
									.put("message",
											"Could not find Campaign. Either campaign service is down or user does not exist."));
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				}

				// call stripe charge api
				if (!userJson.findPath("paymentProfiles").isMissingNode()
						&& !userJson.findPath("paymentProfiles")
								.findPath("stripe").isMissingNode()
						&& !userJson.findPath("paymentProfiles")
								.findPath("stripe").findPath("accountId")
								.isMissingNode()
						&& !userJson.findPath("paymentProfiles")
								.findPath("stripe").findPath("accountId")
								.asText().isEmpty()) {

					String connectedAccountId = userJson
							.findPath("paymentProfiles").findPath("stripe")
							.findPath("accountId").asText();
					JsonNode chargeUserResponse = null;
					BigDecimal applicationFeeCollected = null;

					try {
						// charge user
						chargeUserResponse = StripeUtil.chargeUser(
								donation.getPaymentToken(),
								donation.getAmountInCents(),
								donation.getCurrencyString(),
								donation.getDescription(), connectedAccountId);
						applicationFeeCollected = StripeUtil
								.computeApplicationFeeInCents(donation
										.getAmountInCents());
						// if charge failed respond with exception
					} catch (BuckBuddyException e) {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse.setError(mapper.createObjectNode()
								.put("message",
										"Stripe call failed with "
												+ e.getMessage()));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
					LOG.info("Donation Response Log:",
							chargeUserResponse.toString());
					donation.setChargeUserResponse(chargeUserResponse);
					if (applicationFeeCollected != null) {
						donation.setApplicationFeeCollected(applicationFeeCollected);
					}
					// save donation details
					Map<String, Object> response = donationModelImpl
							.create(mapper.convertValue(donation, Map.class));

					if (response != null
							&& response.get("inserted") instanceof Long
							&& ((Long) response.get("inserted")) > 0) {
						res.status(204);
						res.type("application/json");

						// update campaign with donated amount added
						// to
						// campaign
						// total
						JsonNode campaignJUpdateResponse = RESTClientUtil.sendPOST(
								CAMPAIGN_SERVICE_BASE_UPDATE_CONTRIBUTIONS
										.replace(":campaignSlug",
												donation.getCampaignSlug())
										+ donation.getAmountInCents(), null);
						res.status(200);
						res.type("application/json");
						return mapper.writeValueAsString(buckbuddyResponse);
					} else if (response != null
							&& response.get("errors") instanceof Long
							&& ((Long) response.get("errors")) > 0) {
						res.status(200);
						res.type("application/json");
						buckbuddyResponse
								.setError(mapper
										.createObjectNode()
										.put("message",
												"Charded user but could not create donation entry in db for the charges"));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
					res.status(200);
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				} else {
					res.status(400);
					res.type("application/json");
					buckbuddyResponse.setError(mapper.createObjectNode().put(
							"message",
							"No stripe account found for Campaign Owner with user slug:"
									+ donation.getUserSlug()));
					return mapper.writeValueAsString(buckbuddyResponse);
				}
			} catch (DonationDataException ude) {
				res.status(500);
				res.type("application/json");
				buckbuddyResponse.setError(mapper.createObjectNode().put(
						"message", BuckBuddyException.UNKNOWN));
				return mapper.writeValueAsString(buckbuddyResponse);
			}
		});

		get("/donations/byCampaignSlug/:campaignSlug",
				(req, res) -> {
					BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
					try {
						Integer pageNumber = 1, pageSize = 5;

						if (req.params(":campaignSlug") == null
								|| req.params(":campaignSlug").isEmpty()) {
							res.status(400);
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"campaignSlug in path param is mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						String campaignSlug = req.params(":campaignSlug");
						if (req.queryParams("pageNumber") != null
								&& !req.queryParams("pageNumber").isEmpty()) {
							try {
								pageNumber = Integer.parseInt(req
										.queryParams("pageNumber"));
							} catch (NumberFormatException e) {
								res.status(400);
								buckbuddyResponse
										.setError(mapper
												.createObjectNode()
												.put("message",
														"If provided pageNumber should be positive integers. Default 1"));
								res.type("application/json");
								return mapper
										.writeValueAsString(buckbuddyResponse);
							}
						}
						if (req.queryParams("pageSize") != null
								&& !req.queryParams("pageSize").isEmpty()) {
							try {
								pageSize = Integer.parseInt(req
										.queryParams("pageSize"));
							} catch (NumberFormatException e) {
								res.status(400);
								buckbuddyResponse
										.setError(mapper
												.createObjectNode()
												.put("message",
														"If provided pageSize should be positive integers. Default 5"));
								res.type("application/json");
								return mapper
										.writeValueAsString(buckbuddyResponse);
							}
						}
						Long count = donationModelImpl
								.countByCampaignSlug(campaignSlug);
						ObjectNode responseNode = mapper.createObjectNode();
						List<Donation> donations = donationModelImpl
								.getByCampaignSlugOrderByCreatedDatePaginated(
										campaignSlug, pageNumber, pageSize,
										Boolean.TRUE);
						ArrayNode donationNodeArray = mapper.convertValue(
								donations, ArrayNode.class);
						res.status(200);
						res.type("application/json");
						responseNode.put("donations", donationNodeArray);
						responseNode.put("count", count);
						return responseNode;
					} catch (DonationDataException ude) {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse.setError(mapper.createObjectNode()
								.put("message", BuckBuddyException.UNKNOWN));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
				});
		post("/donations/:donationId/thankYou",
				(req, res) -> {
					BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
					try {

						if (req.params(":donationId") == null
								|| req.params(":donationId").isEmpty()) {
							res.status(400);
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"donationId in path param is mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						String donationId = req.params(":donationId");
						// get donation record
						Donation donation = donationModelImpl
								.getById(donationId);
						if (donation == null) {
							res.status(404);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"donationId not found"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						// send email registration to activate
						Map<String, String> model = new HashMap();
						model.put(
								"username",
								donation.getDonorName() != null
										&& !donation.getDonorName().isEmpty() ? donation
										.getDonorName() : "Donor");
						model.put(
								"amount",
								donation.getAmountInCents()
										.divide(new BigDecimal(100))
										.toPlainString());
						String renderedTemplate = TemplateUtil.render(model,
								EMAIL_THANK_YOU_TEMPLATE);
						if (donation.getEmail() == null
								|| donation.getEmail().isEmpty()) {
							res.status(400);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put(
											"message",
											"No Email found for doantionId:"
													+ donationId));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						boolean emailSent = AWSSESUtil.sendMail(FROM,
								donation.getEmail(), SUBJECT, renderedTemplate);
						if (!emailSent) {
							LOG.error("Could not send Thank You email to donor");
							res.status(500);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Could not send Thank You email to donor"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						res.status(204);
						res.type("application/json");
						return mapper.writeValueAsString(buckbuddyResponse);
					} catch (DonationDataException ude) {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse.setError(mapper.createObjectNode()
								.put("message", BuckBuddyException.UNKNOWN));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
				});
	}

	public static void main(String[] args) {
		Spark.port(4569);
		DonationRouter donationRouter = new DonationRouter();
		donationRouter.initializeCRUDRoutes();
	}

}