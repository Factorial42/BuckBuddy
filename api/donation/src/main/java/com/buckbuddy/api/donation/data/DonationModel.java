package com.buckbuddy.api.donation.data;

import java.util.Map;

public interface DonationModel {

	Map<String, Object> create(Map<String, Object> donationMap) throws DonationDataException;

}
