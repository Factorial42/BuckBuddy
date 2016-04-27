/**
 * 
 */
package com.buckbuddy.api.user.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author jtandalai
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentProfiles {

	private Stripe stripe;

	/**
	 * 
	 */
	public PaymentProfiles() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the stripe
	 */
	public Stripe getStripe() {
		return stripe;
	}

	/**
	 * @param stripe
	 *            the stripe to set
	 */
	public void setStripe(Stripe stripe) {
		this.stripe = stripe;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stripe == null) ? 0 : stripe.hashCode());
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
		if (!(obj instanceof PaymentProfiles)) {
			return false;
		}
		PaymentProfiles other = (PaymentProfiles) obj;
		if (stripe == null) {
			if (other.stripe != null) {
				return false;
			}
		} else if (!stripe.equals(other.stripe)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PaymentProfile [stripe=" + stripe + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
