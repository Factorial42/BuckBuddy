/**
 * 
 */
package com.buckbuddy.api.user.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author jtandalai
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stripe {

	private String name;
	private String accountId;
	private String country;
	private String accessToken;
	private Boolean liveMode;
	private String refreshToken;
	private String tokenType;
	private String stripePublishableKey;
	private String stripeSecretKey;
	private String stripeUserId;
	private String scope;
	private JsonNode createAccountResponse;


	/**
	 * 
	 */
	public Stripe() {
		// TODO Auto-generated constructor stub
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
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the liveMode
	 */
	public Boolean getLiveMode() {
		return liveMode;
	}

	/**
	 * @param liveMode the liveMode to set
	 */
	public void setLiveMode(Boolean liveMode) {
		this.liveMode = liveMode;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * @return the tokenType
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * @param tokenType the tokenType to set
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * @return the stripePublishableKey
	 */
	public String getStripePublishableKey() {
		return stripePublishableKey;
	}

	/**
	 * @param stripePublishableKey the stripePublishableKey to set
	 */
	public void setStripePublishableKey(String stripePublishableKey) {
		this.stripePublishableKey = stripePublishableKey;
	}

	/**
	 * @return the stripeSecretKey
	 */
	public String getStripeSecretKey() {
		return stripeSecretKey;
	}

	/**
	 * @param stripeSecretKey the stripeSecretKey to set
	 */
	public void setStripeSecretKey(String stripeSecretKey) {
		this.stripeSecretKey = stripeSecretKey;
	}

	/**
	 * @return the stripeUserId
	 */
	public String getStripeUserId() {
		return stripeUserId;
	}

	/**
	 * @param stripeUserId the stripeUserId to set
	 */
	public void setStripeUserId(String stripeUserId) {
		this.stripeUserId = stripeUserId;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the createAccountResponse
	 */
	public JsonNode getCreateAccountResponse() {
		return createAccountResponse;
	}

	/**
	 * @param createAccountResponse the createAccountResponse to set
	 */
	public void setCreateAccountResponse(JsonNode createAccountResponse) {
		this.createAccountResponse = createAccountResponse;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Stripe [name=" + name + ", accountId=" + accountId
				+ ", country=" + country + ", accessToken=" + accessToken
				+ ", liveMode=" + liveMode + ", refreshToken=" + refreshToken
				+ ", tokenType=" + tokenType + ", stripePublishableKey="
				+ stripePublishableKey + ", stripeSecretKey=" + stripeSecretKey
				+ ", stripeUserId=" + stripeUserId + ", scope=" + scope
				+ ", createAccountResponse=" + createAccountResponse + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessToken == null) ? 0 : accessToken.hashCode());
		result = prime * result
				+ ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime
				* result
				+ ((createAccountResponse == null) ? 0 : createAccountResponse
						.hashCode());
		result = prime * result
				+ ((liveMode == null) ? 0 : liveMode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((refreshToken == null) ? 0 : refreshToken.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime
				* result
				+ ((stripePublishableKey == null) ? 0 : stripePublishableKey
						.hashCode());
		result = prime * result
				+ ((stripeSecretKey == null) ? 0 : stripeSecretKey.hashCode());
		result = prime * result
				+ ((stripeUserId == null) ? 0 : stripeUserId.hashCode());
		result = prime * result
				+ ((tokenType == null) ? 0 : tokenType.hashCode());
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
		if (!(obj instanceof Stripe)) {
			return false;
		}
		Stripe other = (Stripe) obj;
		if (accessToken == null) {
			if (other.accessToken != null) {
				return false;
			}
		} else if (!accessToken.equals(other.accessToken)) {
			return false;
		}
		if (accountId == null) {
			if (other.accountId != null) {
				return false;
			}
		} else if (!accountId.equals(other.accountId)) {
			return false;
		}
		if (country == null) {
			if (other.country != null) {
				return false;
			}
		} else if (!country.equals(other.country)) {
			return false;
		}
		if (createAccountResponse == null) {
			if (other.createAccountResponse != null) {
				return false;
			}
		} else if (!createAccountResponse.equals(other.createAccountResponse)) {
			return false;
		}
		if (liveMode == null) {
			if (other.liveMode != null) {
				return false;
			}
		} else if (!liveMode.equals(other.liveMode)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (refreshToken == null) {
			if (other.refreshToken != null) {
				return false;
			}
		} else if (!refreshToken.equals(other.refreshToken)) {
			return false;
		}
		if (scope == null) {
			if (other.scope != null) {
				return false;
			}
		} else if (!scope.equals(other.scope)) {
			return false;
		}
		if (stripePublishableKey == null) {
			if (other.stripePublishableKey != null) {
				return false;
			}
		} else if (!stripePublishableKey.equals(other.stripePublishableKey)) {
			return false;
		}
		if (stripeSecretKey == null) {
			if (other.stripeSecretKey != null) {
				return false;
			}
		} else if (!stripeSecretKey.equals(other.stripeSecretKey)) {
			return false;
		}
		if (stripeUserId == null) {
			if (other.stripeUserId != null) {
				return false;
			}
		} else if (!stripeUserId.equals(other.stripeUserId)) {
			return false;
		}
		if (tokenType == null) {
			if (other.tokenType != null) {
				return false;
			}
		} else if (!tokenType.equals(other.tokenType)) {
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
