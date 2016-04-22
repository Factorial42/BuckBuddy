/**
 * 
 */
package com.buckbuddy.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

/**
 * @author jtandalai
 *
 */
public class TemplateUtil {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(TemplateUtil.class);
	
	public static String render(Map<String, String> model, String viewName) {
		MustacheTemplateEngine engine = new MustacheTemplateEngine();
		String response = engine.render(new ModelAndView(model, viewName));
		return response;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map model = new HashMap();
		model.put("username", "Hicky");
		model.put("activationlink", "http://www.google.com");
		System.out
				.println(TemplateUtil
						.render(model,
								"emails/registration.mustache"));

	}
}
