package com.buckbuddy.core.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class AmazonSESUtil {
	// Supply your SMTP credentials below. Note that your SMTP credentials are
	// different from your AWS credentials.
	static final String SMTP_USERNAME = "AKIAILTOSK7ALGHDG45A"; // Replace with
																// your SMTP
																// username.
	static final String SMTP_PASSWORD = "AsRRdfJHyYB7wYfG4SRRyIPJtCjl3wZ3O3WdnUFNgUqF"; // Replace
																						// with
																						// your
																						// SMTP
																						// password.
	// Amazon SES SMTP host name. This example uses the US West (Oregon) region.
	static final String HOST = "email-smtp.us-west-2.amazonaws.com";

	// The port you will connect to on the Amazon SES SMTP endpoint. We are
	// choosing port 25 because we will use
	// STARTTLS to encrypt the connection.
	static final int PORT = 25;

	static final String FROM = "hi@buckbuddy.com"; // Replace with your "From"
													// address. This address
													// must be verified.

	public static void sendMail(HashMap hm) throws Exception {

		// Create a Properties object to contain connection configuration
		// information.
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT);

		// Set properties indicating that we want to use STARTTLS to encrypt the
		// connection.
		// The SMTP session will begin on an unencrypted connection, and then
		// the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		String TO = (String) hm.get("to");
		// String TO = "reddy@buckbuddy.com"; // Replace with a "To"
		// address. If your account
		// is still in the
		// sandbox, this address
		// must be verified.

		String BODY = readHtmlFromTemplate(hm);
		String SUBJECT = "Welcome to Bucking!";

		// Create a Session object to represent a mail session with the
		// specified properties.
		Session session = Session.getDefaultInstance(props);

		// Create a message with the specified information.
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(FROM));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
		msg.setSubject(SUBJECT);
		msg.setContent(BODY, "text/html");

		// Create a transport.
		Transport transport = session.getTransport();

		// Send the message.
		try {
			System.out
					.println("Attempting to send an email through the Amazon SES SMTP interface...");

			// Connect to Amazon SES using the SMTP username and password you
			// specified above.
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
		} finally {
			// Close and terminate the connection.
			transport.close();
		}
	}

	public static String readHtmlFromTemplate(HashMap<String, String> hm) {
		String templateName = hm.get("templatename");
		System.out.println("Fetching template...." + templateName);
		String content = null;
		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"/Users/Reddy/Buckbuddy/Templates/" + templateName
							+ ".html"));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();

		} catch (IOException e) {
			System.out.println("Exception in reading the template");
		}

		// case of registration template
		if (templateName.equalsIgnoreCase("registration")) {
			content = (contentBuilder.toString().replaceAll("@username@",
					hm.get("username")).replaceAll("@activationlink@",
					hm.get("activationlink")));
		}
		System.out.println("content is " + content);
		return content;
	}

	public static void main(String[] args) throws Exception {

		// Basic usage test
		String toEmail = "reddy@buckbuddy.com";
		String userName = "Manjunath Reddy";
		String activationLink = "http://dev.buckbuddy.com";

		// case of emailing out registration template
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("templatename", "registration");
		hm.put("to", toEmail);
		hm.put("username", userName);
		hm.put("activationlink", activationLink);

		AmazonSESUtil.sendMail(hm);
	}
}
