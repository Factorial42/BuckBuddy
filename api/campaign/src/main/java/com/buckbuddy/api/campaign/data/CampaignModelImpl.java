/**
 * 
 */
package com.buckbuddy.api.campaign.data;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.api.campaign.data.model.Campaign;
import com.buckbuddy.api.campaign.data.model.CampaignSlug;
import com.buckbuddy.core.utils.StringUtils;
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
		campaign.setLastUpdatedAt(campaign.getCreatedAt());
		campaign.setContributorsCount(0L);

		if (campaign.getActive() == null) {
			campaign.setActive(Boolean.FALSE);
		}

		Map<String, Object> campaignResponse, campaignWithSlugResponse, campaignSlugResponse;
		;

		try {
			campaignResponse = rethinkDB.table("campaign")
					.insert(rethinkDB.expr(campaign)).run(conn);

			if (campaignResponse != null
					&& campaignResponse.get("inserted") instanceof Long
					&& ((Long) campaignResponse.get("inserted")) > 0) {
				// if campaign is saved successfully, create campaign slug
				CampaignSlug campaignSlug = new CampaignSlug();
				campaignSlug.setCreatedAt(campaign.getCreatedAt());
				campaignSlug.setLastUpdatedAt(campaign.getCreatedAt());
				campaignSlug.setCampaignId(campaign.getCampaignId());
				campaignSlug.setUserId(campaign.getUserId());
				campaignSlug.setUserSlug(campaign.getUserSlug());

				List<String> slugList = new ArrayList<>();
				slugList.add(campaign.getName());

				String campaignSlugString = null;
				boolean success = false;
				for (int i = 0; i < 5; i++) {
					// try a max of 5 times saving slug with current timestamp
					// if it's already taken
					slugList.add(String.valueOf(OffsetDateTime.now()
							.toEpochSecond()));
					campaignSlugString = StringUtils.slugify(slugList);
					campaignSlug.setCampaignSlug(campaignSlug.getUserSlug()
							+ "/" + campaignSlugString);
					campaignSlugResponse = rethinkDB.table("campaignSlug")
							.insert(rethinkDB.expr(campaignSlug)).run(conn);
					if (campaignSlugResponse != null
							&& campaignSlugResponse.get("inserted") instanceof Long
							&& ((Long) campaignSlugResponse.get("inserted")) > 0) {
						success = true;
						break;
					} else {
						slugList.remove(slugList.size() - 1);
					}
				}
				if (!success) {
					LOG.error("Could not create campaign slug. Too many conflicts. Try again later");
				} else {
					// save campaign object with slug info
					Map<String, Object> campaignWithSlug = new HashMap<>();
					campaignWithSlug.put("campaignSlug", campaignSlugString);
					campaignWithSlugResponse = rethinkDB.table("campaign")
							.get(campaign.getCampaignId())
							.update(rethinkDB.expr(campaignWithSlug)).run(conn);
					if (campaignWithSlugResponse == null
							&& campaignResponse.get("errors") instanceof Long
							&& ((Long) campaignResponse.get("errors")) > 0) {
						LOG.error(
								"Error while saving campaign object with slug info",
								campaignResponse.get("first_error"));
					}
				}
			}
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
			userResponse = rethinkDB.table("campaign")
					.get(campaign.getCampaignId())
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
	public Campaign getById(String campaignId) throws CampaignDataException {
		Map<String, Object> campaignResponse;
		try {
			campaignResponse = rethinkDB.table("campaign").get(campaignId)
					.run(conn);
			if (campaignResponse.get("startedAt") != null) {
				if (campaignResponse.get("endedAt") != null) {
					campaignResponse.put("days", ChronoUnit.DAYS.between(
							(OffsetDateTime) campaignResponse.get("startedAt"),
							(OffsetDateTime) campaignResponse.get("endedAt")));
				} else {
					campaignResponse.put("days", ChronoUnit.DAYS.between(
							(OffsetDateTime) campaignResponse.get("startedAt"),
							OffsetDateTime.now()));
				}
			} else {
				campaignResponse.put("days", 0);
			}
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
				if (campaignResponse.get("startedAt") != null) {
					if (campaignResponse.get("endedAt") != null) {
						campaignResponse.put("days", ChronoUnit.DAYS.between(
								(OffsetDateTime) campaignResponse
										.get("startedAt"),
								(OffsetDateTime) campaignResponse
										.get("endedAt")));
					} else {
						campaignResponse.put("days",
								ChronoUnit.DAYS.between(
										(OffsetDateTime) campaignResponse
												.get("startedAt"),
										OffsetDateTime.now()));
					}
				} else {
					campaignResponse.put("days", 0);
				}
			}
			return mapper.convertValue(campaignResponse, Campaign.class);
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Campaign getByCampaignSlug(String campaignSlug) throws CampaignDataException {
		Map<String, Object> campaignResponse = new HashMap<>();
		try {
			Cursor cursor = rethinkDB.table("campaign")
					.filter(rethinkDB.hashMap("campaignSlug", campaignSlug)).run(conn);

			if (cursor.hasNext()) {
				campaignResponse = (Map<String, Object>) cursor.next();
				if (campaignResponse.get("startedAt") != null) {
					if (campaignResponse.get("endedAt") != null) {
						campaignResponse.put("days", ChronoUnit.DAYS.between(
								(OffsetDateTime) campaignResponse
										.get("startedAt"),
								(OffsetDateTime) campaignResponse
										.get("endedAt")));
					} else {
						campaignResponse.put("days",
								ChronoUnit.DAYS.between(
										(OffsetDateTime) campaignResponse
												.get("startedAt"),
										OffsetDateTime.now()));
					}
				} else {
					campaignResponse.put("days", 0);
				}
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

	@Override
	public Boolean checkIfSlugExists(String userId, String campaignURLSlug)
			throws CampaignDataException {
		Map<String, Object> campaignResponse;
		try {
			campaignResponse = rethinkDB
					.table("campaign")
					.filter(rethinkDB.hashMap("userId", userId).with(
							"campaignURLSlug", campaignURLSlug)).run(conn);
			return true;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> activate(Map<String, Object> campaignMap)
			throws CampaignDataException {

		// add validation
		campaignMap.put("lastUpdatedAt", OffsetDateTime.now());
		campaignMap.put("startedAt", OffsetDateTime.now());
		campaignMap.put("active", Boolean.TRUE);

		Map<String, Object> campaignResponse;
		try {
			campaignResponse = rethinkDB.table("campaign")
					.get(campaignMap.get("campaignId"))
					.update(rethinkDB.expr(campaignMap)).run(conn);
			return campaignResponse;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Map<String, Object> deActivate(Map<String, Object> campaignMap)
			throws CampaignDataException {

		// add validation
		campaignMap.put("lastUpdatedAt", OffsetDateTime.now());
		campaignMap.put("endedAt", OffsetDateTime.now());
		campaignMap.put("active", Boolean.FALSE);
		campaignMap.put("days", ChronoUnit.DAYS.between(
				(OffsetDateTime) campaignMap.get("startedAt"),
				(OffsetDateTime) campaignMap.get("endedAt")));

		Map<String, Object> campaignResponse;
		try {
			campaignResponse = rethinkDB.table("campaign")
					.get(campaignMap.get("campaignId"))
					.update(rethinkDB.expr(campaignMap)).run(conn);
			return campaignResponse;
		} catch (Exception e) {
			LOG.error(CampaignDataException.DB_EXCEPTION, e);
			throw new CampaignDataException(CampaignDataException.DB_EXCEPTION);
		}
	}
}
