package com.buckbuddy.core.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {

	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	public static String slugify(List<String> listOfInputStrings) {
		StringBuilder input = new StringBuilder();
		for (String inputString : listOfInputStrings) {
			input.append(inputString + " ");
		}
		String nowhitespace = WHITESPACE.matcher(input.toString().trim())
				.replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		return slug.toLowerCase(Locale.ENGLISH);
	}

	public static void main(String[] args) {
		OffsetDateTime now = OffsetDateTime.now();
		List<String> listOfStrings = new ArrayList<>();
		listOfStrings.add("reach4reddy@gmail.com");
		listOfStrings.add(String.valueOf(now.toEpochSecond()));
		listOfStrings.add("test this out!");
		listOfStrings.add(String.valueOf(now.toEpochSecond()));

		System.out.println(StringUtils.slugify(listOfStrings));
	}
}
