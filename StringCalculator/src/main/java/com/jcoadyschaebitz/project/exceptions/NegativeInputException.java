package main.java.com.jcoadyschaebitz.project.exceptions;

@SuppressWarnings("serial")
public class NegativeInputException extends Exception {

	public NegativeInputException() {
		super();
	}
	
	public NegativeInputException(String message) {
		super(message);
	}
	
}
