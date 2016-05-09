package com.buckbuddy.api.donation.data;

import java.util.List;
import java.util.Map;

import com.buckbuddy.api.donation.data.model.Donation;

public interface DonationModel {

	public Map<String, Object> create(Map<String, Object> donationMap)
			throws DonationDataException;

	public List<Donation> getByCampaignSlugOrderByCreatedDatePaginated(
			String campaignSlug, Integer pageNumber, Integer pageSize,
			Boolean descending) throws DonationDataException;

	public Long countByCampaignSlug(String campaignSlug)
			throws DonationDataException;

	public Donation getById(String donationId) throws DonationDataException;

}
