package com.buckbuddy.api.user;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.put;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;

import com.buckbuddy.api.user.data.UserDataException;
import com.buckbuddy.api.user.data.UserModel;
import com.buckbuddy.api.user.data.UserModelImpl;
import com.buckbuddy.api.user.data.model.User;
import com.buckbuddy.core.BuckBuddyResponse;
import com.buckbuddy.core.exceptions.BuckBuddyException;
import com.buckbuddy.core.payment.StripeUtil;
import com.buckbuddy.core.security.JJWTUtil;
import com.buckbuddy.core.security.SecurityUtil;
import com.buckbuddy.core.social.FBUtil;
import com.buckbuddy.core.utils.AWSS3Util;
import com.buckbuddy.core.utils.AWSSESUtil;
import com.buckbuddy.core.utils.RESTClientUtil;
import com.buckbuddy.core.utils.TemplateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// TODO: Auto-generated Javadoc
/**
 * The Class UserRouter.
 */
public class UserRouter {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(UserRouter.class);

	private static final String STRIPE_BUSINESS_URL = "http://dev.buckbuddy.com";
	private static final String S3_BUCKET = "user.assets.dev.buckbuddy.com";
	private static final String CAMPAIGN_SERVICE_BASE_ACTIVATE_BY_USER_ID = "http://localhost:4568/campaigns/byUserId/:userId/activate";

