/**
 * 
 */
package com.buckbuddy.api.user.data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.api.user.data.model.User;
import com.buckbuddy.api.user.data.model.UserSlug;
import com.buckbuddy.core.exceptions.BuckBuddyException;
import com.buckbuddy.core.security.SecurityUtil;
import com.buckbuddy.core.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

// TODO: Auto-generated Javadoc
/**
 * The Class UserModelImpl.
 */
public class UserModelImpl implements UserModel {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(UserModelImpl.class);

	private static final String DEV_S3_BUCKET = "user.assets.dev.buckbuddy.com";

	/** The rethink db. */
	private static RethinkDB rethinkDB;

	/** The conn. */
	private static Connection conn;

	/** The Constant mapper. */
	private static final ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());

	/**
	 * Instantiates a new user model impl.
	 */
	public UserModelImpl() {
		rethinkDB = RethinkDB.r;
		conn = rethinkDB.connection().hostname("localhost").port(28015)
				.db("buckbuddy").connect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buckbuddy.api.user.data.UserModel#create(com.buckbuddy.api.user.data
	 * .model.User)
	 */
	@Override
	public Map<String, Object> create(User user, Boolean withPassword)
			throws UserDataException {

		// add validation

		try {
			user.setUserId(SecurityUtil.encrypt(user.getEmail(),
					SecurityUtil.SHA_256));
			user.setS3handle(SecurityUtil.encrypt(user.getEmail(),
					SecurityUtil.MD5));
			if (withPassword) {
				user.setPassword(SecurityUtil.encrypt(user.getPassword(),
						SecurityUtil.SHA_256));
			}
		} catch (BuckBuddyException e) {
			LOG.error(UserDataException.SECURITY_EXCEPTION, e);
			throw new UserDataException(UserDataException.SECURITY_EXCEPTION);
		}
		user.setCreatedAt(OffsetDateTime.now());
		user.setLastUpdatedAt(user.getCreatedAt());

		// mark the user active for now. in future mark a user active only after
		// a email verification
		user.setActive(Boolean.FALSE);

		Map<String, Object> userResponse, userWithSlugResponse, userSlugResponse;

		try {
			userResponse = rethinkDB.table("user").insert(rethinkDB.expr(user))
					.run(conn);

			if (userResponse != null
					&& userResponse.get("inserted") instanceof Long
					&& ((Long) userResponse.get("inserted")) > 0) {
				// if user is saved successfully, create user slug
				UserSlug userSlug = new UserSlug();
				userSlug.setCreatedAt(user.getCreatedAt());
				userSlug.setLastUpdatedAt(user.getCreatedAt());
				userSlug.setUserId(user.getUserId());
				List<String> slugList = new ArrayList<>();
				if (user.getFirstName() != null
						&& !user.getFirstName().isEmpty()
						&& user.getLastName() != null
						&& !user.getLastName().isEmpty()) {
					slugList.add(user.getFirstName());
					slugList.add(user.getLastName());
				} else {
					slugList.add(user.getName());
				}

				String userSlugString = null;
				boolean success = false;
				for (int i = 0; i < 5; i++) {
					// try a max of 5 times saving slug with current timestamp
					// if it's already taken
					slugList.add(String.valueOf(OffsetDateTime.now()
							.toEpochSecond()));
					userSlugString = StringUtils.slugify(slugList);
					userSlug.setUserSlug(userSlugString);
					userSlugResponse = rethinkDB.table("userSlug")
							.insert(rethinkDB.expr(userSlug)).run(conn);
					if (userSlugResponse != null
							&& userSlugResponse.get("inserted") instanceof Long
							&& ((Long) userSlugResponse.get("inserted")) > 0) {
						success = true;
						break;
					} else {
						slugList.remove(slugList.size() - 1);
					}
				}
				if (!success) {
					LOG.error("Could not create user slug. Too many conflicts. Try again later");
				} else {
					// save user object with slug info
					Map<String, Object> userWithSlug = new HashMap<>();
					userWithSlug.put("userSlug", userSlugString);
					userWithSlugResponse = rethinkDB.table("user")
							.get(user.getUserId())
							.update(rethinkDB.expr(userWithSlug)).run(conn);
					if (userWithSlugResponse == null
							&& userResponse.get("errors") instanceof Long
							&& ((Long) userResponse.get("errors")) > 0) {
						LOG.error(
								"Error while saving user object with slug info",
								userResponse.get("first_error"));
					}
				}
			}
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> create(User user) throws UserDataException {
		return create(user, Boolean.TRUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buckbuddy.api.user.data.UserModel#update(com.buckbuddy.api.user.data
	 * .model.User)
	 */
	@Override
	public Map<String, Object> update(User user) throws UserDataException {

		// add validation
		user.setLastUpdatedAt(OffsetDateTime.now());

		Map<String, Object> userResponse;
		try {
			userResponse = rethinkDB.table("user").get(user.getUserId())
					.replace(rethinkDB.expr(user)).run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buckbuddy.api.user.data.UserModel#update(com.buckbuddy.api.user.data
	 * .model.User)
	 */
	@Override
	public Map<String, Object> updatePartial(Map<String, Object> user)
			throws UserDataException {

		// add validation
		user.put("lastUpdatedAt", OffsetDateTime.now());

		Map<String, Object> userResponse;
		try {
			// encrypt password
			if (user.get("password") != null
					&& !((String) user.get("password")).isEmpty()) {
				user.put("password", SecurityUtil.encrypt(
						(String) user.get("password"), SecurityUtil.SHA_256));
			}
			userResponse = rethinkDB.table("user").get(user.get("userId"))
					.update(rethinkDB.expr(user)).run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buckbuddy.api.user.data.UserModel#getById(java.lang.String,
	 * java.lang.Boolean)
	 */
	@Override
	public User getById(String userId, Boolean obfuscate, Boolean minified)
			throws UserDataException {
		Map<String, Object> userResponse;
		try {
			if(!minified) {
				userResponse = rethinkDB.table("user").get(userId).run(conn);
			} else {
				userResponse = rethinkDB.table("user").get(userId).pluck("userId", "userSlug", "name", "profilePic").run(conn);
			}
			if (userResponse!=null && obfuscate) {
				userResponse = User.obfuscate(userResponse);
			}
			return mapper.convertValue(userResponse, User.class);
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buckbuddy.api.user.data.UserModel#getById(java.lang.String)
	 */
	@Override
	public User getById(String userId) throws UserDataException {
		return getById(userId, Boolean.TRUE, Boolean.FALSE);
	}

	@Override
	public User getByFBId(String fbId) throws UserDataException {
		Map<String, Object> userResponse = new HashMap<>();
		;
		try {
			Cursor cursor = rethinkDB
					.table("user")
					.filter(rethinkDB.hashMap(
							"socialProfiles",
							rethinkDB.hashMap("facebookProfile",
									rethinkDB.hashMap("facebookID", fbId))))
					.run(conn);
			if (cursor.hasNext()) {
				userResponse = (Map<String, Object>) cursor.next();
				userResponse = User.obfuscate(userResponse);
			}
			return mapper.convertValue(userResponse, User.class);
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	@Override
	public User getByUserSlug(String userSlug) throws UserDataException {
		Map<String, Object> userResponse = new HashMap<>();
		;
		try {
			Cursor cursor = rethinkDB.table("user")
					.filter(rethinkDB.hashMap("userSlug", userSlug)).run(conn);
			if (cursor.hasNext()) {
				userResponse = (Map<String, Object>) cursor.next();
				userResponse = User.obfuscate(userResponse);
			}
			return mapper.convertValue(userResponse, User.class);
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.buckbuddy.api.user.data.UserModel#delete(com.buckbuddy.api.user.data
	 * .model.User)
	 */
	@Override
	public Map<String, Object> delete(User user) throws UserDataException {
		return deleteById(user.getUserId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buckbuddy.api.user.data.UserModel#deleteById(java.lang.String)
	 */
	@Override
	public Map<String, Object> deleteById(String userId)
			throws UserDataException {
		Map<String, Object> userResponse;
		try {
			userResponse = rethinkDB.table("user").get(userId).delete()
					.run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buckbuddy.api.user.data.UserModel#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String userId) throws UserDataException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buckbuddy.api.user.data.UserModel#emailExists(java.lang.String)
	 */
	@Override
	public boolean emailExists(String email) throws UserDataException {
		// TODO Auto-generated method stub
		return false;
	}

	public User createUserFromFBProfile(com.restfb.types.User fbUser,
			String fbToken) {
		return User.createUserFromFBProfile(fbUser, fbToken);
	}

	@Override
	public Map<String, Object> activate(Map<String, Object> userMap)
			throws UserDataException {

		// add validation
		userMap.put("lastUpdatedAt", OffsetDateTime.now());
		userMap.put("active", Boolean.TRUE);

		Map<String, Object> userResponse;
		try {
			userResponse = rethinkDB.table("user")
					.get(userMap.get("userId"))
					.update(rethinkDB.expr(userMap)).run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> deActivate(Map<String, Object> userMap)
			throws UserDataException {

		// add validation
		userMap.put("lastUpdatedAt", OffsetDateTime.now());
		userMap.put("active", Boolean.FALSE);

		Map<String, Object> userResponse;
		try {
			userResponse = rethinkDB.table("user")
					.get(userMap.get("userId"))
					.update(rethinkDB.expr(userMap)).run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.DB_EXCEPTION, e);
			throw new UserDataException(UserDataException.DB_EXCEPTION);
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getByEmail(String email) throws UserDataException,
			BuckBuddyException {
		// user id is email encrypted. so get by user id
		return getById(SecurityUtil.encrypt(email, SecurityUtil.SHA_256),
				Boolean.TRUE, Boolean.FALSE);
	}

	@Override
	public User getByEmail(String email, Boolean obfuscate)
			throws UserDataException, BuckBuddyException {
		// user id is email encrypted. so get by user id
		return getById(SecurityUtil.encrypt(email, SecurityUtil.SHA_256),
				obfuscate, Boolean.FALSE);
	}
}
