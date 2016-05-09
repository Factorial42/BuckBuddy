/**
 * 
 */
package com.buckbuddy.api.donation.data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.api.donation.data.model.Donation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

/**
 * @author jtandalai
 *
 */
public class DonationModelImpl implements DonationModel {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(DonationModelImpl.class);

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
	public DonationModelImpl() {
		rethinkDB = RethinkDB.r;
		conn = rethinkDB.connection().hostname("localhost").port(28015)
				.db("buckbuddy").connect();
	}

	@Override
	public Map<String, Object> create(Map<String, Object> donationMap)
			throws DonationDataException {

		donationMap.put("donationId", rethinkDB.uuid().run(conn));
		donationMap.put("createdAt", OffsetDateTime.now());
		donationMap.put("lastUpdatedAt", donationMap.get("createdAt"));

		Map<String, Object> donationResponse;

		try {
			donationResponse = rethinkDB.table("donation")
					.insert(rethinkDB.expr(donationMap)).run(conn);

			if (donationResponse != null
					&& donationResponse.get("inserted") instanceof Long
					&& ((Long) donationResponse.get("inserted")) > 0) {
				LOG.debug("Successfully created donation record with id {}",
						donationResponse.get("donationId"));
			}
			return donationResponse;
		} catch (Exception e) {
			LOG.error(DonationDataException.DB_EXCEPTION, e);
			throw new DonationDataException(DonationDataException.DB_EXCEPTION);
		}
	}

	@Override
	public List<Donation> getByCampaignSlugOrderByCreatedDatePaginated(
			String campaignSlug, Integer pageNumber, Integer pageSize,
			Boolean descending) throws DonationDataException {

		try {
			List<Donation> donations = new ArrayList<>();
			Cursor cursor = null;
			cursor = rethinkDB
					.table("donation")
					.orderBy()
					.optArg("index",
							descending ? rethinkDB.desc("createdAt")
									: ("createdAt"))
					.filter(rethinkDB.hashMap("campaignSlug", campaignSlug))
					.slice(pageSize * (pageNumber - 1), pageSize * pageNumber)
					.run(conn);

			while (cursor.hasNext()) {
				donations
						.add(mapper.convertValue(cursor.next(), Donation.class));
			}
			return donations;
		} catch (Exception e) {
			LOG.error(DonationDataException.DB_EXCEPTION, e);
			throw new DonationDataException(DonationDataException.DB_EXCEPTION);
		}
	}

	@Override
	public Long countByCampaignSlug(String campaignSlug)
			throws DonationDataException {

		try {
			Object count = rethinkDB.table("donation")
					.filter(rethinkDB.hashMap("campaignSlug", campaignSlug))
					.count().run(conn);

			return (Long) count;
		} catch (Exception e) {
			LOG.error(DonationDataException.DB_EXCEPTION, e);
			throw new DonationDataException(DonationDataException.DB_EXCEPTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.buckbuddy.api.user.data.UserModel#getById(java.lang.String,
	 * java.lang.Boolean)
	 */
	@Override
	public Donation getById(String donationId) throws DonationDataException {
		Map<String, Object> donationResponse;
		try {
			donationResponse = rethinkDB.table("donation").get(donationId)
					.run(conn);
			return mapper.convertValue(donationResponse, Donation.class);
		} catch (Exception e) {
			LOG.error(DonationDataException.DB_EXCEPTION, e);
			throw new DonationDataException(DonationDataException.DB_EXCEPTION);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
