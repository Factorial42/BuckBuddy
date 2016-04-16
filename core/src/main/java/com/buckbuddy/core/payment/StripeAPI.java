/**
 * 
 */
package com.buckbuddy.core.payment;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * @author jtandalai
 *
 */
public class StripeAPI extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://connect.stripe.com/oauth/authorize?stripe_landing=login&response_type=code&client_id=ca_8C41c5wmL93aWac9uMLX2ERpxx1VxL1V&scope=read_write";

	protected StripeAPI() {
	}

	private static class InstanceHolder {

		private static final StripeAPI INSTANCE = new StripeAPI();
	}

	public static StripeAPI instance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.GET;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "https://connect.stripe.com/oauth/token";
	}

	@Override
	public String getRefreshTokenEndpoint() {
		return "https://connect.stripe.com/oauth/token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		Preconditions
				.checkValidUrl(config.getCallback(),
						"Must provide a valid url as callback. Stripe does not support OOB");
		final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL,
				config.getApiKey(), OAuthEncoder.encode(config.getCallback())));
		if (config.hasScope()) {
			sb.append('&').append(OAuthConstants.SCOPE).append('=')
					.append(OAuthEncoder.encode(config.getScope()));
		}

		final String state = config.getState();
		if (state != null) {
			sb.append('&').append(OAuthConstants.STATE).append('=')
					.append(OAuthEncoder.encode(state));
		}
		return sb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
