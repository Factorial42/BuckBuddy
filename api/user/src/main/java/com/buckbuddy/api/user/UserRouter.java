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
import com.buckbuddy.core.exceptions.BuckBuddyException;
import com.buckbuddy.core.security.JJWTUtil;
import com.buckbuddy.core.security.SecurityUtil;
import com.buckbuddy.core.social.FBUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.scribejava.core.model.OAuth2AccessToken;

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

			try {
				String token = null;
				User user = mapper.readValue(req.body(), User.class);
				Map<String, Object> response = userModelImpl.create(user);
				if (response != null
						&& response.get("inserted") instanceof Long
						&& ((Long) response.get("inserted")) > 0) {
					res.status(201);

					// generate token
				token = JJWTUtil.issueToken(user.getUserId());
				response.put("authenticated", Boolean.TRUE);
				response.put("token", token);
			} else if (response != null
					&& response.get("errors") instanceof Long
					&& ((Long) response.get("errors")) > 0) {
				if (((String) response.get("first_error"))
						.startsWith("Duplicate primary key")) {
					res.status(403);
				} else {
					res.status(500);
				}
			} else {
				res.status(200);
				// generate token
				token = JJWTUtil.issueToken(user.getUserId());
				response.put("authenticated", Boolean.TRUE);
				response.put("token", token);
			}
			res.type("application/json");
			return mapper.writeValueAsString(response);
		} catch (UserDataException ude) {
			res.status(500);
			res.type("application/json");
			return mapper.createObjectNode().put("error",
					UserDataException.UNKNOWN);
		}
	}	);

		// regular user login
		post("/users/login",
				(req, res) -> {
					User user = mapper.readValue(req.body(), User.class);

					try {
						// get user
						User userFromDB = userModelImpl.getByEmail(
								user.getEmail(), Boolean.FALSE);

						// check if there the user record
						if (userFromDB == null
								|| userFromDB.getUserId() == null) {
							// log the issue and fail
							LOG.debug("User not found");
							user.setAuthenticated(Boolean.FALSE);
							res.status(404);
							res.type("application/json");
						}

						// check if password matches
						if (userFromDB.getPassword() != null
								&& !userFromDB.getPassword().equals(
										SecurityUtil.encrypt(
												user.getPassword(),
												SecurityUtil.SHA_256))) {
							// log the issue and fail
							LOG.debug("Passwords do not match");
							user.setAuthenticated(Boolean.FALSE);
							res.status(401);
							res.type("application/json");
						}

						// else generate token
						String token = JJWTUtil.issueToken(userFromDB
								.getUserId());
						if (token != null && !token.isEmpty()) {
							userFromDB.setToken(token);
							userFromDB.setAuthenticated(Boolean.TRUE);
							res.status(200);
							res.type("application/json");
							return mapper.writeValueAsString(userFromDB);
						} else {
							// log the issue and fail
							LOG.debug("JJWTUtil.issueToken did not provide valid token:"
									+ token);
							user.setAuthenticated(Boolean.FALSE);
							res.status(401);
							res.type("application/json");
						}
					} catch (BuckBuddyException e) {
						user.setAuthenticated(Boolean.FALSE);
						res.status(401);
						res.type("application/json");
					}
					return user;
				});

		// user authenticate
		post("/users/authenticate",
				(req, res) -> {
					User user = mapper.readValue(req.body(), User.class);

					// generate token
					String token = user.getToken();
					boolean authenticated = JJWTUtil.authenticate(
							user.getUserId(), user.getToken());
					if (authenticated) {
						user = userModelImpl.getById(user.getUserId());
						user.setToken(token);
						user.setAuthenticated(authenticated);
					}

					res.status(200);
					res.type("application/json");
					return user;
				});

		// reset password
		post("/users/resetPassword", (req, res) -> {
			return null;
		});

		// get fb user profile
		get("/users/fb/profile", (req, res) -> {
			String code = req.queryParams("code");
			OAuth2AccessToken accessToken = FBUtil.extendToken(code);
			com.restfb.types.User user = FBUtil.getProfile(accessToken);
			res.status(200);
			res.type("application/json");
			return mapper.writeValueAsString(userModelImpl
					.createUserFromFBProfile(user, accessToken));
		});

		post("/users/fb/signup", (req, res) -> {

			try {
				String token = null;
				User user = mapper.readValue(req.body(), User.class);
				Map<String, Object> response = userModelImpl.create(user, Boolean.FALSE);
				if (response != null
						&& response.get("inserted") instanceof Long
						&& ((Long) response.get("inserted")) > 0) {
					res.status(201);

					// generate token
				token = JJWTUtil.issueToken(user.getUserId());
				response.put("authenticated", Boolean.TRUE);
				response.put("token", token);
			} else if (response != null
					&& response.get("errors") instanceof Long
					&& ((Long) response.get("errors")) > 0) {
				if (((String) response.get("first_error"))
						.startsWith("Duplicate primary key")) {
					res.status(403);
				} else {
					res.status(500);
				}
			} else {
				res.status(200);
				// generate token
				token = JJWTUtil.issueToken(user.getUserId());
				response.put("authenticated", Boolean.TRUE);
				response.put("token", token);
			}
			res.type("application/json");
			return mapper.writeValueAsString(response);
		} catch (UserDataException ude) {
			res.status(500);
			res.type("application/json");
			return mapper.createObjectNode().put("error",
					UserDataException.UNKNOWN);
		}
	}	);
		// fb user login
		post("/users/fb/login",
				(req, res) -> {
					String code = req.queryParams("code");
					OAuth2AccessToken accessToken = FBUtil.extendToken(code);
					com.restfb.types.User fbUser = FBUtil
							.getProfile(accessToken);

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
							User user = new User();
							user.setAuthenticated(Boolean.FALSE);
							return mapper.writeValueAsString(user);
						}

						// else generate token
						String token = JJWTUtil.issueToken(userFromDB
								.getUserId());
						if (token != null && !token.isEmpty()) {
							userFromDB.setToken(token);
							userFromDB.setAuthenticated(Boolean.TRUE);
							res.status(200);
							res.type("application/json");
							return mapper.writeValueAsString(userFromDB);
						} else {
							// log the issue and fail
							LOG.debug("JJWTUtil.issueToken did not provide valid token:"
									+ token);
							res.status(401);
							res.type("application/json");
							User user = new User();
							user.setAuthenticated(Boolean.FALSE);
							return mapper.writeValueAsString(user);
						}
					} catch (BuckBuddyException e) {
						res.status(401);
						res.type("application/json");
						User user = new User();
						user.setAuthenticated(Boolean.FALSE);
						return user;
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