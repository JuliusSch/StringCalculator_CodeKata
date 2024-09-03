package main.java.com.jcoadyschaebitz.project;

import java.util.Scanner;

public class StringCalculatorApplication {

	public static void main(String[] args) {
		StringCalculator stringCalculator = new StringCalculator();
		Scanner scanner = new Scanner(System.in);
		stringCalculator.run(scanner);
	}
}
