/**
 * 
 */
package com.buckbuddy.api.donation.data;

import java.time.OffsetDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.api.donation.data.model.Donation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
