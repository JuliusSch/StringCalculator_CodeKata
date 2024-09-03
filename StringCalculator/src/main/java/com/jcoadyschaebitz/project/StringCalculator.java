package main.java.com.jcoadyschaebitz.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import main.java.com.jcoadyschaebitz.project.exceptions.NegativeInputException;

public class StringCalculator {
	
	// Regex patterns
	
	public static Pattern NEGATIVE_NUMBER_PATTERN = Pattern.compile("^-\\d+$");
	public static Pattern CUSTOM_DELIMITER_SYNTAX = Pattern.compile("^//(.+?)(?:\\\\n|\\n)(.*)");
	public static Pattern MULTIPLE_CUSTOM_DELIMITERS_SYNTAX = Pattern.compile("^\\/\\/(\\[(.*?)\\](?:\\[\\])*)(?:\\\\n|\\n)(.*)");
	public static Pattern SEPARATE_ITEMS_IN_SQUARE_BRACKETS = Pattern.compile("\\[([^\\]]+)\\]");

	public static Pattern STANDARD_DELIMITER_SYNTAX = Pattern.compile("[,\\r?\\n|\\\\n]+"); 
	
	// The standard syntax checks for ',', '\n' and '\n' as a manually entered string of '\' and 'n'.
	
	// the '\' in '\n' needs to be escaped twice when entered manually as two characters, once for Java and
	// again for the regex engine, hence '\\\\n'.

	public void run(Scanner scanner) {
		boolean continueOperation = true;

		while (continueOperation) {
			System.out.println("Please select an operation (add):");
			String response = scanner.nextLine().toLowerCase();
			switch (response) {
			case "add":
				processAdd(scanner);
			default:
				break;
			}
		}

		scanner.close();
	}

	public List<Integer> tryParse(String response) {
		List<Integer> result = new ArrayList<Integer>();
		if (response.equals("")) {
			result.add(0);
			return result;
		}
		Matcher matcher = MULTIPLE_CUSTOM_DELIMITERS_SYNTAX.matcher(response);

		String delimiterRegex;
		if (matcher.find()) {
			response = matcher.group(3);
			delimiterRegex = constructCustomDelimitersPattern(matcher.group(1));
		} else {
			delimiterRegex = STANDARD_DELIMITER_SYNTAX.pattern();
		}
		var numberList = response.split(delimiterRegex);
		List<String> negativeNumbers = new ArrayList<String>();
		List<String> invalidNumbers = new ArrayList<String>();
		for (String number : numberList) {
			try {
				result.add(tryParseNumber(number));
			} catch (NumberFormatException e) {
				invalidNumbers.add(number);
			} catch (NegativeInputException e) {
				negativeNumbers.add(number);
			}
		}
		if (negativeNumbers.size() > 0)
			System.out.println("Negatives not allowed: " + negativeNumbers.stream().collect(Collectors.joining(",")));

		if (invalidNumbers.size() > 0)
			System.out.println("Some numbers could not be parsed: " + invalidNumbers.stream().collect(Collectors.joining(",")));

		return result;
	}
	
	private String constructCustomDelimitersPattern(String delimiters) {
		Matcher delimitersMatcher = SEPARATE_ITEMS_IN_SQUARE_BRACKETS.matcher(delimiters);
		StringBuilder regexBuilder = new StringBuilder();
		
		while (delimitersMatcher.find()) {
			String delimiter = delimitersMatcher.group(1);
			if (regexBuilder.length() > 0)
				regexBuilder.append('|');
			
			regexBuilder.append(Pattern.quote(delimiter));
		}
		return regexBuilder.toString();
	}
	
	public int tryParseNumber(String number) throws NumberFormatException, NegativeInputException {
		try {
			if (number.equals(""))
				return 0;
			if (NEGATIVE_NUMBER_PATTERN.matcher(number).matches())
				throw (new NegativeInputException());
			return Integer.parseInt(number);
		} catch (NumberFormatException | NegativeInputException e) {
			throw e;
		}
	}

	private void processAdd(Scanner scanner) {
		System.out.println(
				"Please enter a string to parse. Either numbers separated by a valid delimiter (, \\n),\nOr of the format '//[[custom delimiter]]\\n[sequence of numbers with new delimiter]:");
		String numberResponse = scanner.nextLine().toLowerCase();
		System.out.println("The answer is " + add(numberResponse));
	}

	public int add(String numbers) {
		int result = 0;
		var parsedNumbers = tryParse(numbers);
		for (Integer num : parsedNumbers) {
			if (num <= 1000)
				result += num;
		}
		return result;
	}
}
