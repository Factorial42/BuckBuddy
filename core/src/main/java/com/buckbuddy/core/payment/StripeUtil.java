/**
 * 
 */
package com.buckbuddy.core.payment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author jtandalai
 *
 */
public class StripeUtil {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(StripeUtil.class);

	/** The Constant mapper. */
	private static final ObjectMapper mapper = new ObjectMapper();

	private static String CLIENT_ID = "ca_8C41c5wmL93aWac9uMLX2ERpxx1VxL1V";
	private static String SECRET_KEY = "sk_test_mqfrrKZmIENX5zodeSV5VJAk";
	private static String TOKEN_URL = "https://connect.stripe.com/oauth/token";
	private static String AUTHORIZATION_CODE = "authorization_code";
	private static String REFRESH_TOKEN = "refresh_token";

	public static JsonNode getAccessToken(String code) {
		JsonNode node = null;
		String response = connectToStripe(code, AUTHORIZATION_CODE);
		try {
			return mapper.readTree(response);
		} catch (IOException e) {
			LOG.error("Error in parsing Stripe response");
			return null;
		}
	}

	public static JsonNode getRefreshToken(String refreshToken) {
		JsonNode node;
		String response = connectToStripe(refreshToken, REFRESH_TOKEN);
		try {
			return mapper.readTree(response);
		} catch (IOException e) {
			LOG.error("Error in parsing Stripe response");
			return null;
		}
	}

	private static String connectToStripe(String token, String grantType) {
		try {
			String params = null;
			URL url = new URL(TOKEN_URL);
			HttpURLConnection httpCon;
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("POST");
			httpCon.setRequestProperty("client_secret", SECRET_KEY);
			if (grantType.equals(AUTHORIZATION_CODE)) {
				params = "client_secret=" + SECRET_KEY + "&code=" + token
						+ "&grant_type=" + AUTHORIZATION_CODE;
			} else if (grantType.equals(REFRESH_TOKEN)) {
				params = "client_secret=" + SECRET_KEY + "&" + REFRESH_TOKEN
						+ "=" + token + "&grant_type=" + REFRESH_TOKEN;
			}
			DataOutputStream out = new DataOutputStream(
					httpCon.getOutputStream());

			out.writeBytes(params);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpCon.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			out.close();
			return response.toString();
		} catch (MalformedURLException e) {
			LOG.error("Malformed Token url {}", TOKEN_URL);
		} catch (IOException e) {
			LOG.error("Error in reading from POST Token call response");
		}
		return null;
	}

	/**
	 * 
	 */
	public StripeUtil() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JsonNode response = StripeUtil
				.getAccessToken("ac_8HFPW2Yf5bkfX8EKega1pp2vZn1sOVki");
		String refreshToken = response.findPath("refresh_token").asText();
		response = StripeUtil.getRefreshToken(refreshToken);
		System.out.println(response.toString());
	}

}
