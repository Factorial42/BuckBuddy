/**
 * 
 */
package com.buckbuddy.api.user.data.model;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * @author jtandalai
 *
 */
@JsonInclude(Include.NON_NULL)
public class User {

	private String userId;
	private String token;
	private Boolean authenticated;
	private OffsetDateTime createdAt;
	private OffsetDateTime lastUpdatedAt;
	private String name;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String password;
	private String country;
	private Currency currency;
	private String currencyString;
	private String s3handle;
	private String profilePic;
	private AffilicateProfile affiliateProfile;
	private PaymentProfiles paymentProfiles;
	private SocialProfiles socialProfiles;
	private Boolean active;

	/**
	 * 
	 */
	public User() {
		// TODO Auto-generated constructor stub
	}

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
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the authenticated
	 */
	public Boolean getAuthenticated() {
		return authenticated;
	}

	/**
	 * @param authenticated
	 *            the authenticated to set
	 */
	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}

	/**
	 * @return the createdAt
	 */
	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param offsetDateTime
	 *            the createdAt to set
	 */
	public void setCreatedAt(OffsetDateTime offsetDateTime) {
		this.createdAt = offsetDateTime;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		String[] nameArray = null;
		this.name = name;
		nameArray = name!=null?name.split(" "):new String[]{};
		if (nameArray.length >= 3) {
			this.setFirstName(nameArray[0]);
			this.setMiddleName(nameArray[1]);
			this.setLastName(nameArray[2]);
		}
		if (nameArray.length == 2) {
			this.setFirstName(nameArray[0]);
			this.setLastName(nameArray[1]);
		}
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
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
	 * @param middleName
	 *            the middleName to set
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
	 * @param lastName
	 *            the lastName to set
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
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email!=null?email.trim():email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password!=null?password.trim():password;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
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
	 * @return the s3handle
	 */
	public String getS3handle() {
		return s3handle;
	}

	/**
	 * @param s3handle
	 *            the s3handle to set
	 */
	public void setS3handle(String s3handle) {
		this.s3handle = s3handle;
	}

	/**
	 * @return the profilePic
	 */
	public String getProfilePic() {
		return profilePic;
	}

	/**
	 * @param profilePic
	 *            the profilePic to set
	 */
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	/**
	 * @return the affiliateProfile
	 */
	public AffilicateProfile getAffiliateProfile() {
		return affiliateProfile;
	}

	/**
	 * @param affiliateProfile
	 *            the affiliateProfile to set
	 */
	public void setAffiliateProfile(AffilicateProfile affiliateProfile) {
		this.affiliateProfile = affiliateProfile;
	}

	/**
	 * @return the paymentProfiles
	 */
	public PaymentProfiles getPaymentProfiles() {
		return paymentProfiles;
	}

	/**
	 * @param paymentProfiles
	 *            the paymentProfiles to set
	 */
	public void setPaymentProfiles(PaymentProfiles paymentProfiles) {
		this.paymentProfiles = paymentProfiles;
	}

	/**
	 * @return the socialProfiles
	 */
	public SocialProfiles getSocialProfiles() {
		return socialProfiles;
	}

	/**
	 * @param socialProfiles
	 *            the socialProfiles to set
	 */
	public void setSocialProfiles(SocialProfiles socialProfiles) {
		this.socialProfiles = socialProfiles;
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

	public static User obfuscate(User user) {
		user.setPassword(null);
		return user;
	}

	public static Map<String, Object> obfuscate(Map<String, Object> user) {
		user.remove("password");
		return user;
	}

	public static User createUserFromFBProfile(com.restfb.types.User fbUser,
			String fbToken) {
		User user = new User();
		SocialProfiles socialProfile = new SocialProfiles();
		FacebookProfile facebookProfile = new FacebookProfile();
		facebookProfile.setName(fbUser.getName());
		facebookProfile.setFacebookID(fbUser.getThirdPartyId());
		facebookProfile.setPic(fbUser.getPicture().getUrl());
		facebookProfile.setEmail(fbUser.getEmail());
		facebookProfile.setUserAccessToken(fbToken);
		socialProfile.setFacebookProfile(facebookProfile);
		user.setSocialProfiles(socialProfile);
		user.setName(fbUser.getName());
		user.setProfilePic(fbUser.getPicture().getUrl());
		
		user.setEmail(fbUser.getEmail());

		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((affiliateProfile == null) ? 0 : affiliateProfile.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result
				+ ((currencyString == null) ? 0 : currencyString.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((lastUpdatedAt == null) ? 0 : lastUpdatedAt.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((paymentProfiles == null) ? 0 : paymentProfiles.hashCode());
		result = prime * result
				+ ((profilePic == null) ? 0 : profilePic.hashCode());
		result = prime * result
				+ ((socialProfiles == null) ? 0 : socialProfiles.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (affiliateProfile == null) {
			if (other.affiliateProfile != null) {
				return false;
			}
		} else if (!affiliateProfile.equals(other.affiliateProfile)) {
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
		if (currencyString == null) {
			if (other.currencyString != null) {
				return false;
			}
		} else if (!currencyString.equals(other.currencyString)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
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
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (paymentProfiles == null) {
			if (other.paymentProfiles != null) {
				return false;
			}
		} else if (!paymentProfiles.equals(other.paymentProfiles)) {
			return false;
		}
		if (profilePic == null) {
			if (other.profilePic != null) {
				return false;
			}
		} else if (!profilePic.equals(other.profilePic)) {
			return false;
		}
		if (socialProfiles == null) {
			if (other.socialProfiles != null) {
				return false;
			}
		} else if (!socialProfiles.equals(other.socialProfiles)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId + ", createdAt=" + createdAt
				+ ", lastUpdatedAt=" + lastUpdatedAt + ", firstName="
				+ firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", email=" + email + ", password=" + password
				+ ", country=" + country + ", currency=" + currency
				+ ", currencyString=" + currencyString + ", profilePic="
				+ profilePic + ", affiliateProfile=" + affiliateProfile
				+ ", paymentProfiles=" + paymentProfiles + ", socialProfiles="
				+ socialProfiles + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
