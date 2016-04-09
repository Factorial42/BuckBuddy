package com.buckbuddy.api.user.data.model;

import java.time.ZonedDateTime;

public class Token {

	private String accessToken;
	private ZonedDateTime expiresAt;
	
	public Token() {
		// TODO Auto-generated constructor stub
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public ZonedDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(ZonedDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Token [accessToken=" + accessToken + ", expiresAt=" + expiresAt
				+ "]";
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
				+ ((expiresAt == null) ? 0 : expiresAt.hashCode());
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
		if (!(obj instanceof Token)) {
			return false;
		}
		Token other = (Token) obj;
		if (accessToken == null) {
			if (other.accessToken != null) {
				return false;
			}
		} else if (!accessToken.equals(other.accessToken)) {
			return false;
		}
		if (expiresAt == null) {
			if (other.expiresAt != null) {
				return false;
			}
		} else if (!expiresAt.equals(other.expiresAt)) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		
	}
}
