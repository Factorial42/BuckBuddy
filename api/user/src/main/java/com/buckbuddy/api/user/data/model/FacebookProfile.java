/**
 * 
 */
package com.buckbuddy.api.user.data.model;

/**
 * @author jtandalai
 *
 */
public class FacebookProfile {

	private String name;
	private String facebookID;
	private String pic;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String userAccessToken;
	private String appAccessToken;
	private String pageAccessToken;
	private String clientToken;

	/**
	 * 
	 */
	public FacebookProfile() {
		// TODO Auto-generated constructor stub
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
		this.name = name;
	}

	/**
	 * @return the facebookID
	 */
	public String getFacebookID() {
		return facebookID;
	}

	/**
	 * @param facebookID the facebookID to set
	 */
	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}

	/**
	 * @return the pic
	 */
	public String getPic() {
		return pic;
	}

	/**
	 * @param pic the pic to set
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserAccessToken() {
		return userAccessToken;
	}

	public void setUserAccessToken(String userAccessToken) {
		this.userAccessToken = userAccessToken;
	}

	public String getAppAccessToken() {
		return appAccessToken;
	}

	public void setAppAccessToken(String appAccessToken) {
		this.appAccessToken = appAccessToken;
	}

	public String getPageAccessToken() {
		return pageAccessToken;
	}

	public void setPageAccessToken(String pageAccessToken) {
		this.pageAccessToken = pageAccessToken;
	}

	public String getClientToken() {
		return clientToken;
	}

	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FacebookProfile [firstName=" + firstName + ", middleName="
				+ middleName + ", lastName=" + lastName + ", email=" + email
				+ ", userAccessToken=" + userAccessToken + ", appAccessToken="
				+ appAccessToken + ", pageAccessToken=" + pageAccessToken
				+ ", clientToken=" + clientToken + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appAccessToken == null) ? 0 : appAccessToken.hashCode());
		result = prime * result
				+ ((clientToken == null) ? 0 : clientToken.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result
				+ ((pageAccessToken == null) ? 0 : pageAccessToken.hashCode());
		result = prime * result
				+ ((userAccessToken == null) ? 0 : userAccessToken.hashCode());
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
		if (!(obj instanceof FacebookProfile)) {
			return false;
		}
		FacebookProfile other = (FacebookProfile) obj;
		if (appAccessToken == null) {
			if (other.appAccessToken != null) {
				return false;
			}
		} else if (!appAccessToken.equals(other.appAccessToken)) {
			return false;
		}
		if (clientToken == null) {
			if (other.clientToken != null) {
				return false;
			}
		} else if (!clientToken.equals(other.clientToken)) {
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
		if (middleName == null) {
			if (other.middleName != null) {
				return false;
			}
		} else if (!middleName.equals(other.middleName)) {
			return false;
		}
		if (pageAccessToken == null) {
			if (other.pageAccessToken != null) {
				return false;
			}
		} else if (!pageAccessToken.equals(other.pageAccessToken)) {
			return false;
		}
		if (userAccessToken == null) {
			if (other.userAccessToken != null) {
				return false;
			}
		} else if (!userAccessToken.equals(other.userAccessToken)) {
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
