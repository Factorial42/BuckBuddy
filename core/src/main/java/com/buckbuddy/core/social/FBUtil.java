/**
 * 
 */
package com.buckbuddy.core.social;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import com.restfb.types.User;

/**
 * @author jtandalai
 *
 */
public class FBUtil {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FBUtil.class);

	private static String apiKey = "1530924480549533";
	private static String apiSecret = "3fd3833bc39892ff31e9ede4e67d1160";
	private static String callback = "http://dev.buckbuddy.com/";
	private static String scope = "email,public_profile";
	private static List<String> profiles = Arrays.asList("me", "me/picture");

	// to handle OAuth with FB
	private static OAuth20Service OAUTH_SERVICE;

	static {
		OAUTH_SERVICE = new ServiceBuilder().apiKey(apiKey)
				.apiSecret(apiSecret).callback(callback).scope(scope)
				.build(FacebookApi.instance());
	}

	private String initiateOAuth() {
		return OAUTH_SERVICE.getAuthorizationUrl();
	}

	public static OAuth2AccessToken getAccessToken(String code) {
		return OAUTH_SERVICE.getAccessToken(code);
	}

	public static AccessToken extendToken(String fbToken) {
		FacebookClient facebookClient = new DefaultFacebookClient(fbToken,
				apiSecret, Version.LATEST);
		AccessToken extendedFBToken = facebookClient.obtainExtendedAccessToken(
				apiKey, apiSecret, fbToken);
		return extendedFBToken;
	}

	public static User getProfile(String fbToken) throws IOException {
		AccessToken extendedFBToken = extendToken(fbToken);
		FacebookClient facebookClient = new DefaultFacebookClient(
				extendedFBToken.getAccessToken(), apiSecret, Version.LATEST);
		User user = facebookClient.fetchObject("me", User.class);
		user = facebookClient.fetchObject(user.getId(), User.class, Parameter
				.with("fields", "name,birthday,email,picture,third_party_id"));

		JsonObject picture = facebookClient.fetchObject("me/picture",
				JsonObject.class, Parameter.with("width", "125"), // the image size
				Parameter.with("redirect", "false"));
		user.getPicture().setHeight(picture.getJsonObject("data").getInt("height"));
		user.getPicture().setWidth(picture.getJsonObject("data").getInt("width"));
		user.getPicture().setUrl(picture.getJsonObject("data").getString("url"));
		
		return user;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		FBUtil fbUtil = new FBUtil();
		fbUtil.testCompleteFlow();
	}

	private void testCompleteFlow() throws IOException {
		FBUtil fbUtil = new FBUtil();
		String NETWORK_NAME = "Facebook";
		String secretState = "secret" + new Random().nextInt(999999);
		final Scanner in = new Scanner(System.in, "UTF-8");

		System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
		System.out.println();
		System.out.println("Fetching the Authorization URL...");

		String authorizationUrl = fbUtil.initiateOAuth();

		System.out.println("Got the Authorization URL!");
		System.out.println("Now go and authorize ScribeJava here:");
		System.out.println(authorizationUrl);
		System.out.println("And paste the authorization code here");
		System.out.print(">>");
		final String code = in.nextLine();
		System.out.println();

		System.out
				.println("And paste the state from server here. We have set 'secretState'='"
						+ secretState + "'.");
		System.out.print(">>");
		final String value = in.nextLine();
		if (secretState.equals(value)) {
			System.out.println("State value does match!");
		} else {
			System.out.println("Ooops, state value does not match!");
			System.out.println("Expected = " + secretState);
			System.out.println("Got      = " + value);
			System.out.println();
		}
		OAuth2AccessToken accessToken = getAccessToken(code);
		System.out.println(getProfile(accessToken.getAccessToken()));
	}

}
