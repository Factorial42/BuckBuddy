package com.buckbuddy.api.user.data;

import java.util.Map;

import com.buckbuddy.api.user.data.model.User;
import com.buckbuddy.core.exceptions.BuckBuddyException;
import com.fasterxml.jackson.databind.JsonNode;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserModel.
 */
public interface UserModel {

	/**
	 * Creates the.
	 *
	 * @param user
	 *            the user
	 * @return the string
	 * @throws UserDataException
	 *             the user data exception
	 */
	public Map<String, Object> create(User user) throws UserDataException;

	/**
	 * Update.
	 *
	 * @param user
	 *            the user
	 * @return the string
	 * @throws UserDataException
	 *             the user data exception
	 */
	public Map<String, Object> update(User user) throws UserDataException;

	/**
	 * Update partial.
	 *
	 * @param user
	 *            the user
	 * @return the string
	 * @throws UserDataException
	 *             the user data exception
	 */
	public Map<String, Object> updatePartial(Map<String, Object> user)
			throws UserDataException;

	/**
	 * Gets the by id.
	 *
	 * @param userId
	 *            the user id
	 * @return the by id
	 * @throws UserDataException
	 *             the user data exception
	 */
	public User getById(String userId) throws UserDataException;

	/**
	 * Delete.
	 *
	 * @param user
	 *            the user
	 * @return the user
	 * @throws UserDataException
	 *             the user data exception
	 */
	public Map<String, Object> delete(User user) throws UserDataException;

	/**
	 * Delete by id.
	 *
	 * @param userId
	 *            the user id
	 * @return the user
	 * @throws UserDataException
	 *             the user data exception
	 */
	public Map<String, Object> deleteById(String userId)
			throws UserDataException;

	/**
	 * Exists.
	 *
	 * @param userId
	 *            the user id
	 * @return true, if successful
	 * @throws UserDataException
	 *             the user data exception
	 */
	public boolean exists(String userId) throws UserDataException;

	/**
	 * Email exists.
	 *
	 * @param email
	 *            the email
	 * @return true, if successful
	 * @throws UserDataException
	 *             the user data exception
	 */
	public boolean emailExists(String email) throws UserDataException;

	public User getById(String email, Boolean obfuscate, Boolean minified)
			throws UserDataException;

	public User createUserFromFBProfile(com.restfb.types.User user,
			String fbToken);

	public User getByFBId(String fbId) throws UserDataException;

	public User getByEmail(String email) throws UserDataException,
			BuckBuddyException;

	public User getByEmail(String email, Boolean obfuscate)
			throws UserDataException, BuckBuddyException;

	public User getByUserSlug(String userSlug) throws UserDataException;

	public User getByUserSlug(String userSlug, Boolean obfuscate, Boolean minified) throws UserDataException;

	public Map<String, Object> create(User user, Boolean withPassword)
			throws UserDataException;

	public Map<String, Object> activate(Map<String, Object> userMap)
			throws UserDataException;

	public Map<String, Object> deActivate(Map<String, Object> userMap)
			throws UserDataException;

	public Map<String, Object> updateUserMapWithPaymentProfile(
			Map<String, Object> userMap, JsonNode paymentStripeProfileNode);

	public JsonNode getVerificationFieldsNeededForId(String userId)
			throws UserDataException;
	
	public Boolean isTransfersEnabled(String userId)
			throws UserDataException;

	public Map<String, Object> updateUserMapWithBalanceResponse(
			Map<String, Object> userMap, JsonNode banlanceResponseNode);
}
