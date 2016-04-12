package com.buckbuddy.api.user.data;

public class UserDataException extends Exception {
	
	public final static String UNKNOWN = "unknown";	
	public final static String SECURITY_EXCEPTION = "Security Exception";
	public final static String DB_EXCEPTION = "Database Exception";
	public final static String DATA_PARSE_ERROR = "error parsing response from db";

	public UserDataException() {
		super();
	}

	public UserDataException(String message) {
		super(message);
	}

	public UserDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDataException(Throwable cause) {
		super(cause);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
