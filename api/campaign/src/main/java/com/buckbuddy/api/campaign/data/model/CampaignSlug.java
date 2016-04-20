/**
 * 
 */
package com.buckbuddy.api.campaign.data.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author jtandalai
 *
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignSlug {

	// primary key campaign slug is a combination of user slug + campaign slug
	private String campaignSlug;
	private String campaignId;
	private String userId;
	private String userSlug;
	private OffsetDateTime createdAt;
	private OffsetDateTime lastUpdatedAt;

	/**
	 * 
	 */
	public CampaignSlug() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the campaignSlug
	 */
	public String getCampaignSlug() {
		return campaignSlug;
	}

	/**
	 * @param campaignSlug the campaignSlug to set
	 */
	public void setCampaignSlug(String campaignSlug) {
		this.campaignSlug = campaignSlug;
	}

	/**
	 * @return the campaignId
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userSlug
	 */
	public String getUserSlug() {
		return userSlug;
	}

	/**
	 * @param userSlug the userSlug to set
	 */
	public void setUserSlug(String userSlug) {
		this.userSlug = userSlug;
	}

	/**
	 * @return the createdAt
	 */
	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
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
	 * @param lastUpdatedAt the lastUpdatedAt to set
	 */
	public void setLastUpdatedAt(OffsetDateTime lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((campaignId == null) ? 0 : campaignId.hashCode());
		result = prime * result
				+ ((campaignSlug == null) ? 0 : campaignSlug.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result
				+ ((lastUpdatedAt == null) ? 0 : lastUpdatedAt.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((userSlug == null) ? 0 : userSlug.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		if (!(obj instanceof CampaignSlug)) {
			return false;
		}
		CampaignSlug other = (CampaignSlug) obj;
		if (campaignId == null) {
			if (other.campaignId != null) {
				return false;
			}
		} else if (!campaignId.equals(other.campaignId)) {
			return false;
		}
		if (campaignSlug == null) {
			if (other.campaignSlug != null) {
				return false;
			}
		} else if (!campaignSlug.equals(other.campaignSlug)) {
			return false;
		}
		if (createdAt == null) {
			if (other.createdAt != null) {
				return false;
			}
		} else if (!createdAt.equals(other.createdAt)) {
			return false;
		}
		if (lastUpdatedAt == null) {
			if (other.lastUpdatedAt != null) {
				return false;
			}
		} else if (!lastUpdatedAt.equals(other.lastUpdatedAt)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		if (userSlug == null) {
			if (other.userSlug != null) {
				return false;
			}
		} else if (!userSlug.equals(other.userSlug)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CampaignSlug [campaignSlug=" + campaignSlug + ", campaignId="
				+ campaignId + ", userId=" + userId + ", userSlug=" + userSlug
				+ ", createdAt=" + createdAt + ", lastUpdatedAt="
				+ lastUpdatedAt + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
