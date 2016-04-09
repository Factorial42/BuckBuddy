/**
 * 
 */
package com.buckbuddy.api.user.data;

import java.time.OffsetDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.api.user.data.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

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
	private static final ObjectMapper mapper = new ObjectMapper();

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
	public Map<String,Object> create(User user) throws UserDataException {

		// add validation
		user.setCreatedAt(OffsetDateTime.now());
		user.setLastUpdatedAt(OffsetDateTime.now());

		Map<String,Object> userResponse;

		try {
			userResponse = rethinkDB.table("user").insert(rethinkDB.expr(user))
					.run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.UNKNOWN, e);
			throw new UserDataException(UserDataException.UNKNOWN);
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
	public Map<String,Object> update(User user) throws UserDataException {

		// add validation
		user.setLastUpdatedAt(OffsetDateTime.now());

		Map<String,Object> userResponse;
		try {
			userResponse = rethinkDB.table("user")
					.replace(rethinkDB.expr(user)).run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.UNKNOWN, e);
			throw new UserDataException(UserDataException.UNKNOWN);
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
	public Map<String,Object> updatePartial(Map<String, Object> user)
			throws UserDataException {

		// add validation
		user.put("lastUpdatedAt", OffsetDateTime.now());

		Map<String,Object> userResponse;
		try {
			userResponse = rethinkDB.table("user").update(rethinkDB.expr(user))
					.run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.UNKNOWN, e);
			throw new UserDataException(UserDataException.UNKNOWN);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buckbuddy.api.user.data.UserModel#getById(java.lang.String)
	 */
	@Override
	public Map<String,Object> getById(String email) throws UserDataException {
		Map<String,Object> userResponse;
		try {
			userResponse = rethinkDB.table("user").get(email).run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.UNKNOWN, e);
			throw new UserDataException(UserDataException.UNKNOWN);
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
	public Map<String,Object> delete(User user) throws UserDataException {
		return deleteById(user.getEmail());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buckbuddy.api.user.data.UserModel#deleteById(java.lang.String)
	 */
	@Override
	public Map<String,Object> deleteById(String email) throws UserDataException {
		Map<String,Object> userResponse;
		try {
			userResponse = rethinkDB.table("user").get(email).delete()
					.run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(UserDataException.UNKNOWN, e);
			throw new UserDataException(UserDataException.UNKNOWN);
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

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
