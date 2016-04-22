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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Account;
import com.stripe.model.Balance;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import com.stripe.model.Transfer;
import com.stripe.net.RequestOptions;

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

	/*
	 * create managed account curl https://api.stripe.com/v1/accounts -u
	 * sk_test_mqfrrKZmIENX5zodeSV5VJAk -d managed=true -d country=US -d \
	 * email="reddy@buckbuddy.com" -d
	 * business_url="http://dev.buckbuddy.com/reddy/campaign1" -d
	 * tos_acceptance[date]=1461261330 \ -d tos_acceptance[ip]="23.241.119.143"
	 * ​
	 */
	public static void createManagedAccount(String email, String businessUrl,
			Long tosAcceptanceDate, String tosAcceptanceIP) {

		Account managedAccount = null;
		Stripe.apiKey = SECRET_KEY;

		Map<String, Object> accountParams = new HashMap<String, Object>();
		accountParams.put("country", "US");
		accountParams.put("managed", true);
		accountParams.put("email", email);
		accountParams.put("business_url", businessUrl);
		accountParams.put("tos_acceptance[date]", tosAcceptanceDate);
		accountParams.put("tos_acceptance[ip]", tosAcceptanceIP);

		try {
			managedAccount = Account.create(accountParams);
			System.out.println(managedAccount.toString());
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Unable to create account for {}",
					accountParams.toString(), e);
		}
	}

	public static void retrieveBalance(String accountId) {
		/*
		 * curl https://api.stripe.com/v1/balance -u
		 * sk_test_EGU9Ur5y4V8vJIkFZPLa81xU: ​
		 */
		Stripe.apiKey = SECRET_KEY;
		try {
			Balance balance = Balance.retrieve();
			System.out.println(balance.toString());
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Error in retrieving account balance for", e);
		}
	}

	public static void acceptTOS(String connectedStripeAccountId,
			long tosTimestamp, String tosIP) {
		Stripe.apiKey = SECRET_KEY;
		Account account = null;
		try {
			account = Account.retrieve(connectedStripeAccountId, null);

			if (account != null) {
				Map<String, Object> tosAcceptanceParams = new HashMap<String, Object>();
				tosAcceptanceParams.put("date", (long) tosTimestamp / 1000L);
				tosAcceptanceParams.put("ip", tosIP);

				Map<String, Object> accountParams = new HashMap<String, Object>();
				accountParams.put("tos_acceptance", tosAcceptanceParams);

				account.update(accountParams);
			}

		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Error in retrieving account info", e);
		}
	}

	public static void updateManagedAccountWithBankAccountDetails(
			String connectedStripeAccountId, String bankAccountToken) {
		// external_account

		Stripe.apiKey = SECRET_KEY;
		Account account = null;
		try {
			account = Account.retrieve(connectedStripeAccountId, null);
			if (account != null) {
				Map<String, Object> accountParams = new HashMap<String, Object>();
				accountParams.put("external_account", bankAccountToken);
				account.update(accountParams);
			}
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Error in retrieving account info", e);
		}
	}

	public static void updateManagedAccountWithLegalEntity(
			String connectedStripeAccountId, String dobDay, String dobMonth,
			String dobYear, String firstName, String lastName, String type) {
		// external_account

		Stripe.apiKey = SECRET_KEY;
		Account account = null;
		try {
			account = Account.retrieve(connectedStripeAccountId, null);
			if (account != null) {
				Map<String, Object> dobParams = new HashMap<String, Object>();
				dobParams.put("day", dobDay);
				dobParams.put("month", dobMonth);
				dobParams.put("year", dobYear);

				Map<String, Object> legalEntitiesParams = new HashMap<String, Object>();
				legalEntitiesParams.put("dob", dobParams);
				legalEntitiesParams.put("first_name", firstName);
				legalEntitiesParams.put("last_name", lastName);
				legalEntitiesParams.put("type", type);

				Map<String, Object> accountParams = new HashMap<String, Object>();
				accountParams.put("legal_entity", legalEntitiesParams);
				account.update(accountParams);
			}
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Error in retrieving account info", e);
		}
	}

	public static void transferToBankAccount(String connectedStripeAccountId,
			String amount, String currency) {
		/*
		 * 
		 * transfer from managed account to bank account curl
		 * https://api.stripe.com/v1/transfers -u
		 * sk_test_EGU9Ur5y4V8vJIkFZPLa81xU: -d amount=40 -d currency=usd -d
		 * destination=acct_182oi5KEyKtkoESH -d
		 * description="Transfer to Users account to his bank account"
		 */
		Stripe.apiKey = SECRET_KEY;
		RequestOptions requestOptions = RequestOptions.builder()
				.setStripeAccount(connectedStripeAccountId).build();

		Map<String, Object> transferParams = new HashMap<String, Object>();
		transferParams.put("amount", amount);
		transferParams.put("currency", currency);
		transferParams.put("destination", "default_for_currency");

		try {
			Transfer.create(transferParams, requestOptions);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Error in transfer to account:{}",
					connectedStripeAccountId, e);
		}
	}

	public static void chargeUser(String token, String amount, String currency,
			String description, String connectedAccountId, String applicationFee) {
		/*
		 * //Charge a user curl https://api.stripe.com/v1/charges -u
		 * _test_EGU9Ur5y4V8vJIkFZPLa81xU: \ -d amount=40 -d currency=usd -d
		 * source=tok_17y5vOHngV6Dzl2INmBE80KD -d
		 * description="Charge a customer for a managed account"
		 */
		// Set your secret key: remember to change this to your live secret key
		// in production
		// See your keys here https://dashboard.stripe.com/account/apikeys

		Stripe.apiKey = SECRET_KEY;

		// Create the charge on Stripe's servers - this will charge the user's
		// card
		try {
			Map<String, Object> chargeParams = new HashMap<String, Object>();
			chargeParams.put("amount", amount); // amount in cents, again
			chargeParams.put("currency", currency);
			chargeParams.put("source", token);
			chargeParams.put("description", description);
			chargeParams.put("destination", connectedAccountId);
			chargeParams.put("application_fee", applicationFee);

			Charge charge = Charge.create(chargeParams);
			System.out.println(charge.toString());
		} catch (CardException | AuthenticationException
				| InvalidRequestException | APIConnectionException
				| APIException e) {
			LOG.error("Could not charge user.", e);
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

	private static Token createTestCreditCardToken() {
		Stripe.apiKey = SECRET_KEY;
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> ccParams = new HashMap<>();

		ccParams.put("number", "4242424242424242");
		ccParams.put("exp_month", 1);
		ccParams.put("exp_year", 2017);
		ccParams.put("cvc", 314);
		params.put("card", ccParams);

		try {
			Token token = Token.create(params);
			return token;
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Unable to create Token for 4242424242424242");
		}
		return null;
	}

	private static Token createTestBankAccountToken() {
		Stripe.apiKey = SECRET_KEY;
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> baParams = new HashMap<>();

		baParams.put("routing_number", "110000000");
		baParams.put("country", "US");
		baParams.put("account_number", "000123456789");
		params.put("bank_account", baParams);

		try {
			Token token = Token.create(params);
			return token;
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error(
					"Unable to create Token for Routing:000123456789 and Account number:000123456789",
					e);
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// JsonNode response = StripeUtil
		// .getAccessToken("ac_8HFPW2Yf5bkfX8EKega1pp2vZn1sOVki");
		// String refreshToken = response.findPath("refresh_token").asText();
		// response = StripeUtil.getRefreshToken(refreshToken);
		// System.out.println(response.toString());

		StripeUtil.createManagedAccount("reddy@buckbuddy.com",
				"http://dev.buckbuddy.com/reddy/campaign1",
				(long) System.currentTimeMillis() / 1000L, "23.241.119.143");

		Token testToken = createTestCreditCardToken();
		StripeUtil.chargeUser(testToken.getId(), "100", "USD", "test charge",
				"acct_183AFtIiad52S0yY", "20");
		StripeUtil.retrieveBalance("acct_183AFtIiad52S0yY");

		Token testBAToken = createTestBankAccountToken();
		updateManagedAccountWithBankAccountDetails("acct_183AFtIiad52S0yY",
				testBAToken.getId());

		updateManagedAccountWithLegalEntity("acct_183AFtIiad52S0yY", "1", "1",
				"1973", "Test", "Buddy", "individual");
	}

}
