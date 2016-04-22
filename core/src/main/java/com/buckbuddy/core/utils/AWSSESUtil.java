package com.buckbuddy.core.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class AWSSESUtil.
 */
public class AWSSESUtil {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AWSSESUtil.class);

	/** The Constant SMTP_USERNAME. */
	/*
	 * Supply your SMTP credentials below. Note that your SMTP credentials are
	 * different from your AWS credentials.
	 * 
	 * Replace with your SMTP username.
	 */
	static final String SMTP_USERNAME = "AKIAILTOSK7ALGHDG45A";

	/** The Constant SMTP_PASSWORD. */
	/*
	 * Replace with your SMTP password.
	 */
	static final String SMTP_PASSWORD = "AsRRdfJHyYB7wYfG4SRRyIPJtCjl3wZ3O3WdnUFNgUqF";

	/** The Constant HOST. */
	/*
	 * Amazon SES SMTP host name. This example uses the US West (Oregon) region.
	 */
	static final String HOST = "email-smtp.us-west-2.amazonaws.com";

	/** The Constant PORT. */
	/*
	 * The port you will connect to on the Amazon SES SMTP endpoint. We are
	 * choosing port 25 because we will use STARTTLS to encrypt the connection.
	 */
	static final int PORT = 25;

	/*
	 * Replace with your "From" address. This address must be verified.
	 */

	/**
	 * Send mail.
	 *
	 * @param fromAddress
	 *            the from address
	 * @param toAddress
	 *            the to address
	 * @param subject
	 *            the subject
	 * @param body
	 *            the body
	 * @return 
	 * @throws Exception
	 *             the exception
	 */
	public static boolean sendMail(String fromAddress, String toAddress,
			String subject, String body) throws Exception {
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(fromAddress));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
				toAddress));
		msg.setSubject(subject);
		msg.setContent(body, "text/html");

		Transport transport = session.getTransport();
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Attempting to send an email through the Amazon SES SMTP interface...");
			}
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			transport.sendMessage(msg, msg.getAllRecipients());

			if (LOG.isDebugEnabled()) {
				LOG.debug("Email sent!");
			}
			return Boolean.TRUE;
		} catch (Exception e) {
			LOG.error(
					"The email  From:{}, To:{}, with Subject:{} and Body:{} was not sent.",
					fromAddress, toAddress, subject, body);
			LOG.error("Error message: ", e);
		} finally {
			transport.close();
		}
		return false;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		AWSSESUtil.sendMail("hi@buckbuddy.com", "jtandalai@gmail.com", "hi",
				"test");
	}
}
