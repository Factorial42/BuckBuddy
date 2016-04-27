package com.buckbuddy.api.donation.data;

public class DonationDataException extends Exception {
	public final static String UNKNOWN = "Unknown";
	public final static String DB_EXCEPTION = "Database Exception";

	public DonationDataException() {
		super();
	}

	public DonationDataException(String message) {
		super(message);
	}

	public DonationDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DonationDataException(Throwable cause) {
		super(cause);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
