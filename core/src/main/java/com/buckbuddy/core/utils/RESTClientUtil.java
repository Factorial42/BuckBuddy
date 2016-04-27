/**
 * 
 */
package com.buckbuddy.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jtandalai
 *
 */
public class RESTClientUtil {
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(RESTClientUtil.class);
	private static ObjectMapper mapper = new ObjectMapper();

	public static JsonNode sendGET(String getUrl, String userAgent)
			throws IOException {
		JsonNode jsonResponse = null;
		URL obj = new URL(getUrl);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", userAgent != null ? userAgent : "");
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			LOG.info(response.toString());
			if (response != null && !response.toString().isEmpty()) {
				jsonResponse = mapper.readTree(response.toString());
			}
		} else {
			LOG.error("Failed.");
		}
		return jsonResponse;
	}
	
	public static JsonNode sendPOST(String getUrl, String userAgent)
			throws IOException {
		JsonNode jsonResponse = null;
		URL obj = new URL(getUrl);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", userAgent != null ? userAgent : "");
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK
				|| responseCode == HttpURLConnection.HTTP_NO_CONTENT) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			LOG.info(response.toString());
			if (response != null && !response.toString().isEmpty()) {
				jsonResponse = mapper.readTree(response.toString());
			} else {
				jsonResponse = mapper.createObjectNode();
			}
			((ObjectNode) jsonResponse).put("responseCode",
					HttpURLConnection.HTTP_OK);
		} else {
			LOG.error("Failed.");
		}
		return jsonResponse;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LOG.info(RESTClientUtil
					.sendGET(
							"http://localhost:4567/users/ec67978322e5fbd7604e8db2ad0e89ab1fc4b7dee0a9a9655fb662b8ae647e71",
							null).toString());
		} catch (IOException e) {
			LOG.error("Error while calling GET method", e);
		}
	}

}
