package com.buckbuddy.api.campaign;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.put;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

import com.buckbuddy.api.campaign.data.CampaignDataException;
import com.buckbuddy.api.campaign.data.CampaignModel;
import com.buckbuddy.api.campaign.data.CampaignModelImpl;
import com.buckbuddy.api.campaign.data.model.Campaign;
import com.buckbuddy.api.campaign.data.model.ProfilePic;
import com.buckbuddy.core.BuckBuddyResponse;
import com.buckbuddy.core.exceptions.BuckBuddyException;
import com.buckbuddy.core.security.JJWTUtil;
import com.buckbuddy.core.utils.AWSS3Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CampaignRouter {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(CampaignRouter.class);

	private static final String S3_BUCKET = "user.assets.dev.buckbuddy.com";

	private static final String ASSETS_URL = "http://user.assets.dev.buckbuddy.com/";
	private static final String CAMPAIGN_PROFILE_PIC_PREFIX = "campaign.pic";

	/** The Constant mapper. */
	private static final ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());

	/** The campaign model impl. */
	private CampaignModel campaignModelImpl;

	/**
	 * Instantiates a new campaign router.
	 */
	public CampaignRouter() {
		campaignModelImpl = new CampaignModelImpl();
	}

	/**
	 * Initialize crud routes.
	 */
	public void initializeCRUDRoutes() {
		post("/campaigns/",
				(req, res) -> {
					BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
					try {
						String token = null;
						Campaign campaign = mapper.readValue(req.body(),
								Campaign.class);

						// check for email and password - mandatory fields
						if (campaign.getUserId() == null
								|| campaign.getUserId().isEmpty()) {
							res.status(400);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"UserId is mandatory"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
						Map<String, Object> response = campaignModelImpl
								.create(campaign);
						if (response != null
								&& response.get("inserted") instanceof Long
								&& ((Long) response.get("inserted")) > 0) {

							// success flow
							res.status(201);
							// generate token
							Campaign campaignFromDB = campaignModelImpl
									.getByUserId(campaign.getUserId());
							buckbuddyResponse.setData(mapper.convertValue(
									campaignFromDB, ObjectNode.class));
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
					} catch (CampaignDataException ude) {
						res.status(500);
						res.type("application/json");
						buckbuddyResponse.setError(mapper.createObjectNode()
								.put("message", BuckBuddyException.UNKNOWN));
						return mapper.writeValueAsString(buckbuddyResponse);
					}
				});
		get("/campaigns/byToken/:token",
				(req, res) -> {
					try {
						BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
						String token = req.params(":token");
						String userId = JJWTUtil.getSubject(token);
						Campaign campaign = campaignModelImpl
								.getByUserId(userId);
						if (campaign != null) {
							res.status(200);
							res.type("application/json");
							buckbuddyResponse.setData(mapper.convertValue(
									campaign, ObjectNode.class));
							return mapper.writeValueAsString(buckbuddyResponse);
						} else {
							res.status(404);
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} catch (CampaignDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								CampaignDataException.UNKNOWN);
					}
				});
		get("/campaigns/:campaignId",
				(req, res) -> {
					try {
						Campaign campaign = campaignModelImpl.getById(req
								.params(":campaignId"));
						if (campaign != null) {
							res.status(200);
							res.type("application/json");
						} else {
							res.status(404);
							res.type("application/json");
						}
						return mapper
								.writeValueAsString(campaign != null ? campaign
										: new Campaign());
					} catch (CampaignDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								CampaignDataException.UNKNOWN);
					}
				});
		put("/campaigns/:campaignId",
				(req, res) -> {
					try {
						Campaign campaign = mapper.readValue(req.body(),
								Campaign.class);
						if (campaign.getCampaignId() != null) {
							res.status(403);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"Campaign ID is required.");
						}
						Map<String, Object> response = campaignModelImpl
								.update(campaign);
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
					} catch (CampaignDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								CampaignDataException.UNKNOWN);
					}
				});
		patch("/campaigns/:campaignId",
				(req, res) -> {
					try {

						Map<String, Object> campaignMap = mapper.readValue(
								req.body(), Map.class);
						if (campaignMap.get("campaignId") != null) {
							res.status(403);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"Campaign ID is required.");
						}

						Map<String, Object> response = campaignModelImpl
								.updatePartial(campaignMap);
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
					} catch (CampaignDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								CampaignDataException.UNKNOWN);
					}
				});
		delete("/campaigns/:campaignId",
				(req, res) -> {
					try {
						String campaignId = req.params(":campaignId");
						Map<String, Object> response = campaignModelImpl
								.deleteById(campaignId);

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
					} catch (CampaignDataException ude) {
						res.status(500);
						res.type("application/json");
						return mapper.createObjectNode().put("error",
								CampaignDataException.UNKNOWN);
					}
				});

	}

	private void initializeProfileRoutes() {
		post("/campaigns/:campaignId/uploadProfilePic",
				"multipart/form-data",
				(req, res) -> {
					BuckBuddyResponse buckbuddyResponse = new BuckBuddyResponse();
					String campaignId = (req.params(":campaignId"));
					Integer sequence;
					if (req.queryParams("sequence") != null
							&& !req.queryParams("sequence").isEmpty()) {
						try {
							sequence = Integer.parseInt((req
									.queryParams("sequence")));
						} catch (NumberFormatException e) {
							res.status(400);
							buckbuddyResponse.setError(mapper
									.createObjectNode().put("message",
											"Sequence has to be a number"));
							res.type("application/json");
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					} else {
						sequence = 0;
					}
					String token = (req.queryParams("token"));
					String userId = JJWTUtil.getSubject(token);
					if (userId == null || userId.isEmpty()) {
						res.status(400);
						buckbuddyResponse.setError(mapper.createObjectNode()
								.put("message", "UserId is mandatory"));
						res.type("application/json");
						return mapper.writeValueAsString(buckbuddyResponse);
					}
					Map<String, Object> campaignMap = new HashMap<>();
					campaignMap.put("campaignId", campaignId);
					campaignMap.put("userId", userId);
					Path tempDirPath = Files
							.createTempDirectory("buckbuddy-upload");
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
							LOG.debug("Filename: "
									+ part.getSubmittedFileName());
						}
					}

					Part uploadedFile = req.raw().getPart("image");

					String fileName = uploadedFile.getSubmittedFileName()
							.contains("?") ? uploadedFile
							.getSubmittedFileName().replaceAll("\\?.*", "")
							: uploadedFile.getSubmittedFileName();
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
					String extension = fileName.substring(fileName
							.lastIndexOf(".") + 1);

					String profilePicS3Path = campaignMap.get("campaignId")
							+ "/" + CAMPAIGN_PROFILE_PIC_PREFIX + "/"
							+ fileName;

					String metadataString = "image/" + extension;
					boolean success = AWSS3Util.upload(S3_BUCKET,
							profilePicS3Path, uploadedFile.getInputStream(),
							metadataString);
					if (!success) {
						LOG.error("Could not upload profile pic {} for {}",
								uploadedFile.getSubmittedFileName().toString(),
								campaignMap.get("campaignId"));
						res.status(500);
						res.type("application/json");
						buckbuddyResponse
								.setError(mapper.createObjectNode().put(
										"message",
										"Could not upload profile pic."));
						return mapper.writeValueAsString(buckbuddyResponse);

					} else {
						List<ProfilePic> profilePics = new ArrayList<>();
						ProfilePic profilePic = new ProfilePic();
						profilePic.setUrl(ASSETS_URL + profilePicS3Path);						
						profilePics.add(profilePic);
						
						campaignMap.put("profilePics", profilePics);
						Map<String, Object> updateResponse = campaignModelImpl
								.appendToProfilePics(campaignId, ASSETS_URL
										+ profilePicS3Path, sequence);
						if (updateResponse != null
								&& updateResponse.get("replaced") instanceof Long
								&& ((Long) updateResponse.get("replaced")) > 0) {
							LOG.info(
									"Updated campaign {} with s3 profile pic url {}",
									campaignMap.get("campaignId"),
									profilePic.getUrl());
							Campaign campaign=campaignModelImpl.getById(campaignId);
							campaignMap.put("profilePics", campaign.getProfilePics());
							
							res.status(200);
							res.type("application/json");
							buckbuddyResponse.setData(mapper.convertValue(
									campaignMap, ObjectNode.class));
							return mapper.writeValueAsString(buckbuddyResponse);
						} else if(updateResponse != null
								&& updateResponse.get("skipped") instanceof Long
								&& ((Long) updateResponse.get("skipped")) > 0) {
							res.status(404);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Could not update campaign with profile pic url"));
							return mapper.writeValueAsString(buckbuddyResponse);
						} else {
							res.status(500);
							res.type("application/json");
							buckbuddyResponse
									.setError(mapper
											.createObjectNode()
											.put("message",
													"Could not update campaign with profile pic url"));
							return mapper.writeValueAsString(buckbuddyResponse);
						}
					}
				});
	}

	public static void main(String[] args) {
		Spark.port(4568);
		CampaignRouter campaignRouter = new CampaignRouter();
		campaignRouter.initializeCRUDRoutes();
		campaignRouter.initializeProfileRoutes();
	}

}