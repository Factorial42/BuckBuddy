/**
 * 
 */
package com.buckbuddy.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

/**
 * @author jtandalai
 *
 */
public class AWSS3Util {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AWSS3Util.class);

	private static AmazonS3 s3client = new AmazonS3Client(
			new ProfileCredentialsProvider());

	public static Boolean upload(String bucketName, String keyName,
			InputStream inputStream, String metadataString) {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			if (metadataString != null && !metadataString.isEmpty()) {
				metadata.setContentType(metadataString);
			}
			PutObjectResult result = s3client.putObject(new PutObjectRequest(
					bucketName, keyName, inputStream, metadata));
			return true;
		} catch (Exception e) {
			LOG.error("Error in uploading file to s3", e);
			return false;
		}
	}

	public static Boolean delete(String bucketName, String keyName) {
		try {
			s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
			return true;
		} catch (Exception e) {
			LOG.error("Error in deleting file bucket:{} key:{} to s3",
					bucketName, keyName, e);
			return false;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(
					"/Users/jtandalai/Downloads/test"));
		} catch (FileNotFoundException e) {
			LOG.error("File not found", e);
		}
		AWSS3Util.upload("user.assets.dev.buckbuddy.com", "testuser/testFile",
				inputStream, "image/png");
	}

}
