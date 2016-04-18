/**
 * 
 */
package com.buckbuddy.api.user.data.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author jtandalai
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AffilicateProfile {
	
	private UUID affiliateId;

	/**
	 * 
	 */
	public AffilicateProfile() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the affiliateId
	 */
	public UUID getAffiliateId() {
		return affiliateId;
	}

	/**
	 * @param affiliateId the affiliateId to set
	 */
	public void setAffiliateId(UUID affiliateId) {
		this.affiliateId = affiliateId;
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
		if (!(obj instanceof AffilicateProfile)) {
			return false;
		}
		AffilicateProfile other = (AffilicateProfile) obj;
		if (affiliateId == null) {
			if (other.affiliateId != null) {
				return false;
			}
		} else if (!affiliateId.equals(other.affiliateId)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AffilicateProfile [affiliateId=" + affiliateId + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
