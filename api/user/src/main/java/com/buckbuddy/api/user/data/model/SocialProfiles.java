/**
 * 
 */
package com.buckbuddy.api.user.data.model;

/**
 * @author jtandalai
 *
 */
public class SocialProfiles {	

	private FacebookProfile facebookProfile;
	private TwitterProfile twitterProfile;
	private WhatsappProfile whatsappProfile;
	private SMSProfile smsProfile;

	/**
	 * 
	 */
	public SocialProfiles() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the facebookProfile
	 */
	public FacebookProfile getFacebookProfile() {
		return facebookProfile;
	}

	/**
	 * @param facebookProfile the facebookProfile to set
	 */
	public void setFacebookProfile(FacebookProfile facebookProfile) {
		this.facebookProfile = facebookProfile;
	}

	/**
	 * @return the twitterProfile
	 */
	public TwitterProfile getTwitterProfile() {
		return twitterProfile;
	}

	/**
	 * @param twitterProfile the twitterProfile to set
	 */
	public void setTwitterProfile(TwitterProfile twitterProfile) {
		this.twitterProfile = twitterProfile;
	}

	/**
	 * @return the whatsappProfile
	 */
	public WhatsappProfile getWhatsappProfile() {
		return whatsappProfile;
	}

	/**
	 * @param whatsappProfile the whatsappProfile to set
	 */
	public void setWhatsappProfile(WhatsappProfile whatsappProfile) {
		this.whatsappProfile = whatsappProfile;
	}

	/**
	 * @return the smsProfile
	 */
	public SMSProfile getSmsProfile() {
		return smsProfile;
	}

	/**
	 * @param smsProfile the smsProfile to set
	 */
	public void setSmsProfile(SMSProfile smsProfile) {
		this.smsProfile = smsProfile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SocialProfiles [facebookProfile=" + facebookProfile
				+ ", twitterProfile=" + twitterProfile + ", whatsappProfile="
				+ whatsappProfile + ", smsProfile=" + smsProfile + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((facebookProfile == null) ? 0 : facebookProfile.hashCode());
		result = prime * result
				+ ((smsProfile == null) ? 0 : smsProfile.hashCode());
		result = prime * result
				+ ((twitterProfile == null) ? 0 : twitterProfile.hashCode());
		result = prime * result
				+ ((whatsappProfile == null) ? 0 : whatsappProfile.hashCode());
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
		if (!(obj instanceof SocialProfiles)) {
			return false;
		}
		SocialProfiles other = (SocialProfiles) obj;
		if (facebookProfile == null) {
			if (other.facebookProfile != null) {
				return false;
			}
		} else if (!facebookProfile.equals(other.facebookProfile)) {
			return false;
		}
		if (smsProfile == null) {
			if (other.smsProfile != null) {
				return false;
			}
		} else if (!smsProfile.equals(other.smsProfile)) {
			return false;
		}
		if (twitterProfile == null) {
			if (other.twitterProfile != null) {
				return false;
			}
		} else if (!twitterProfile.equals(other.twitterProfile)) {
			return false;
		}
		if (whatsappProfile == null) {
			if (other.whatsappProfile != null) {
				return false;
			}
		} else if (!whatsappProfile.equals(other.whatsappProfile)) {
			return false;
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
