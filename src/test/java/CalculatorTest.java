import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CalculatorTest {

    private class TestCase {
        String input;
        Double expected;
        Exception error;

        public TestCase(String input, Double expected, Exception error) {
            this.input = input;
            this.expected = expected;
            this.error = error;
        }
    }

    @Test
    void processFormula() {
        List<TestCase> testCases = List.of(
                new TestCase("1 + 1", 2.0, null),
                new TestCase("1 - 1", 0.0, null),
                new TestCase("4 - 9", -5.0, null),
                new TestCase("3 + -9", -6.0, null),
                new TestCase("5 * 2", 10.0, null),
                new TestCase("17 / 3", 5.666666666666667, null),
                new TestCase("2 * ( 1 - 1 )", 0.0, null),
                new TestCase("( 7 * 1 ) - 1", 6.0, null),
                new TestCase("( 2 + 7 * 3 ) - 1", 22.0, null),
                new TestCase("( 7 - 1 ) * ( 1 / 2 )", 3.0, null),
                new TestCase("4 + 2 - ( 7 - 1 ) * 3 + 1 - 5 / ( 1 / 2 )", -21.0, null),
                new TestCase("( ) 4 - 1", 3.0, null),
                new TestCase("( ( 1 - 1)", null, new IllegalArgumentException("Invalid token: 1)")),
                new TestCase("( 1 - 1", null, new IllegalArgumentException("Unclosed brackets found in the equation: ( 1 - 1")),
                new TestCase("(1-1)", null, new IllegalArgumentException("Invalid token: (1-1)")),
                new TestCase("( 1-1 )", null, new IllegalArgumentException("Invalid token: 1-1")),
                new TestCase("( 1 - 1", null, new IllegalArgumentException("Unclosed brackets found in the equation: ( 1 - 1")),
                new TestCase("( 1 - 1 ) )", null, new IllegalArgumentException("Invalid equation, no opening bracket found for closing bracket")),
                new TestCase("( ( 1 - 1 ))", null, new IllegalArgumentException("Invalid token: ))"))
        );

        testCases.forEach(testCase -> {
            try {
                Double result = Calculator.process(testCase.input);
                assertEquals(testCase.expected, result);
            } catch (Exception e) {
                assertNotNull(testCase.error);
                assertEquals(testCase.error.getClass(), e.getClass());
                assertEquals(testCase.error.getMessage(), e.getMessage());
            }
        });
    }
}
