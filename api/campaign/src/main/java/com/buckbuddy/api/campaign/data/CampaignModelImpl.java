/**
 * 
 */
package com.buckbuddy.api.campaign.data;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.api.campaign.data.model.Campaign;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

/**
 * @author jtandalai
 *
 */
public class CampaignModelImpl implements CampaignModel {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(CampaignModelImpl.class);

	/** The rethink db. */
	private static RethinkDB rethinkDB;

	/** The conn. */
	private static Connection conn;

	/** The Constant mapper. */
	private static final ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());

	/**
	 * 
	 */
	public CampaignModelImpl() {
		rethinkDB = RethinkDB.r;
		conn = rethinkDB.connection().hostname("localhost").port(28015)
				.db("buckbuddy").connect();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> create(Campaign campaign)
			throws CampaignDataException {

		campaign.setCampaignId(rethinkDB.uuid().run(conn));
		campaign.setCreatedAt(OffsetDateTime.now());
		campaign.setLastUpdatedAt(OffsetDateTime.now());

		Map<String, Object> campaignResponse;

		try {
			campaignResponse = rethinkDB.table("campaign")
					.insert(rethinkDB.expr(campaign)).run(conn);
			return campaignResponse;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> update(Campaign campaign)
			throws CampaignDataException {

		// add validation
		campaign.setLastUpdatedAt(OffsetDateTime.now());

		Map<String, Object> userResponse;
		try {
			userResponse = rethinkDB.table("campaign").get(campaign.getCampaignId())
					.replace(rethinkDB.expr(campaign)).run(conn);
			return userResponse;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> updatePartial(Map<String, Object> campaign)
			throws CampaignDataException {

		// add validation
		campaign.put("lastUpdatedAt", OffsetDateTime.now());

		Map<String, Object> campaignResponse;
		try {
			campaignResponse = rethinkDB.table("campaign")
					.get(campaign.get("campaignId"))
					.update(rethinkDB.expr(campaign)).run(conn);
			return campaignResponse;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> appendToProfilePics(String campaignId,
			String profilePic, Integer sequence) throws CampaignDataException {

		Map<String, Object> campaignResponse = null;
		try {
			// figure last updated time on this
			MapObject profilePicObject = rethinkDB.hashMap("url", profilePic)
                    .with("sequence",sequence);
			campaignResponse = rethinkDB
					.table("campaign")
					.get(campaignId)
					.update(row -> rethinkDB.hashMap("profilePics",
							row.g("profilePics").append(profilePicObject))

					).run(conn);
			return campaignResponse;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Campaign getById(String campaignId) throws CampaignDataException {
		Map<String, Object> campaignResponse;
		try {
			campaignResponse = rethinkDB.table("campaign").get(campaignId)
					.run(conn);

			return mapper.convertValue(campaignResponse, Campaign.class);
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Campaign getByUserId(String userId) throws CampaignDataException {
		Map<String, Object> campaignResponse = new HashMap<>();
		try {
			Cursor cursor = rethinkDB.table("campaign")
					.filter(rethinkDB.hashMap("userId", userId)).run(conn);

			if (cursor.hasNext()) {
				campaignResponse = (Map<String, Object>) cursor.next();
			}
			return mapper.convertValue(campaignResponse, Campaign.class);
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> delete(Campaign campaign)
			throws CampaignDataException {
		Map<String, Object> campaignResponse;
		try {
			campaignResponse = rethinkDB.table("campaign")
					.get(campaign.getCampaignId()).delete().run(conn);
			return campaignResponse;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> deleteById(String campaignId)
			throws CampaignDataException {
		Map<String, Object> campaignResponse;
		try {
			campaignResponse = rethinkDB.table("campaign").get(campaignId)
					.delete().run(conn);
			return campaignResponse;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

}
