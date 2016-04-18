/**
 * 
 */
package com.buckbuddy.core.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.security.Key;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.core.exceptions.BuckBuddyException;

/**
 * @author jtandalai
 *
 */
public class JJWTUtil {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(JJWTUtil.class);

	public final static String AUTHENTICATION_FAILURE = "failed auth";

	private static final String JJWT_SIGNING_ENCODED_KEY_STRING = "7mclOKr3n3+5noEVnHcqGTWAk7z3OP8G/1qDd3A9FNGIZAgWmLP6L5duyVKINKuzd8Dz0UVt5bQC5AwEMApBLA==";

	private static final Key JJWT_SIGNING_KEY = new SecretKeySpec(Base64
			.getDecoder().decode(JJWT_SIGNING_ENCODED_KEY_STRING), 0, Base64
			.getDecoder().decode(JJWT_SIGNING_ENCODED_KEY_STRING).length,
			"HmacSHA512");

	/**
	 * 
	 */
	public JJWTUtil() {
	}

	public static String issueToken(String id) throws BuckBuddyException {
		try {
			String token = Jwts.builder().setSubject(id)
					.signWith(SignatureAlgorithm.HS512, JJWT_SIGNING_KEY)
					.compact();
			return token;
		} catch (SignatureException e) {
			// don't trust the JWT!
			LOG.error(AUTHENTICATION_FAILURE, e);
			throw new BuckBuddyException(AUTHENTICATION_FAILURE, e);
		}
	}

	public static boolean authenticate(String id, String token) {
		try {
			return getSubject(token).equals(id);
		} catch (SignatureException e) {
			LOG.error(AUTHENTICATION_FAILURE, e);
			return false;
		}
	}

	public static String getSubject(String token) {
		return Jwts.parser().setSigningKey(JJWT_SIGNING_KEY)
				.parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String token = null;
		try {
			System.out.println(JJWTUtil.getSubject(""));
			token = JJWTUtil.issueToken("bucky_ykqhoax_tester@tfbnw.net");
			System.out.println("Token issued:" + token);

			// success test
			boolean authenticated = JJWTUtil.authenticate(
					"bucky_ykqhoax_tester@tfbnw.net", token);
			System.out.println("Authenticated:" + authenticated);

			// failure test
			authenticated = JJWTUtil.authenticate(
					"bucky_ykqhoax_tester@tfbnw.net", token.replaceFirst(token
							.substring(token.length() - 4, token.length() - 1),
							"test"));
			System.out.println("Authenticated:" + authenticated);
		} catch (BuckBuddyException e) {
			LOG.error("Error issuing token", e);
		}
	}
}