	private static final String ASSETS_URL = "http://user.assets.dev.buckbuddy.com/";
	private static final String USER_PROFILE_PIC_PREFIX = "profile.pic";
	private static final String EMAIL_REGISTRATION_TEMPLATE = "emails/registration.mustache";
	private static final String FROM = "hi@buckbuddy.com";
	private static final String SUBJECT = "Welcome to Bucking!";
	private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For",
			"Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	/** The Constant mapper. */
	private static final ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());

	/** The user model impl. */
	private UserModel userModelImpl;

	/**
	 * Instantiates a new user router.
	 */
	public UserRouter() {
		userModelImpl = new UserModelImpl();
	}

	/**
	 * Initialize crud routes.
	 */
	public void initializeCRUDRoutes() {
		get("/users/byToken/:token",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token=req.params(":token");
						String userId=JJWTUtil.getSubject(token);
						User user = userModelImpl.getById(userId);
						if (user != null) {
							res.status(200);
							res.type("application/json");
							buckbuddyResponse.setData(mapper.convertValue(user,
									ObjectNode.class));
							return mapper.writeValueAsString(buckbuddyResponse);
						} else {
							res.status(404);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		get("/users/bySlug/:userSlug",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String userSlug = req.params(":userSlug");

						User user = userModelImpl.getByUserSlug(userSlug, Boolean.FALSE, Boolean.FALSE);
						if (user != null) {
							res.status(200);
							res.type("application/json");
							return mapper.writeValueAsString(user);
						} else {
							res.status(404);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		get("/users/bySlug/:userSlug/minified",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String userSlug = req.params(":userSlug");

						User user = userModelImpl.getByUserSlug(userSlug, Boolean.TRUE, Boolean.TRUE);
						if (user != null) {
							res.status(200);
							res.type("application/json");
							return mapper.writeValueAsString(user);
						} else {
							res.status(404);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		get("/users/:userId",
				(req, res) -> {
					try {
						User user = userModelImpl.getById(req.params(":userId"));
						if (user != null) {
							res.status(200);
							res.type("application/json");
						} else {
							res.status(404);
							res.type("application/json");
						}
						return mapper.writeValueAsString(user);
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		get("/users/:userId/minified",
				(req, res) -> {
					try {
						User user = userModelImpl.getById(req.params(":userId"), Boolean.TRUE, Boolean.TRUE);
						if (user != null) {
							res.status(200);
							res.type("application/json");
						} else {
							res.status(404);
							res.type("application/json");
						}
						return mapper.writeValueAsString(user);
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		get("/users/email/:email",
				(req, res) -> {
					try {
						User user = userModelImpl.getByEmail(req
								.params(":email"));
						if (user != null) {
							res.status(200);
							res.type("application/json");
						} else {
							res.status(404);
							res.type("application/json");
						}
						return mapper.writeValueAsString(user);
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		put("/users/:userId",
				(req, res) -> {
					try {
						User user = mapper.readValue(req.body(), User.class);
						if (user.getUserId() != null) {
							res.status(403);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"User ID is required.");
						}
						Map<String, Object> response = userModelImpl
								.update(user);
						if (response != null
								&& response.get("replaced") instanceof Long
								&& ((Long) response.get("replaced")) > 0) {
							res.status(204);
							res.type("application/json");
						} else if (response != null
								&& response.get("errors") instanceof Long
								&& ((Long) response.get("errors")) > 0) {
							res.status(500);
							res.type("application/json");
						} else {
							res.status(204);
							res.type("application/json");
						}
						return mapper.writeValueAsString(response);
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		patch("/users/:userId",
				(req, res) -> {
					try {

						Map<String, Object> userMap = mapper.readValue(
								req.body(), Map.class);
						if (userMap.get("userId") != null) {
							res.status(403);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"User ID is required.");
						}

						Map<String, Object> response = userModelImpl
								.updatePartial(userMap);
						if (response != null
								&& response.get("replaced") instanceof Long
								&& ((Long) response.get("replaced")) > 0) {
							res.status(204);
							res.type("application/json");
						} else if (response != null
								&& response.get("errors") instanceof Long
								&& ((Long) response.get("errors")) > 0) {
							res.status(500);
							res.type("application/json");
						} else {
							res.status(204);
							res.type("application/json");
						}
						return mapper.writeValueAsString(response);
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		delete("/users/:userId",
				(req, res) -> {
					try {
						String userId = req.params(":userId");
						Map<String, Object> response = userModelImpl
								.deleteById(userId);

						if (response != null
								&& response.get("deleted") instanceof Long
								&& ((Long) response.get("deleted")) > 0) {
							res.status(204);
							res.type("application/json");
						} else if (response != null
								&& response.get("errors") instanceof Long
								&& ((Long) response.get("errors")) > 0) {
							res.status(500);
							res.type("application/json");
						} else {
							res.status(204);
							res.type("application/json");
						}
						return mapper.writeValueAsString(response);
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});

	}

	private void initializeLoginRoutes() {
		post("/users/signup", (req, res) -> {
			BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
			try {
				String token = null;
				User user = mapper.readValue(req.body(), User.class);

				// check for email and password - mandatory fields
				if (user.getEmail() == null || user.getEmail().isEmpty()
						|| user.getPassword() == null
						|| user.getPassword().isEmpty()) {
					res.status(400);
					buckbuddyResponse.setError(mapper.createObjectNode().put(
							"message", "Email & Password are mandatory"));
					res.type("application/json");
					return mapper.writeValueAsString(buckbuddyResponse);
				}
				Map<String, Object> response = userModelImpl.create(user);
				if (response != null
						&& response.get("inserted") instanceof Long
						&& ((Long) response.get("inserted")) > 0) {

					// success flow
				res.status(201);
				// generate token
				User userFromDB = userModelImpl.getByEmail(user.getEmail());
				token = JJWTUtil.issueToken(userFromDB.getUserId());
				userFromDB.setToken(token);
				buckbuddyResponse.setData(mapper.convertValue(userFromDB,
						ObjectNode.class));

				// send email registration to activate
				Map model = new HashMap();
				model.put("username", user.getName());
				model.put("activationlink", "http://dev.buckbuddy.com/u/"+token+"/activate");
				String renderedTemplate = TemplateUtil.render(
						model, EMAIL_REGISTRATION_TEMPLATE);
				boolean emailSent = AWSSESUtil.sendMail(FROM,
						user.getEmail(), SUBJECT,
						renderedTemplate);
				if (!emailSent) {
					LOG.info("Could not send email registration to user");
				}
				
				res.type("application/json");
				return mapper.writeValueAsString(buckbuddyResponse);
			} else if (response != null
					&& response.get("errors") instanceof Long
					&& ((Long) response.get("errors")) > 0) {

				// failure flow
				res.status(401);
				buckbuddyResponse.setError(mapper.createObjectNode().put(
						"message", (String) response.get("first_error")));
				res.type("application/json");
				return mapper.writeValueAsString(buckbuddyResponse);
			} else {
				// failure flow
				res.status(500);
				buckbuddyResponse.setError(mapper.createObjectNode().put(
						"message", BuckBuddyException.UNKNOWN));
				res.type("application/json");
				return mapper.writeValueAsString(buckbuddyResponse);
			}
		} catch (UserDataException ude) {
			res.status(500);
			res.type("application/json");
			buckbuddyResponse.setError(mapper.createObjectNode().put("message",
					BuckBuddyException.UNKNOWN));
			return mapper.writeValueAsString(buckbuddyResponse);
		}
	}	);

		// regular user login
		post("/users/login",
				(req, res) -> {
					BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
					User user = mapper.readValue(req.body(), User.class);

					try {
						// get user
						User userFromDB = userModelImpl.getByEmail(
								user.getEmail(), Boolean.FALSE);

						// check if there is user record
						if (userFromDB == null
								|| userFromDB.getUserId() == null) {
							// log the issue and fail
							LOG.debug("User not found");
							res.status(404);
							res.type("application/json");
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"User not found"));
							return mapper.writeValueAsString(buckbuddyResponse);
						} else if (userFromDB.getPassword() != null
								&& !userFromDB.getPassword().equals(
										SecurityUtil.encrypt(
												user.getPassword(),
												SecurityUtil.SHA_256))) {
							// if passwords do not match, log the issue and fail
							LOG.debug("Passwords do not match");
							res.status(401);
							res.type("application/json");
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"Passwords do not match"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}

						// else generate token
						String token = JJWTUtil.issueToken(userFromDB
								.getUserId());
						if (token != null && !token.isEmpty()) {
							userFromDB.setToken(token);
							res.status(200);
							res.type("application/json");

							buckbuddyResponse.setData(mapper.convertValue(
									User.obfuscate(userFromDB),
									ObjectNode.class));
							return mapper.writeValueAsString(buckbuddyResponse);
						} else {
							// log the issue and fail
							LOG.debug("JJWTUtil.issueToken did not provide valid token:"
									+ token);
							res.status(500);
							res.type("application/json");
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											BuckBuddyException.UNKNOWN));
							return mapper.writeValueAsString(buckbuddyResponse);
						}

					} catch (BuckBuddyException e) {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse.setError(mapper.createObjectNode()
								.put("message", BuckBuddyException.UNKNOWN));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
				});

		// user authenticate
		post("/users/authenticate", (req, res) -> {
			return null;
		});

		// reset password
		post("/users/resetPassword", (req, res) -> {
			return null;
		});

		// get fb user profile
		get("/users/fb/profile", (req, res) -> {
			BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
			String fbToken = req.queryParams("fbToken");
			com.restfb.types.User fbUser = FBUtil.getProfile(fbToken);
			res.type("application/json");
			User user = userModelImpl.createUserFromFBProfile(fbUser, fbToken);
			if (user != null
					&& user.getSocialProfiles() != null
					&& user.getSocialProfiles().getFacebookProfile() != null
					&& user.getSocialProfiles().getFacebookProfile()
							.getFacebookID() != null) {
				res.status(200);
				res.type("application/json");
				buckbuddyResponse.setData(mapper.convertValue(user,
						ObjectNode.class));
				return mapper.writeValueAsString(buckbuddyResponse);
			} else {
				res.status(500);
				res.type("application/json");
				buckbuddyResponse.setError(mapper.createObjectNode().put(
						"message", BuckBuddyException.UNKNOWN));
				return mapper.writeValueAsString(buckbuddyResponse);
			}
		});

		post("/users/fb/signup",
				(req, res) -> {

					BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
					try {
						Map<String, Object> usermap = mapper.readValue(
								req.body(), Map.class);

						// check for email and token - mandatory fields
						if (usermap.get("name") == null
								|| ((String) usermap.get("name")).isEmpty()
								|| usermap.get("email") == null
								|| ((String) usermap.get("email")).isEmpty()
								|| usermap.get("fbToken") == null
								|| ((String) usermap.get("fbToken")).isEmpty()) {
							res.status(400);
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Name, Email & Token are mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}

						com.restfb.types.User fbUser = FBUtil
								.getProfile(((String) usermap.get("fbToken")));
						res.type("application/json");
						User user = userModelImpl.createUserFromFBProfile(
								fbUser, ((String) usermap.get("fbToken")));
						user.setName(((String) usermap.get("name")));
						user.setEmail(((String) usermap.get("email")));

						Map<String, Object> response = userModelImpl.create(
								user, Boolean.FALSE);
						if (response != null
								&& response.get("inserted") instanceof Long
								&& ((Long) response.get("inserted")) > 0) {
							// success flow
							res.status(201);
							// generate token
							User userFromDB = userModelImpl.getByEmail(user
									.getEmail());
							String token = JJWTUtil.issueToken(userFromDB
									.getUserId());
							userFromDB.setToken(token);

							// upload pic
							if (userFromDB.getProfilePic() != null) {
								Path filenamePath = Paths.get(
										userFromDB.getProfilePic().toString())
										.getFileName();
								String fileName = filenamePath.toString()
										.contains("?") ? filenamePath
										.toString().replaceAll("\\?.*", "")
										: filenamePath.toString();
								fileName = fileName.substring(fileName
										.lastIndexOf("/") + 1);
								String extension = fileName.substring(fileName
										.lastIndexOf(".") + 1);
								String profilePicS3Path = userFromDB
										.getUserId()
										+ "/"
										+ USER_PROFILE_PIC_PREFIX
										+ "/"
										+ fileName;

								boolean success = false;
								try {
									String metadataString = "image/"
											+ extension;

									success = AWSS3Util.upload(S3_BUCKET,
											profilePicS3Path, new URL(
													userFromDB.getProfilePic())
													.openStream(),
											metadataString);
								} catch (MalformedURLException e) {
									LOG.error("malformed Profile pic url {}",
											userFromDB.getProfilePic());
								}
								if (!success) {
									LOG.error("Could not upload profile pic"
											+ userFromDB.getProfilePic()
													.toString() + " for:"
											+ userFromDB.getUserId());
								} else {
									userFromDB.setProfilePic(ASSETS_URL
											+ profilePicS3Path);
									Map<String, Object> userMap = new HashMap<>();
									userMap.put("userId",
											userFromDB.getUserId());
									userMap.put("profilePic",
											userFromDB.getProfilePic());
									Map<String, Object> updateResponse = userModelImpl
											.updatePartial(userMap);
									if (updateResponse != null
											&& updateResponse.get("replaced") instanceof Long
											&& ((Long) updateResponse
													.get("replaced")) > 0) {
										LOG.info(
												"Updated user {} with s3 profile pic url {}",
												userFromDB.getUserId(),
												userFromDB.getProfilePic());
									}
								}
							}
							buckbuddyResponse.setData(mapper.convertValue(
									userFromDB, ObjectNode.class));

							// send email registration to activate
							Map model = new HashMap();
							model.put("username", user.getName());
							model.put("activationlink", "http://dev.buckbuddy.com/u/"+token+"/activate");
							String renderedTemplate = TemplateUtil.render(
									model, EMAIL_REGISTRATION_TEMPLATE);
							boolean emailSent = AWSSESUtil.sendMail(FROM,
									user.getEmail(), SUBJECT,
									renderedTemplate);
							if (!emailSent) {
								LOG.info("Could not send email registration to user");
							}
							
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						} else if (response != null
								&& response.get("errors") instanceof Long
								&& ((Long) response.get("errors")) > 0) {

							// failure flow
							res.status(401);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put(
											"message",
											(String) response
													.get("first_error")));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						} else {
							// failure flow
							res.status(500);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											BuckBuddyException.UNKNOWN));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}

					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse.setError(mapper.createObjectNode()
								.put("message", BuckBuddyException.UNKNOWN));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
				});
		// fb user login
		post("/users/fb/login",
				(req, res) -> {
					BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
					String fbToken = req.queryParams("fbToken");
					com.restfb.types.User fbUser = FBUtil.getProfile(fbToken);

					try {
						User userFromDB = userModelImpl.getByFBId(fbUser
								.getThirdPartyId());
						// check if there the user record
						if (userFromDB == null
								|| userFromDB.getUserId() == null) {
							// log the issue and fail
							LOG.debug("User not found");
							res.status(404);
							res.type("application/json");
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"User not found"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}

						// else generate token
						String token = JJWTUtil.issueToken(userFromDB
								.getUserId());
						if (token != null && !token.isEmpty()) {
							userFromDB.setToken(token);
							userFromDB.setAuthenticated(Boolean.TRUE);
							res.status(200);
							res.type("application/json");

							buckbuddyResponse.setData(mapper.convertValue(
									User.obfuscate(userFromDB),
									ObjectNode.class));
							return mapper.writeValueAsString(buckbuddyResponse);
						} else {
							// log the issue and fail
							LOG.debug("JJWTUtil.issueToken did not provide valid token:"
									+ token);
							res.status(500);
							res.type("application/json");
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											BuckBuddyException.UNKNOWN));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (BuckBuddyException e) {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse.setError(mapper.createObjectNode()
								.put("message", BuckBuddyException.UNKNOWN));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
				});
	}

	private void initializeProfileRoutes() {

		get("/users/byToken/:token/verificationFieldsNeeded",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token = req.params(":token");
						if(token==null || token.isEmpty()) {
							res.status(400);
							buckbuddyResponse.setError(mapper.createObjectNode().put(
									"message", "Token is mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						String userId = JJWTUtil.getSubject(token);
						JsonNode fieldsNeeded = userModelImpl
								.getVerificationFieldsNeededForId(userId);
						if (fieldsNeeded != null) {
							res.status(200);
							res.type("application/json");
							return mapper.writeValueAsString(fieldsNeeded);
						} else {
							res.status(404);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		get("/users/byToken/:token/isTransfersEnabled",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token = req.params(":token");
						if (token == null || token.isEmpty()) {
							res.status(400);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"Token is mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						String userId = JJWTUtil.getSubject(token);
						Boolean isTransfersEnabled = userModelImpl
								.isTransfersEnabled(userId);
						if (isTransfersEnabled != null) {
							res.status(200);
							res.type("application/json");
							return mapper.createObjectNode().put(
									"isTransfersEnabled", isTransfersEnabled);
						} else {
							res.status(404);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		get("/users/byToken/:token/balance",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token = req.params(":token");
						if (token == null || token.isEmpty()) {
							res.status(400);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"Token is mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						String userId = JJWTUtil.getSubject(token);
						if (userId == null) {
							res.status(401);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"Valid Token is required.");
						}

						User user = userModelImpl.getById(userId, Boolean.FALSE, Boolean.FALSE);
						if (user == null) {
							res.status(404);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"User not found.");
						}
						if (user.getPaymentProfiles() == null
								|| user.getPaymentProfiles().getStripe() == null
								|| user.getPaymentProfiles().getStripe()
										.getAccountId() == null) {
							LOG.debug(
									"User Stripe account details do not exist for id:{}",
									userId);
							res.status(404);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"User Stripe account details do not exist"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						String connectedStripeAccountId = user
								.getPaymentProfiles().getStripe()
								.getAccountId();
						JsonNode balanceResponseNode = StripeUtil
								.retrieveBalance(connectedStripeAccountId);
						res.status(200);
						res.type("application/json");
						return mapper.writeValueAsString(balanceResponseNode);
						
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
		post("/users/:userId/uploadProfilePic", "multipart/form-data", (req,
				res) -> {
			BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
			String userId = (req.params(":userId"));

			Map<String, Object> userMap = new HashMap<>();
			userMap.put("userId", userId);
			Path tempDirPath = Files.createTempDirectory("buckbuddy-upload");
			// the directory location where files will be stored
				String location = tempDirPath.toString();
				// the maximum size allowed for uploaded files
				long maxFileSize = 100000000;
				// the maximum size allowed for multipart/form-data requests
				long maxRequestSize = 100000000;
				// the size threshold after which
				// files will be written to disk
				int fileSizeThreshold = 1024;

				MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
						location, maxFileSize, maxRequestSize,
						fileSizeThreshold);
				req.raw().setAttribute("org.eclipse.jetty.multipartConfig",
						multipartConfigElement);

				Collection<Part> parts = req.raw().getParts();
				if (LOG.isDebugEnabled()) {
					for (Part part : parts) {
						LOG.debug("Name: " + part.getName());
						LOG.debug("Size: " + part.getSize());
						LOG.debug("Filename: " + part.getSubmittedFileName());
					}
				}

				Part uploadedFile = req.raw().getPart("image");

				String fileName = uploadedFile.getSubmittedFileName().contains(
						"?") ? uploadedFile.getSubmittedFileName().replaceAll(
						"\\?.*", "") : uploadedFile.getSubmittedFileName();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

				String profilePicS3Path = userMap.get("userId") + "/"
						+ USER_PROFILE_PIC_PREFIX + "/" + fileName;

				String metadataString = "image/" + extension;
				boolean success = AWSS3Util.upload(S3_BUCKET, profilePicS3Path,
						uploadedFile.getInputStream(), metadataString);
				if (!success) {
					LOG.error("Could not upload profile pic {} for {}",
							uploadedFile.getSubmittedFileName().toString(),
							userMap.get("userId"));
					res.status(500);
					res.type("application/json");
					buckbuddyResponse.setError(mapper.createObjectNode().put(
							"message", "Could not upload profile pic."));
					return mapper.writeValueAsString(buckbuddyResponse);

				} else {
					userMap.put("profilePic", ASSETS_URL + profilePicS3Path);
					Map<String, Object> updateResponse = userModelImpl
							.updatePartial(userMap);
					if (updateResponse != null
							&& updateResponse.get("replaced") instanceof Long
							&& ((Long) updateResponse.get("replaced")) > 0) {
						LOG.info("Updated user {} with s3 profile pic url {}",
								userMap.get("userId"),
								userMap.get("profilePic"));

						res.status(200);
						res.type("application/json");
						buckbuddyResponse.setData(mapper.convertValue(userMap,
								ObjectNode.class));
						return mapper.writeValueAsString(buckbuddyResponse);
					} else {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse
								.setError(mapper
										.createObjectNode()
										.put("message",
												"Could not update user with profile pic url"));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
				}
			});

		post("/users/:userId/paymentProfile",
				(req, res) -> {
					BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
					String userId = (req.params(":userId"));
//					String authorizationCode = (req.queryParams("code"));
					String tosTimestampInMillisString = (req.queryParams("tosTimestampInMillis"));
					String tosIP = getClientIpAddress(req);
					if (tosTimestampInMillisString == null || tosIP == null
							|| tosTimestampInMillisString.isEmpty()
							|| tosIP.isEmpty()) {
						res.status(400);
						buckbuddyResponse.setError(mapper.createObjectNode().put(
								"message", "TOS Timestamp and IP are mandatory"));
						res.type("application/json");
						return mapper.writeValueAsString(buckbuddyResponse);
					}
					Long tosTimestampInMillis = Long.valueOf(tosTimestampInMillisString);

					// get user
					User userFromDB = userModelImpl.getById(userId);
					
					Map<String, Object> userMap = new HashMap<>();
					userMap.put("userId", userId);
					
					JsonNode paymentStripeProfileNode = null;
					try {
						paymentStripeProfileNode = StripeUtil
								.createManagedAccount(userFromDB.getEmail(),
										STRIPE_BUSINESS_URL,
										tosTimestampInMillis / 1000L, tosIP);
						userMap = userModelImpl
								.updateUserMapWithPaymentProfile(userMap,
										paymentStripeProfileNode);

						Map<String, Object> response = userModelImpl
								.updatePartial(userMap);

						if (response != null
								&& response.get("replaced") instanceof Long
								&& ((Long) response.get("replaced")) > 0) {
							res.status(204);
							res.type("application/json");
						} else if (response != null
								&& response.get("errors") instanceof Long
								&& ((Long) response.get("errors")) > 0) {
							res.status(500);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Could not update user with payment info"));
						}

					} catch (BuckBuddyException e) {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse
								.setError(mapper
										.createObjectNode()
										.put("message", e.getMessage()));
					}
					return mapper.writeValueAsString(buckbuddyResponse);
				});
		patch("/users/byToken/:token/fieldsNeeded",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token = req.params(":token");
						if (token == null || token.isEmpty()) {
							res.status(400);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"Token is mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						Map<String, Object> userMap = mapper.readValue(
								req.body(), Map.class);
						String userId = JJWTUtil.getSubject(token);
						userMap.put("userId", userId);

						if (userMap.get("external_account") == null
								|| userMap.get("external_account").toString()
										.isEmpty()
								|| userMap.get("legal_entity.dob.day") == null
								|| userMap.get("legal_entity.dob.day")
										.toString().isEmpty()
								|| userMap.get("legal_entity.dob.month") == null
								|| userMap.get("legal_entity.dob.month")
										.toString().isEmpty()
								|| userMap.get("legal_entity.dob.year") == null
								|| userMap.get("legal_entity.dob.year")
										.toString().isEmpty()
								|| userMap.get("legal_entity.first_name") == null
								|| userMap.get("legal_entity.first_name")
										.toString().isEmpty()
								|| userMap.get("legal_entity.last_name") == null
								|| userMap.get("legal_entity.last_name")
										.toString().isEmpty()) {
							res.status(400);
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Bad nessage some fields are missing. DOB day, month and year. Legal First name and Last name with Bank account token are all required"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						User user = userModelImpl.getById(userId, Boolean.FALSE, Boolean.FALSE);
						if (user == null) {
							LOG.debug("User not found");
							res.status(404);
							res.type("application/json");
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"User not found"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						if (user.getPaymentProfiles() == null
								|| user.getPaymentProfiles().getStripe() == null
								|| user.getPaymentProfiles().getStripe()
										.getAccountId() == null) {
							LOG.debug("User Stripe account details do not exist id:{}", userId);
							res.status(404);
							res.type("application/json");
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"User Stripe account details do not exist"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						JsonNode stripeAccountNode = StripeUtil
								.updateManagedAccountWithBankAccountAndLegalEntity(
										user.getPaymentProfiles().getStripe()
												.getAccountId(),
										userMap.get("external_account")
												.toString(),
										userMap.get("legal_entity.dob.day")
												.toString(),
										userMap.get("legal_entity.dob.month")
												.toString(),
										userMap.get("legal_entity.dob.year")
												.toString(),
										userMap.get("legal_entity.first_name")
												.toString(),
										userMap.get("legal_entity.last_name")
												.toString(), "individual");
						if(stripeAccountNode==null) {
							res.status(500);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Could not update user with payment info"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						userMap = userModelImpl
								.updateUserMapWithPaymentProfile(userMap,
										stripeAccountNode);

						Map<String, Object> response = userModelImpl
								.updatePartial(userMap);

						if (response != null
								&& response.get("replaced") instanceof Long
								&& ((Long) response.get("replaced")) > 0) {
							res.status(204);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						} else {
							res.status(500);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Could not update user with payment info"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
	}

	private String getClientIpAddress(Request req) {
		if(req.ip()!=null && !req.ip().isEmpty()) {
			return req.ip();
		}
	    for (String header : HEADERS_TO_TRY) {
	        String ip = req.headers(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }
	    return "";
	}

	private void initializeActivityRoutes() {
		patch("/users/:token/activate",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token = req.params(":token");
						String userId = JJWTUtil.getSubject(token);

						if (userId == null) {
							res.status(401);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"Token is required.");
						}

						User user = userModelImpl.getById(userId);
						if (user == null) {
							res.status(404);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"User not found.");
						}
						Map<String, Object> userMap = new HashMap<>();
						userMap.put("userId", user.getUserId());

						Map<String, Object> response = userModelImpl
								.activate(userMap);
						if (response != null
								&& response.get("replaced") instanceof Long
								&& ((Long) response.get("replaced")) > 0) {
							res.status(200);
							res.type("application/json");

							// activate campaign
							JsonNode campaignActivateJson=null;
							try {
								campaignActivateJson = RESTClientUtil.sendPOST(
										CAMPAIGN_SERVICE_BASE_ACTIVATE_BY_USER_ID
												.replace(":userId", userId), null);
							} catch(Exception e) {
								LOG.error(
										"Could not activate campaign for user id {}",
										userId);
								return mapper.createObjectNode().put(
										"error",
										"Could not activate campaign for:"
												+ userId);
							}
							if (campaignActivateJson != null
									&& !campaignActivateJson.findPath("errors")
											.isMissingNode()) {
								LOG.error(
										"Could not activate campaign for user id {}",
										userId);
								return mapper.createObjectNode().put(
										"error",
										"Could not activate campaign for:"
												+ userId);
							}
							return mapper.writeValueAsString(buckbuddyResponse);
						} else if (response != null
								&& response.get("errors") instanceof Long
								&& ((Long) response.get("errors")) > 0) {
							res.status(500);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"Could not activate User:" + userId);
						} else {
							res.status(204);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								"Could not activate User:");
					}
				});

		patch("/users/:token/deActivate",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token=req.params(":token");
						String userId=JJWTUtil.getSubject(token);

						if (userId == null) {
							res.status(401);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"Token is required.");
						}

						User user = userModelImpl.getById(userId);
						if (user == null) {
							res.status(404);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"User not found.");
						}
						Map<String, Object> userMap = new HashMap<>();
						userMap.put("userId", user.getUserId());

						Map<String, Object> response = userModelImpl
								.deActivate(userMap);
						if (response != null
								&& response.get("replaced") instanceof Long
								&& ((Long) response.get("replaced")) > 0) {
							res.status(204);
							res.type("application/json");
						} else if (response != null
								&& response.get("errors") instanceof Long
								&& ((Long) response.get("errors")) > 0) {
							res.status(500);
							res.type("application/json");
						} else {
							res.status(204);
							res.type("application/json");
						}
						return mapper.writeValueAsString(response);
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});

		post("/users/byToken/:token/cashout",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token = req.params(":token");
						String userId = JJWTUtil.getSubject(token);

						if (userId == null) {
							res.status(401);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"Token is required.");
						}

						User user = userModelImpl.getById(userId, Boolean.FALSE, Boolean.FALSE);
						if (user == null) {
							res.status(404);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"User not found.");
						}

						String currencyString = req
								.queryParams("currencyString");
						String amountInCentsString = req
								.queryParams("amountInCents");
						if (currencyString == null || currencyString.isEmpty()) {
							res.status(400);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"currencyString  is mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}

						if (amountInCentsString == null
								|| amountInCentsString.isEmpty()
								|| new BigDecimal(amountInCentsString)
										.compareTo(BigDecimal.ZERO) <= 0) {
							res.status(400);
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Transfer amountInCents should be greater than absolute 0"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						if (user.getPaymentProfiles() == null
								|| user.getPaymentProfiles().getStripe() == null
								|| user.getPaymentProfiles().getStripe()
										.getAccountId() == null) {
							LOG.debug(
									"User Stripe account details do not exist id:{}",
									userId);
							res.status(404);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"User Stripe account details do not exist"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						String connectedStripeAccountId = user
								.getPaymentProfiles().getStripe()
								.getAccountId();
						BigDecimal amountInCents = new BigDecimal(
								amountInCentsString);
						JsonNode transferResponse = StripeUtil
								.transferToBankAccount(
										connectedStripeAccountId,
										amountInCents, currencyString);
						JsonNode balanceResponseNode = StripeUtil
								.retrieveBalance(connectedStripeAccountId);
						Map<String, Object> userMap = new HashMap<>();
						userMap.put("userId", user.getUserId());

						userMap = userModelImpl
								.updateUserMapWithBalanceResponse(userMap,
										balanceResponseNode);

						Map<String, Object> response = userModelImpl
								.updatePartial(userMap);

						if (response != null
								&& response.get("replaced") instanceof Long
								&& ((Long) response.get("replaced")) > 0) {
							res.status(204);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						} else {
							res.status(500);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Could not update user with Balance Info"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (UserDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								UserDataException.UNKNOWN);
					}
				});
	}

	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		
		UserRouter userRouter = new UserRouter();
		userRouter.initializeCRUDRoutes();
		userRouter.initializeLoginRoutes();
		userRouter.initializeProfileRoutes();
		userRouter.initializeActivityRoutes();
	}

}