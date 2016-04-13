/**
 * 
 */
package com.buckbuddy.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jtandalai
 *
 */
@JsonInclude(Include.NON_NULL)
public class BuckBuddyResponse {

	private ObjectNode data;
	private ObjectNode error;
	
	/**
	 * @return the data
	 */
	public ObjectNode getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(ObjectNode data) {
		this.data = data;
	}

	/**
	 * @return the error
	 */
	public ObjectNode getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ObjectNode error) {
		this.error = error;
	}

	/**
	 * 
	 */
	public BuckBuddyResponse() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
