package com.buckbuddy.api.campaign.data;

public class CampaignDataException extends Exception {
	public final static String UNKNOWN = "Unknown";
	public final static String DB_EXCEPTION = "Database Exception";

	public CampaignDataException() {
		super();
	}

	public CampaignDataException(String message) {
		super(message);
	}

	public CampaignDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public CampaignDataException(Throwable cause) {
		super(cause);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
