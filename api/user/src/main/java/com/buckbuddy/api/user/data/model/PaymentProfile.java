/**
 * 
 */
package com.buckbuddy.api.user.data.model;

/**
 * @author jtandalai
 *
 */
public class PaymentProfile {

	private Stripe partner;

	/**
	 * 
	 */
	public PaymentProfile() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the partner
	 */
	public Stripe getPartner() {
		return partner;
	}

	/**
	 * @param partner
	 *            the partner to set
	 */
	public void setPartner(Stripe partner) {
		this.partner = partner;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((partner == null) ? 0 : partner.hashCode());
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
		if (!(obj instanceof PaymentProfile)) {
			return false;
		}
		PaymentProfile other = (PaymentProfile) obj;
		if (partner == null) {
			if (other.partner != null) {
				return false;
			}
		} else if (!partner.equals(other.partner)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PaymentProfile [partner=" + partner + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
