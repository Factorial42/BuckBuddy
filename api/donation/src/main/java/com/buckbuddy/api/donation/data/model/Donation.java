package com.buckbuddy.api.donation.data.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Donation {

	private String donationId;
	private OffsetDateTime createdAt;
	private OffsetDateTime lastUpdatedAt;
	private String ip;
	private BigDecimal amountInCents;
	private Currency currency;
	private String currencyString;
	private BigDecimal applicationFeeCollected;
	private String paymentToken; // token generated in front end by Stripe util
	private String affiliateId;
	
	// campaignUser
	private String campaignUserId;
	private String campaignUserSlug;
	private String campaignUserName;
	private String campaignId;
	private String campaignSlug;
	private String campaignName;

	private String donorName;
	private String donorUserToken;
	private String donorUserId;
	private String donorUserSlug;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String description;
	private String country;
	private String facebookHandle;
	private String twitterHandle;
	private String whatsappHandle;
	private String smsHandle;
	private JsonNode chargeUserResponse;
	private String donorProfilePic;
	private Boolean thankable;
	private Boolean thanked;

	
	/**
	 * @return the donationId
	 */
	public String getDonationId() {
		return donationId;
	}
	/**
	 * @param donationId the donationId to set
	 */
	public void setDonationId(String donationId) {
		this.donationId = donationId;
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
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmountInCents() {
		return amountInCents;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmountInCents(BigDecimal amountInCents) {
		this.amountInCents = amountInCents;
	}
	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
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
	 * @param currencyString the currencyString to set
	 */
	public void setCurrencyString(String currencyString) {
		this.currencyString = currencyString;
	}
	
	/**
	 * @return the applicationFeeCollected
	 */
	public BigDecimal getApplicationFeeCollected() {
		return applicationFeeCollected;
	}
	/**
	 * @param applicationFeeCollected the applicationFeeCollected to set
	 */
	public void setApplicationFeeCollected(BigDecimal applicationFeeCollected) {
		this.applicationFeeCollected = applicationFeeCollected;
	}
	/**
	 * @return the paymentToken
	 */
	public String getPaymentToken() {
		return paymentToken;
	}
	/**
	 * @param paymentToken the paymentToken to set
	 */
	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}
	/**
	 * @return the donorName
	 */
	public String getDonorName() {
		return donorName;
	}
	/**
	 * @param donorName the donorName to set
	 */
	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}
	/**
	 * @return the donorUserToken
	 */
	public String getDonorUserToken() {
		return donorUserToken;
	}
	/**
	 * @param donorUserToken the donorUserToken to set
	 */
	public void setDonorUserToken(String donorUserToken) {
		this.donorUserToken = donorUserToken;
	}
	/**
	 * @return the affiliateId
	 */
	public String getAffiliateId() {
		return affiliateId;
	}
	/**
	 * @param affiliateId the affiliateId to set
	 */
	public void setAffiliateId(String affiliateId) {
		this.affiliateId = affiliateId;
	}
	/**
	 * @return the campaignName
	 */
	public String getCampaignName() {
		return campaignName;
	}
	/**
	 * @param campaignName the campaignName to set
	 */
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
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
	 * @return the name
	 */
	public String getName() {
		return donorName;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.donorName = name;
	}
	/**
	 * @return the campaignUserId
	 */
	public String getCampaignUserId() {
		return campaignUserId;
	}
	/**
	 * @param campaignUserId the campaignUserId to set
	 */
	public void setCampaignUserId(String campaignUserId) {
		this.campaignUserId = campaignUserId;
	}
	/**
	 * @return the campaignUserSlug
	 */
	public String getCampaignUserSlug() {
		return campaignUserSlug;
	}
	/**
	 * @param campaignUserSlug the campaignUserSlug to set
	 */
	public void setCampaignUserSlug(String campaignUserSlug) {
		this.campaignUserSlug = campaignUserSlug;
	}
	/**
	 * @return the campaignUserName
	 */
	public String getCampaignUserName() {
		return campaignUserName;
	}
	/**
	 * @param campaignUserName the campaignUserName to set
	 */
	public void setCampaignUserName(String campaignUserName) {
		this.campaignUserName = campaignUserName;
	}
	/**
	 * @return the donorUserId
	 */
	public String getDonorUserId() {
		return donorUserId;
	}
	/**
	 * @param donorUserId the donorUserId to set
	 */
	public void setDonorUserId(String donorUserId) {
		this.donorUserId = donorUserId;
	}
	/**
	 * @return the donorUserSlug
	 */
	public String getDonorUserSlug() {
		return donorUserSlug;
	}
	/**
	 * @param donorUserSlug the donorUserSlug to set
	 */
	public void setDonorUserSlug(String donorUserSlug) {
		this.donorUserSlug = donorUserSlug;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the facebookHandle
	 */
	public String getFacebookHandle() {
		return facebookHandle;
	}
	/**
	 * @param facebookHandle the facebookHandle to set
	 */
	public void setFacebookHandle(String facebookHandle) {
		this.facebookHandle = facebookHandle;
	}
	/**
	 * @return the twitterHandle
	 */
	public String getTwitterHandle() {
		return twitterHandle;
	}
	/**
	 * @param twitterHandle the twitterHandle to set
	 */
	public void setTwitterHandle(String twitterHandle) {
		this.twitterHandle = twitterHandle;
	}
	/**
	 * @return the whatsappHandle
	 */
	public String getWhatsappHandle() {
		return whatsappHandle;
	}
	/**
	 * @param whatsappHandle the whatsappHandle to set
	 */
	public void setWhatsappHandle(String whatsappHandle) {
		this.whatsappHandle = whatsappHandle;
	}
	/**
	 * @return the smsHandle
	 */
	public String getSmsHandle() {
		return smsHandle;
	}
	/**
	 * @param smsHandle the smsHandle to set
	 */
	public void setSmsHandle(String smsHandle) {
		this.smsHandle = smsHandle;
	}

	/**
	 * @return the chargeUserResponse
	 */
	public JsonNode getChargeUserResponse() {
		return chargeUserResponse;
	}

	/**
	 * @param chargeUserResponse the chargeUserResponse to set
	 */
	public void setChargeUserResponse(JsonNode chargeUserResponse) {
		this.chargeUserResponse = chargeUserResponse;
	}
	
	/**
	 * @return the profilePic
	 */
	public String getDonorProfilePic() {
		return donorProfilePic;
	}
	/**
	 * @param profilePic the profilePic to set
	 */
	public void setDonorProfilePic(String donorProfilePic) {
		this.donorProfilePic = donorProfilePic;
	}
	/**
	 * @return the thankable
	 */
	public Boolean getThankable() {
		return thankable;
	}
	/**
	 * @param thankable the thankable to set
	 */
	public void setThankable(Boolean thankable) {
		this.thankable = thankable;
	}
	/**
	 * @return the thanked
	 */
	public Boolean getThanked() {
		return thanked;
	}
	/**
	 * @param thanked the thanked to set
	 */
	public void setThanked(Boolean thanked) {
		this.thanked = thanked;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Donation [donationId=" + donationId + ", createdAt="
				+ createdAt + ", lastUpdatedAt=" + lastUpdatedAt + ", ip=" + ip
				+ ", amountInCents=" + amountInCents + ", currency=" + currency
				+ ", currencyString=" + currencyString
				+ ", applicationFeeCollected=" + applicationFeeCollected
				+ ", paymentToken=" + paymentToken + ", affiliateId="
				+ affiliateId + ", campaignUserId=" + campaignUserId
				+ ", campaignUserSlug=" + campaignUserSlug
				+ ", campaignUserName=" + campaignUserName + ", campaignId="
				+ campaignId + ", campaignSlug=" + campaignSlug
				+ ", campaignName=" + campaignName + ", donorName=" + donorName
				+ ", donorUserToken=" + donorUserToken + ", donorUserId="
				+ donorUserId + ", donorUserSlug=" + donorUserSlug
				+ ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", email=" + email
				+ ", description=" + description + ", country=" + country
				+ ", facebookHandle=" + facebookHandle + ", twitterHandle="
				+ twitterHandle + ", whatsappHandle=" + whatsappHandle
				+ ", smsHandle=" + smsHandle + ", chargeUserResponse="
				+ chargeUserResponse + ", donorProfilePic=" + donorProfilePic
				+ ", thankable=" + thankable + ", thanked=" + thanked + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((affiliateId == null) ? 0 : affiliateId.hashCode());
		result = prime * result
				+ ((amountInCents == null) ? 0 : amountInCents.hashCode());
		result = prime
				* result
				+ ((applicationFeeCollected == null) ? 0
						: applicationFeeCollected.hashCode());
		result = prime * result
				+ ((campaignId == null) ? 0 : campaignId.hashCode());
		result = prime * result
				+ ((campaignName == null) ? 0 : campaignName.hashCode());
		result = prime * result
				+ ((campaignSlug == null) ? 0 : campaignSlug.hashCode());
		result = prime * result
				+ ((campaignUserId == null) ? 0 : campaignUserId.hashCode());
		result = prime
				* result
				+ ((campaignUserName == null) ? 0 : campaignUserName.hashCode());
		result = prime
				* result
				+ ((campaignUserSlug == null) ? 0 : campaignUserSlug.hashCode());
		result = prime
				* result
				+ ((chargeUserResponse == null) ? 0 : chargeUserResponse
						.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
		result = prime * result
				+ ((currencyString == null) ? 0 : currencyString.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((donationId == null) ? 0 : donationId.hashCode());
		result = prime * result
				+ ((donorName == null) ? 0 : donorName.hashCode());
		result = prime * result
				+ ((donorProfilePic == null) ? 0 : donorProfilePic.hashCode());
		result = prime * result
				+ ((donorUserId == null) ? 0 : donorUserId.hashCode());
		result = prime * result
				+ ((donorUserSlug == null) ? 0 : donorUserSlug.hashCode());
		result = prime * result
				+ ((donorUserToken == null) ? 0 : donorUserToken.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((facebookHandle == null) ? 0 : facebookHandle.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((lastUpdatedAt == null) ? 0 : lastUpdatedAt.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result
				+ ((paymentToken == null) ? 0 : paymentToken.hashCode());
		result = prime * result
				+ ((smsHandle == null) ? 0 : smsHandle.hashCode());
		result = prime * result
				+ ((thankable == null) ? 0 : thankable.hashCode());
		result = prime * result + ((thanked == null) ? 0 : thanked.hashCode());
		result = prime * result
				+ ((twitterHandle == null) ? 0 : twitterHandle.hashCode());
		result = prime * result
				+ ((whatsappHandle == null) ? 0 : whatsappHandle.hashCode());
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
		if (!(obj instanceof Donation)) {
			return false;
		}
		Donation other = (Donation) obj;
		if (affiliateId == null) {
			if (other.affiliateId != null) {
				return false;
			}
		} else if (!affiliateId.equals(other.affiliateId)) {
			return false;
		}
		if (amountInCents == null) {
			if (other.amountInCents != null) {
				return false;
			}
		} else if (!amountInCents.equals(other.amountInCents)) {
			return false;
		}
		if (applicationFeeCollected == null) {
			if (other.applicationFeeCollected != null) {
				return false;
			}
		} else if (!applicationFeeCollected
				.equals(other.applicationFeeCollected)) {
			return false;
		}
		if (campaignId == null) {
			if (other.campaignId != null) {
				return false;
			}
		} else if (!campaignId.equals(other.campaignId)) {
			return false;
		}
		if (campaignName == null) {
			if (other.campaignName != null) {
				return false;
			}
		} else if (!campaignName.equals(other.campaignName)) {
			return false;
		}
		if (campaignSlug == null) {
			if (other.campaignSlug != null) {
				return false;
			}
		} else if (!campaignSlug.equals(other.campaignSlug)) {
			return false;
		}
		if (campaignUserId == null) {
			if (other.campaignUserId != null) {
				return false;
			}
		} else if (!campaignUserId.equals(other.campaignUserId)) {
			return false;
		}
		if (campaignUserName == null) {
			if (other.campaignUserName != null) {
				return false;
			}
		} else if (!campaignUserName.equals(other.campaignUserName)) {
			return false;
		}
		if (campaignUserSlug == null) {
			if (other.campaignUserSlug != null) {
				return false;
			}
		} else if (!campaignUserSlug.equals(other.campaignUserSlug)) {
			return false;
		}
		if (chargeUserResponse == null) {
			if (other.chargeUserResponse != null) {
				return false;
			}
		} else if (!chargeUserResponse.equals(other.chargeUserResponse)) {
			return false;
		}
		if (country == null) {
			if (other.country != null) {
				return false;
			}
		} else if (!country.equals(other.country)) {
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
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (donationId == null) {
			if (other.donationId != null) {
				return false;
			}
		} else if (!donationId.equals(other.donationId)) {
			return false;
		}
		if (donorName == null) {
			if (other.donorName != null) {
				return false;
			}
		} else if (!donorName.equals(other.donorName)) {
			return false;
		}
		if (donorProfilePic == null) {
			if (other.donorProfilePic != null) {
				return false;
			}
		} else if (!donorProfilePic.equals(other.donorProfilePic)) {
			return false;
		}
		if (donorUserId == null) {
			if (other.donorUserId != null) {
				return false;
			}
		} else if (!donorUserId.equals(other.donorUserId)) {
			return false;
		}
		if (donorUserSlug == null) {
			if (other.donorUserSlug != null) {
				return false;
			}
		} else if (!donorUserSlug.equals(other.donorUserSlug)) {
			return false;
		}
		if (donorUserToken == null) {
			if (other.donorUserToken != null) {
				return false;
			}
		} else if (!donorUserToken.equals(other.donorUserToken)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (facebookHandle == null) {
			if (other.facebookHandle != null) {
				return false;
			}
		} else if (!facebookHandle.equals(other.facebookHandle)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (ip == null) {
			if (other.ip != null) {
				return false;
			}
		} else if (!ip.equals(other.ip)) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		if (lastUpdatedAt == null) {
			if (other.lastUpdatedAt != null) {
				return false;
			}
		} else if (!lastUpdatedAt.equals(other.lastUpdatedAt)) {
			return false;
		}
		if (middleName == null) {
			if (other.middleName != null) {
				return false;
			}
		} else if (!middleName.equals(other.middleName)) {
			return false;
		}
		if (paymentToken == null) {
			if (other.paymentToken != null) {
				return false;
			}
		} else if (!paymentToken.equals(other.paymentToken)) {
			return false;
		}
		if (smsHandle == null) {
			if (other.smsHandle != null) {
				return false;
			}
		} else if (!smsHandle.equals(other.smsHandle)) {
			return false;
		}
		if (thankable == null) {
			if (other.thankable != null) {
				return false;
			}
		} else if (!thankable.equals(other.thankable)) {
			return false;
		}
		if (thanked == null) {
			if (other.thanked != null) {
				return false;
			}
		} else if (!thanked.equals(other.thanked)) {
			return false;
		}
		if (twitterHandle == null) {
			if (other.twitterHandle != null) {
				return false;
			}
		} else if (!twitterHandle.equals(other.twitterHandle)) {
			return false;
		}
		if (whatsappHandle == null) {
			if (other.whatsappHandle != null) {
				return false;
			}
		} else if (!whatsappHandle.equals(other.whatsappHandle)) {
			return false;
		}
		return true;
	}
}
