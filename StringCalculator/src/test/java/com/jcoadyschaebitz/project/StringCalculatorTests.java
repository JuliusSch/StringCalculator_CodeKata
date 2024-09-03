package test.java.com.jcoadyschaebitz.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.jcoadyschaebitz.project.StringCalculator;

public class StringCalculatorTests {

    private StringCalculator stringCalculator;

    @BeforeEach
    public void setUp() {
        stringCalculator = new StringCalculator();
    }

    // add() Tests
    
    @Test
    public void testAdd_EmptyString_ReturnsZero() {
        int result = stringCalculator.add("");
        assertEquals(0, result);
    }

    @Test
    public void testAdd_SingleNumber_ReturnsTheNumber() {
        int result = stringCalculator.add("1");
        assertEquals(1, result);
    }

    @Test
    public void testAdd_TwoNumbersSeparatedByComma_ReturnsSum() {
        int result = stringCalculator.add("1,2");
        assertEquals(3, result);
    }

    @Test
    public void testAdd_MultipleNumbers_ReturnsSum() {
        int result = stringCalculator.add("1,2,3,4");
        assertEquals(10, result);
    }

    @Test
    public void testAdd_NumbersSeparatedByNewline_ReturnsSum() {
        int result = stringCalculator.add("1\n2,3");
        assertEquals(6, result);
    }

    @Test
    public void testAdd_InvalidNumber_HandlesExceptionAndReturnsPartialSum() {
        int result = stringCalculator.add("1,2,a");
        assertEquals(3, result);
    }
    
    @Test
    public void testAdd_IgnoresNumbersGreaterThan1000() {
        int result = stringCalculator.add("2,1001,6,2000,999,1000");
        assertEquals(2007, result);
    }

    // tryParse() Tests
    
    @Test
    public void testTryParse_EmptyString_ReturnsListWithZero() {
        List<Integer> result = stringCalculator.tryParse("");
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).intValue());
    }

    @Test
    public void testTryParse_MultipleNumbersSeparatedByCommaOrNewline_ParsesCorrectly() {
        List<Integer> result = stringCalculator.tryParse("1,2\n3");
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(2, result.get(1).intValue());
        assertEquals(3, result.get(2).intValue());
    }
    
    @Test
    public void testTryParse_WithCustomDelimiter_SingleCharacter_ParsesCorrectly() {
    	List<Integer> result = stringCalculator.tryParse("//[|]\n1|2|3");
        List<Integer> expectedResult = Arrays.asList(1, 2, 3);
        assertEquals(expectedResult, result);
    }
    
    public void testTryParse_WithCustomDelimiterAndManuallyTypedNewString_SingleCharacter_ParsesCorrectly() {    	
    	List<Integer> result = stringCalculator.tryParse("//[#]\\n1#2#3");
    	List<Integer> expectedResult = Arrays.asList(1, 2, 3);
    	assertEquals(expectedResult, result);
    }
    
    @Test
    public void testTryParse_WithCustomDelimiter_MultipleCharacters_ParsesCorrectly() {
    	List<Integer> result = stringCalculator.tryParse("//[***]\n1***2***3");
        List<Integer> expectedResult = Arrays.asList(1, 2, 3);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testTryParse_WithCustomDelimiterAndManuallyTypedNewString_MultipleCharacters_ParsesCorrectly() {
    	List<Integer> result = stringCalculator.tryParse("//[sos]\\n1sos2sos3");
        List<Integer> expectedResult = Arrays.asList(1, 2, 3);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testTryParse_WithNegativeNumbers_IdentifiesAndPrints() {
        PrintStream originalSysout = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String input = "1,-2,3,-4";
        
        List<Integer> result = stringCalculator.tryParse(input);
        List<Integer> expectedResult = Arrays.asList(1, 3);
        String expectedOutput = "Negatives not allowed: -2,-4";

        assertEquals(expectedResult, result);        
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(originalSysout);
    }

    @Test
    public void testTryParse_WithInvalidNumbers_IdentifiesAndPrints() {
        PrintStream originalSysout = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String input = "1,a,3,b";
        
        List<Integer> result = stringCalculator.tryParse(input);
        List<Integer> expectedResult = Arrays.asList(1, 3);
        String expectedOutput = "Some numbers could not be parsed: a,b";

        assertEquals(expectedResult, result);
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(originalSysout);
    }
    
    @Test
    public void testTryParse_WithMultipleCustomDelimiters_ParsesCorrectly() {
    	List<Integer> result = stringCalculator.tryParse("//[one][45][|][@]\\n4one12456|17@9");
        List<Integer> expectedResult = Arrays.asList(4, 12, 6, 17, 9);
        assertEquals(expectedResult, result);	
    }
}
