/**
 * 
 */
package com.buckbuddy.core.payment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buckbuddy.core.exceptions.BuckBuddyException;
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
import com.stripe.net.RequestOptions.RequestOptionsBuilder;

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

	private static BigDecimal STRIPE_BASE_FIXED_CHARGE_IN_CENTS = new BigDecimal(
			30);
	private static BigDecimal STRIPE_BASE_PERCENTAGE_CHARGE_X_100 = new BigDecimal(
			290);
	private static BigDecimal BUCKBUDDY_BASE_FIXED_CHARGE_IN_PERCENTAGE_X_100 = new BigDecimal(
			500);

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
	public static JsonNode createManagedAccount(String email,
			String businessUrl, Long tosAcceptanceDate, String tosAcceptanceIP)
			throws BuckBuddyException {

		Account managedAccount = null;
		Stripe.apiKey = SECRET_KEY;

		Map<String, Object> accountParams = new HashMap<String, Object>();
		accountParams.put("country", "US");
		accountParams.put("managed", true);
		accountParams.put("email", email);
		accountParams.put("business_url", businessUrl);
		if (tosAcceptanceDate != null && tosAcceptanceIP != null) {
			accountParams.put("tos_acceptance[date]", tosAcceptanceDate);
			accountParams.put("tos_acceptance[ip]", tosAcceptanceIP);
		}

		try {
			managedAccount = Account.create(accountParams);

			return mapper.convertValue(managedAccount, JsonNode.class);

		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Unable to create account for {}",
					accountParams.toString(), e);
			throw new BuckBuddyException("Could not charge user.", e);
		}
	}

	public static JsonNode retrieveBalance(String accountId) throws BuckBuddyException {
		/*
		 * curl https://api.stripe.com/v1/balance -u
		 * sk_test_EGU9Ur5y4V8vJIkFZPLa81xU: ​
		 */
		Stripe.apiKey = SECRET_KEY;
		try {
			RequestOptions options = new RequestOptionsBuilder()
					.setApiKey(Stripe.apiKey).setStripeAccount(accountId)
					.build();

			Balance balance = Balance.retrieve(options);
			return mapper.convertValue(balance, JsonNode.class);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Error in retrieving account balance for {}", accountId, e);
			throw new BuckBuddyException("Error in retrieving account balance  "+accountId, e);
		}
	}

	public static void acceptTOS(String connectedStripeAccountId,
			Long tosTimestamp, String tosIP) {
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

	public static JsonNode updateManagedAccountWithBankAccountAndLegalEntity(
			String connectedStripeAccountId, String bankAccountToken,
			String dobDay, String dobMonth, String dobYear, String firstName,
			String lastName, String type) throws BuckBuddyException {
		// external_account

		Stripe.apiKey = SECRET_KEY;
		Account account = null;
		try {
			account = Account.retrieve(connectedStripeAccountId, null);
			if (account != null) {
				Map<String, Object> accountParams = new HashMap<String, Object>();
				accountParams.put("external_account", bankAccountToken);

				Map<String, Object> dobParams = new HashMap<String, Object>();
				dobParams.put("day", dobDay);
				dobParams.put("month", dobMonth);
				dobParams.put("year", dobYear);

				Map<String, Object> legalEntitiesParams = new HashMap<String, Object>();
				legalEntitiesParams.put("dob", dobParams);
				legalEntitiesParams.put("first_name", firstName);
				legalEntitiesParams.put("last_name", lastName);
				legalEntitiesParams.put("type", type);

				accountParams.put("legal_entity", legalEntitiesParams);
				account= account.update(accountParams);
				return mapper.convertValue(account, JsonNode.class);
			}
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Error in updating account info", e);
			throw new BuckBuddyException("Error in updating account info.", e);
		}
		return null;
	}

	public static JsonNode transferToBankAccount(String connectedStripeAccountId,
			BigDecimal amountInCents, String currency) throws BuckBuddyException {
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
		transferParams.put("amount", amountInCents);
		transferParams.put("currency", currency);
		transferParams.put("destination", "default_for_currency");

		try {
			Transfer transferResponse = Transfer.create(transferParams, requestOptions);
			return mapper.convertValue(transferResponse, JsonNode.class);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOG.error("Error in transfer to account:{}",
					connectedStripeAccountId, e);
			throw new BuckBuddyException("Error in transfering to:"+connectedStripeAccountId, e);
		}
	}

	public static JsonNode chargeUser(String token, BigDecimal amountInCents,
			String currency, String description, String connectedAccountId)
			throws BuckBuddyException {
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
			BigDecimal applicationFeeInCents = computeApplicationFeeInCents(amountInCents);
			// if both amount and app fee are two decimals, amount to charge
			// should also end up in max two decimals
			BigDecimal amountToCharge = amountInCents
					.subtract(applicationFeeInCents);
			Map<String, Object> chargeParams = new HashMap<String, Object>();

			chargeParams.put("amount", amountInCents);
			chargeParams.put("currency", currency);
			chargeParams.put("source", token);
			chargeParams.put("description", description);
			chargeParams.put("destination", connectedAccountId);
			chargeParams.put("application_fee",
					applicationFeeInCents.toString());

			Charge charge = Charge.create(chargeParams);
			return mapper.convertValue(charge, JsonNode.class);
		} catch (CardException | AuthenticationException
				| InvalidRequestException | APIConnectionException
				| APIException e) {
			LOG.error("Could not charge user.", e);
			throw new BuckBuddyException("Could not charge user.", e);
		}
	}

	public static BigDecimal computeApplicationFeeInCents(
			BigDecimal amountInCents) {
		BigDecimal applicationFee = STRIPE_BASE_FIXED_CHARGE_IN_CENTS;
		BigDecimal percentageAdds = STRIPE_BASE_PERCENTAGE_CHARGE_X_100
				.add(BUCKBUDDY_BASE_FIXED_CHARGE_IN_PERCENTAGE_X_100);

		BigDecimal percentageOfAmountInCents = amountInCents
				.multiply(percentageAdds);
		percentageOfAmountInCents = percentageOfAmountInCents
				.divide(new BigDecimal(10000));
		applicationFee = applicationFee.add(percentageOfAmountInCents);
		return applicationFee.setScale(0, RoundingMode.HALF_UP);
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

		ccParams.put("number", "4000000000000077");
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
	 * @throws BuckBuddyException
	 */
	public static void main(String[] args) throws BuckBuddyException {
		// JsonNode response = StripeUtil
		// .getAccessToken("ac_8HFPW2Yf5bkfX8EKega1pp2vZn1sOVki");
		// String refreshToken = response.findPath("refresh_token").asText();
		// response = StripeUtil.getRefreshToken(refreshToken);
		// System.out.println(response.toString());

		StripeUtil.createManagedAccount("reddy@buckbuddy.com",
				"http://dev.buckbuddy.com/reddy/campaign1",
				(long) System.currentTimeMillis() / 1000L, "23.241.119.143");

		Token testToken = createTestCreditCardToken();
		StripeUtil.chargeUser(testToken.getId(), new BigDecimal(1000), "USD",
				"test charge", "acct_184xd4JL16Rlwnz1");

		StripeUtil.retrieveBalance("acct_184xd4JL16Rlwnz1");

		Token testBAToken = createTestBankAccountToken();
		updateManagedAccountWithBankAccountDetails("acct_183AFtIiad52S0yY",
				testBAToken.getId());
		testBAToken = createTestBankAccountToken();
		updateManagedAccountWithBankAccountAndLegalEntity(
				"acct_183AFtIiad52S0yY", testBAToken.getId(), "1", "1", "1973",
				"Test", "Buddy", "individual");

		System.out.println(computeApplicationFeeInCents(new BigDecimal(100)));
	}

}
