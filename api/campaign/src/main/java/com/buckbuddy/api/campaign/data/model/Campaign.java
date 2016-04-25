/**
 * 
 */
package com.buckbuddy.api.campaign.data.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author jtandalai
 *
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties
public class Campaign {

	private String userId;
	private String userSlug;
	private String campaignSlug;
	private String campaignId;
	private OffsetDateTime createdAt;
	private OffsetDateTime lastUpdatedAt;
	private OffsetDateTime startedAt;
	private OffsetDateTime endedAt;
	private String name;
	private String description;
	private String cause;
	private BigDecimal amount;
	private Currency currency;
	private String currencyString;
	private Long contributorsCount;
	private Boolean active;
	private Long days;
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
	 * @return the userSlug
	 */
	public String getUserSlug() {
		return userSlug;
	}

	/**
	 * @param userSlug
	 *            the userSlug to set
	 */
	public void setUserSlug(String userSlug) {
		this.userSlug = userSlug;
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
	 * @return the campaignSlug
	 */
	public String getCampaignSlug() {
		return campaignSlug;
	}

	/**
	 * @param campaignSlug
	 *            the campaignSlug to set
	 */
	public void setCampaignSlug(String campaignSlug) {
		this.campaignSlug = campaignSlug;
	}

	/**
	 * @return the startedAt
	 */
	public OffsetDateTime getStartedAt() {
		return startedAt;
	}

	/**
	 * @param startedAt
	 *            the startedAt to set
	 */
	public void setStartedAt(OffsetDateTime startedAt) {
		this.startedAt = startedAt;
	}

	/**
	 * @return the endedAt
	 */
	public OffsetDateTime getEndedAt() {
		return endedAt;
	}

	/**
	 * @param endedAt
	 *            the endedAt to set
	 */
	public void setEndedAt(OffsetDateTime endedAt) {
		this.endedAt = endedAt;
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
	 * @param active
	 *            the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the days
	 */
	public Long getDays() {
		return days;
	}

	/**
	 * @param days
	 *            the days to set
	 */
	public void setDays(Long days) {
		this.days = days;
	}

	/**
	 * @return the profilePics
	 */
	public List<ProfilePic> getProfilePics() {
		return profilePics;
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
	 * @return the contributorsCount
	 */
	public Long getContributorsCount() {
		return contributorsCount;
	}

	/**
	 * @param contributorsCount
	 *            the contributorsCount to set
	 */
	public void setContributorsCount(Long contributorsCount) {
		this.contributorsCount = contributorsCount;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Campaign [userId=" + userId + ", userSlug=" + userSlug
				+ ", campaignSlug=" + campaignSlug + ", campaignId="
				+ campaignId + ", createdAt=" + createdAt + ", lastUpdatedAt="
				+ lastUpdatedAt + ", startedAt=" + startedAt + ", endedAt="
				+ endedAt + ", name=" + name + ", description=" + description
				+ ", cause=" + cause + ", amount=" + amount + ", currency="
				+ currency + ", currencyString=" + currencyString
				+ ", contributorsCount=" + contributorsCount + ", active="
				+ active + ", days=" + days + ", profilePics=" + profilePics
				+ ", campaignNetworks=" + campaignNetworks + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((campaignId == null) ? 0 : campaignId.hashCode());
		result = prime
				* result
				+ ((campaignNetworks == null) ? 0 : campaignNetworks.hashCode());
		result = prime * result
				+ ((campaignSlug == null) ? 0 : campaignSlug.hashCode());
		result = prime * result + ((cause == null) ? 0 : cause.hashCode());
		result = prime
				* result
				+ ((contributorsCount == null) ? 0 : contributorsCount
						.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
		result = prime * result
				+ ((currencyString == null) ? 0 : currencyString.hashCode());
		result = prime * result + ((days == null) ? 0 : days.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((endedAt == null) ? 0 : endedAt.hashCode());
		result = prime * result
				+ ((lastUpdatedAt == null) ? 0 : lastUpdatedAt.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((profilePics == null) ? 0 : profilePics.hashCode());
		result = prime * result
				+ ((startedAt == null) ? 0 : startedAt.hashCode());
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
		if (!(obj instanceof Campaign)) {
			return false;
		}
		Campaign other = (Campaign) obj;
		if (active == null) {
			if (other.active != null) {
				return false;
			}
		} else if (!active.equals(other.active)) {
			return false;
		}
		if (amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!amount.equals(other.amount)) {
			return false;
		}
		if (campaignId == null) {
			if (other.campaignId != null) {
				return false;
			}
		} else if (!campaignId.equals(other.campaignId)) {
			return false;
		}
		if (campaignNetworks == null) {
			if (other.campaignNetworks != null) {
				return false;
			}
		} else if (!campaignNetworks.equals(other.campaignNetworks)) {
			return false;
		}
		if (campaignSlug == null) {
			if (other.campaignSlug != null) {
				return false;
			}
		} else if (!campaignSlug.equals(other.campaignSlug)) {
			return false;
		}
		if (cause == null) {
			if (other.cause != null) {
				return false;
			}
		} else if (!cause.equals(other.cause)) {
			return false;
		}
		if (contributorsCount == null) {
			if (other.contributorsCount != null) {
				return false;
			}
		} else if (!contributorsCount.equals(other.contributorsCount)) {
			return false;
		}
		if (createdAt == null) {
			if (other.createdAt != null) {
				return false;
			}
		} else if (!createdAt.equals(other.createdAt)) {
			return false;
		}
		if (currency == null) {
			if (other.currency != null) {
				return false;
			}
		} else if (!currency.equals(other.currency)) {
			return false;
		}
		if (currencyString == null) {
			if (other.currencyString != null) {
				return false;
			}
		} else if (!currencyString.equals(other.currencyString)) {
			return false;
		}
		if (days == null) {
			if (other.days != null) {
				return false;
			}
		} else if (!days.equals(other.days)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (endedAt == null) {
			if (other.endedAt != null) {
				return false;
			}
		} else if (!endedAt.equals(other.endedAt)) {
			return false;
		}
		if (lastUpdatedAt == null) {
			if (other.lastUpdatedAt != null) {
				return false;
			}
		} else if (!lastUpdatedAt.equals(other.lastUpdatedAt)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (profilePics == null) {
			if (other.profilePics != null) {
				return false;
			}
		} else if (!profilePics.equals(other.profilePics)) {
			return false;
		}
		if (startedAt == null) {
			if (other.startedAt != null) {
				return false;
			}
		} else if (!startedAt.equals(other.startedAt)) {
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
