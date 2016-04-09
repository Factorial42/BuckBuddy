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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: Auto-generated Javadoc
/**
 * The Class UserRouter.
 */
public class UserRouter {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(UserRouter.class);

	/** The Constant mapper. */
	private static final ObjectMapper mapper = new ObjectMapper();

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
		post("/users", (req, res) -> {
			User user = mapper.readValue(req.body(), User.class);
			// use a HashMap representation for the object
			// instead of pojo since rethinkDB.expr passes userId as null
			// which the rethinkdb engine does not like
			// TypeReference<HashMap<String, Object>> typeRef = new
			// TypeReference<HashMap<String, Object>>() {
			// };
			//
			// Map<String, Object> userMap = mapper.readValue(req.body(),
			// typeRef);

				try {
					Map<String, Object> response = userModelImpl.create(user);
					if (response != null
							&& response.get("inserted") instanceof Long
							&& ((Long) response.get("inserted")) > 0) {
						res.status(201);
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
						res.status(204);
					}
					res.type("application/json");
					return mapper.writeValueAsString(response);
				} catch (UserDataException ude) {
					res.status(500);
					res.type("application/json");
					return mapper.createObjectNode().put("error",
							UserDataException.UNKNOWN);
				}
			});
		get("/users/:email",
				(req, res) -> {
					try {
						Map<String, Object> response = userModelImpl
								.getById(req.params(":email"));
						if (response != null) {
							res.status(200);
							res.type("application/json");
						} else {
							res.status(404);
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
		put("/users/:email",
				(req, res) -> {
					try {
						User user = mapper.readValue(req.body(), User.class);
						if (user.getEmail() != null
								&& !req.params(":email")
										.equals(user.getEmail())) {
							res.status(403);
							res.type("application/json");
							return mapper
									.createObjectNode()
									.put("error",
											"email in document does not match the requested email in update request.");
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
		patch("/users/:email",
				(req, res) -> {
					try {
						User user = mapper.readValue(req.body(), User.class);
						if (user.getEmail() != null
								&& !req.params(":email")
										.equals(user.getEmail())) {
							res.status(403);
							res.type("application/json");
							return mapper
									.createObjectNode()
									.put("error",
											"email in document does not match the requested email in update request.");
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
		delete("/users/:email",
				(req, res) -> {
					try {
						String email = req.params(":email");
						Map<String, Object> response = userModelImpl
								.deleteById(email);

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

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		UserRouter userRouter = new UserRouter();
		userRouter.initializeCRUDRoutes();
	}

}