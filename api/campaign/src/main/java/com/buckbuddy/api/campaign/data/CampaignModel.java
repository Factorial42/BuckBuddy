/**
 * 
 */
package com.buckbuddy.api.campaign.data;

import java.util.Map;

import com.buckbuddy.api.campaign.data.model.Campaign;

// TODO: Auto-generated Javadoc
/**
 * The Class CampaignModel.
 */
public interface CampaignModel {

	/**
	 * Creates the.
	 *
	 * @param campaign
	 *            the campaign
	 * @return the map
	 * @throws CampaignDataException
	 *             the campaign data exception
	 */
	public Map<String, Object> create(Campaign campaign)
			throws CampaignDataException;

	/**
	 * Update.
	 *
	 * @param campaign
	 *            the campaign
	 * @return the map
	 * @throws CampaignDataException
	 *             the campaign data exception
	 */
	public Map<String, Object> update(Campaign campaign)
			throws CampaignDataException;

	/**
	 * Update partial.
	 *
	 * @param campaign
	 *            the campaign
	 * @return the map
	 * @throws CampaignDataException
	 *             the campaign data exception
	 */
	public Map<String, Object> updatePartial(Map<String, Object> campaign)
			throws CampaignDataException;

	/**
	 * Gets the by id.
	 *
	 * @param campaignId
	 *            the campaign id
	 * @return the by id
	 * @throws CampaignDataException
	 *             the campaign data exception
	 */
	public Campaign getById(String campaignId) throws CampaignDataException;

	/**
	 * Delete.
	 *
	 * @param campaign
	 *            the campaign
	 * @return the map
	 * @throws CampaignDataException
	 *             the campaign data exception
	 */
	public Map<String, Object> delete(Campaign campaign)
			throws CampaignDataException;

	/**
	 * Delete by id.
	 *
	 * @param campaignId
	 *            the campaign id
	 * @return the map
	 * @throws CampaignDataException
	 *             the campaign data exception
	 */
	public Map<String, Object> deleteById(String campaignId)
			throws CampaignDataException;

	public Campaign getByUserId(String userId) throws CampaignDataException;

	public Map<String, Object> appendToProfilePics(String campaignId, String profilePic, Integer sequence)
			throws CampaignDataException;

}
