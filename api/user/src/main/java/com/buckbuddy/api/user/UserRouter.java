package com.buckbuddy.api.user;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.api.user.data.UserDataException;
import com.buckbuddy.api.user.data.UserModel;
import com.buckbuddy.api.user.data.UserModelImpl;
import com.buckbuddy.api.user.data.model.User;
import com.buckbuddy.core.BuckBuddyResponse;
import com.buckbuddy.core.exceptions.BuckBuddyException;
import com.buckbuddy.core.security.JJWTUtil;
import com.buckbuddy.core.security.SecurityUtil;
import com.buckbuddy.core.social.FBUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
						User user = mapper.readValue(req.body(), User.class);
						if (user.getUserId() != null) {
							res.status(403);
							res.type("application/json");
							return mapper.createObjectNode().put("error",
									"User ID is required.");
						}
						TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
						};

						Map<String, Object> userMap = mapper.readValue(
								req.body(), typeRef);

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
							String loginToken = JJWTUtil.issueToken(userFromDB
									.getUserId());
							userFromDB.setToken(loginToken);
							buckbuddyResponse.setData(mapper.convertValue(
									userFromDB, ObjectNode.class));
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
	}

}