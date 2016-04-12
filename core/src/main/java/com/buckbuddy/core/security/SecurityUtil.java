/**
 * 
 */
package com.buckbuddy.core.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.buckbuddy.core.exceptions.BuckBuddyException;

/**
 * @author jtandalai
 *
 */
public class SecurityUtil {

	public static final String SHA_256 = "SHA-256";
	public static final String MD5 = "MD5";

	/**
	 * 
	 */
	public SecurityUtil() {
		// TODO Auto-generated constructor stub
	}

	public static String encrypt(String message, String algorithm) throws BuckBuddyException {

		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

			return convertByteArrayToHexString(hashedBytes);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			throw new BuckBuddyException("Could not generate hash from String",
					ex);
		}
	}

	private static String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString(
					(arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString();
	}

	/**
	 * @param args
	 * @throws BuckBuddyException
	 */
	public static void main(String[] args) throws BuckBuddyException {
		System.out.println(SecurityUtil.encrypt(
				"bucky_ykqhoax_tester@tfbnw.net", MD5).equals(
				SecurityUtil.encrypt("bucky_ykqhoax_tester@tfbnw.net", MD5)));
		System.out.println(SecurityUtil.encrypt(
				"bucky_ykqhoax_tester@tfbnw.net", SHA_256).equals(
				SecurityUtil.encrypt("bucky_ykqhoax_tester@tfbnw.net", SHA_256)));
		System.out.println(!SecurityUtil.encrypt(
				"bucky_ykqhoax_tester@tfbnw.net", MD5).equals(
				SecurityUtil.encrypt("bucky_ykqhoax_tester@tfbnw.net", SHA_256)));
	}

}
