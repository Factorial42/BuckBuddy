/**
 * 
 */
package com.buckbuddy.api.campaign.data.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author jtandalai
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class Campaign {

	private String userId;
	private String campaignId;
	private AtomicInteger counter;
	private OffsetDateTime createdAt;
	private OffsetDateTime lastUpdatedAt;
	private String name;
	private String description;
	private String cause;
	private BigDecimal amount;
	private Currency currency;
	private String currencyString;
	private Boolean active;
	private List<ProfilePic> profilePics;

	private CampaignNetworks campaignNetworks;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the campaignId
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * @param campaignId
	 *            the campaignId to set
	 */
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * @return the createdAt
	 */
	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the lastUpdatedAt
	 */
	public OffsetDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	/**
	 * @param lastUpdatedAt
	 *            the lastUpdatedAt to set
	 */
	public void setLastUpdatedAt(OffsetDateTime lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	/**
	 * @return the counter
	 */
	public AtomicInteger getCounter() {
		return counter;
	}

	/**
	 * @param counter
	 *            the counter to set
	 */
	public void setCounter(AtomicInteger counter) {
		this.counter = counter;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the cause
	 */
	public String getCause() {
		return cause;
	}

	/**
	 * @param cause
	 *            the cause to set
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * @return the currencyString
	 */
	public String getCurrencyString() {
		return currencyString;
	}

	/**
	 * @param currencyString
	 *            the currencyString to set
	 */
	public void setCurrencyString(String currencyString) {
		this.currencyString = currencyString;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @return the profilePics
	 */
	public List<ProfilePic> getProfilePics() {
		return profilePics == null ? new ArrayList<ProfilePic>() : profilePics;
	}

	/**
	 * @param profilePics
	 *            the profilePics to set
	 */
	public void setProfilePics(List<ProfilePic> profilePics) {
		this.profilePics = profilePics;
	}

	public void addToProfilePics(ProfilePic profilePic) {
		if (profilePic != null && !profilePic.getUrl().isEmpty()) {
			this.profilePics.add(profilePic);
		}
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the campaignNetworks
	 */
	public CampaignNetworks getCampaignNetworks() {
		return campaignNetworks;
	}

	/**
	 * @param campaignNetworks
	 *            the campaignNetworks to set
	 */
	public void setCampaignNetworks(CampaignNetworks campaignNetworks) {
		this.campaignNetworks = campaignNetworks;
	}

	public Campaign() {
		// TODO Auto-generated constructor stub
	}

	private class CampaignNetworks {
		private String facebookHash;
		private String twitterHash;
		private String whatsappHash;
		private String smsHash;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((facebookHash == null) ? 0 : facebookHash.hashCode());
			result = prime * result
					+ ((smsHash == null) ? 0 : smsHash.hashCode());
			result = prime * result
					+ ((twitterHash == null) ? 0 : twitterHash.hashCode());
			result = prime * result
					+ ((whatsappHash == null) ? 0 : whatsappHash.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof CampaignNetworks)) {
				return false;
			}
			CampaignNetworks other = (CampaignNetworks) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (facebookHash == null) {
				if (other.facebookHash != null) {
					return false;
				}
			} else if (!facebookHash.equals(other.facebookHash)) {
				return false;
			}
			if (smsHash == null) {
				if (other.smsHash != null) {
					return false;
				}
			} else if (!smsHash.equals(other.smsHash)) {
				return false;
			}
			if (twitterHash == null) {
				if (other.twitterHash != null) {
					return false;
				}
			} else if (!twitterHash.equals(other.twitterHash)) {
				return false;
			}
			if (whatsappHash == null) {
				if (other.whatsappHash != null) {
					return false;
				}
			} else if (!whatsappHash.equals(other.whatsappHash)) {
				return false;
			}
			return true;
		}

		private Campaign getOuterType() {
			return Campaign.this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CampaignNetworks [facebookHash=" + facebookHash
					+ ", twitterHash=" + twitterHash + ", whatsappHash="
					+ whatsappHash + ", smsHash=" + smsHash + "]";
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
