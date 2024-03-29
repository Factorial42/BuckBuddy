/**
 * 
 */
package com.buckbuddy.api.campaign.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author jtandalai
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class ProfilePic {

	private Long sequence;
	private String profilePicId;
	private String url;

	/**
	 * @return the sequence
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the profilePicID
	 */
	public String getProfilePicId() {
		return profilePicId;
	}

	/**
	 * @param profilePicID the profilePicID to set
	 */
	public void setProfilePicId(String profilePicId) {
		this.profilePicId = profilePicId;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((profilePicId == null) ? 0 : profilePicId.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		if (!(obj instanceof ProfilePic)) {
			return false;
		}
		ProfilePic other = (ProfilePic) obj;
		if (profilePicId == null) {
			if (other.profilePicId != null) {
				return false;
			}
		} else if (!profilePicId.equals(other.profilePicId)) {
			return false;
		}
		if (sequence == null) {
			if (other.sequence != null) {
				return false;
			}
		} else if (!sequence.equals(other.sequence)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProfilePic [sequence=" + sequence + ", profilePicID="
				+ profilePicId + ", url=" + url + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
