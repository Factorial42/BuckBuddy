/**
 * 
 */
package com.buckbuddy.api.user.data;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.api.user.data.model.User;
import com.buckbuddy.core.exceptions.BuckBuddyException;
import com.buckbuddy.core.security.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.scribejava.core.model.OAuth2AccessToken;
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
		user.setLastUpdatedAt(OffsetDateTime.now());

		// mark the user active for now. in future mark a user active only after
		// a email verification
		user.setActive(Boolean.TRUE);

		Map<String, Object> userResponse;

		try {
			userResponse = rethinkDB.table("user").insert(rethinkDB.expr(user))
					.run(conn);
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
			userResponse = rethinkDB.table("user")
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
			if (user.get("password") != null
					&& !((String) user.get("password")).isEmpty()) {
				user.put("password", SecurityUtil.encrypt(
						(String) user.get("password"), SecurityUtil.SHA_256));
			}
			userResponse = rethinkDB.table("user").update(rethinkDB.expr(user))
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
	 * @see com.buckbuddy.api.user.data.UserModel#getById(java.lang.String,
	 * java.lang.Boolean)
	 */
	@Override
	public User getById(String userId, Boolean obfuscate)
			throws UserDataException {
		Map<String, Object> userResponse;
		try {
			userResponse = rethinkDB.table("user").get(userId).run(conn);
			if (obfuscate) {
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
		return getById(userId, Boolean.TRUE);
	}

	@Override
	public User getByFBId(String fbId) throws UserDataException {
		Map<String, Object> userResponse=new HashMap<>();;
		try {
			Cursor cursor = rethinkDB
					.table("user")
					.filter(rethinkDB.hashMap(
							"socialProfiles",
							rethinkDB.hashMap("facebookProfile",
									rethinkDB.hashMap("facebookID", fbId))))
					.run(conn);
			if(cursor.hasNext()) {
				userResponse=(Map<String, Object>) cursor.next();
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
			OAuth2AccessToken accessToken) {
		return User.createUserFromFBProfile(fbUser, accessToken);
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
				Boolean.TRUE);
	}

	@Override
	public User getByEmail(String email, Boolean obfuscate)
			throws UserDataException, BuckBuddyException {
		// user id is email encrypted. so get by user id
		return getById(SecurityUtil.encrypt(email, SecurityUtil.SHA_256),
				obfuscate);
	}
}
