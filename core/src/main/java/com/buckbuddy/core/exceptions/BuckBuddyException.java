/**
 * 
 */
package com.buckbuddy.core.exceptions;

/**
 * @author jtandalai
 *
 */
public class BuckBuddyException extends Exception {	

	public final static String UNKNOWN = "unknown";

	public BuckBuddyException() {
		super();
	}

	public BuckBuddyException(String message) {
		super(message);
	}

	public BuckBuddyException(String message, Throwable cause) {
		super(message, cause);
	}

	public BuckBuddyException(Throwable cause) {
		super(cause);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
