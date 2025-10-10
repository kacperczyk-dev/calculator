import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

public class Calculator {

    private static final Set<String> supportedTokens = Set.of("+", "-", "*", "/", "(", ")");
    private static final Set<String> supportedOperators = Set.of("+", "-", "*", "/");

    private Calculator() {}

    public static Double process(String equation) {
        return processPostfix(convertToPostfix(equation));
    }

    private static int precedence(String operator) {
        if("(".equals(operator)) return -1;
        if("*".equals(operator) || "/".equals(operator)) return 2;
        if("+".equals(operator) || "-".equals(operator)) return 1;
        throw new IllegalArgumentException("Operator not supported: " + operator);
    }

    private static Queue<String> convertToPostfix(String equation) {
        if(equation == null || equation.isBlank()) throw new IllegalArgumentException("Equation should not be null or blank");
        equation = equation.trim();

        Stack<String> tokens = new Stack<>();
        Queue<String> postfix  = new ArrayDeque<>();
        String[] equationTokens = equation.split(" ");

        if(supportedOperators.contains(equationTokens[0]) || supportedOperators.contains(equationTokens[equationTokens.length - 1])) throw new IllegalArgumentException("Equation should not start or end with an operator");

        Boolean wasLastItemOperator = false;
        for (String token : Arrays.stream(equationTokens).toList()) {
            Double value = tryToParseDouble(token);
            if (value != null) {
                postfix.add(token);
                wasLastItemOperator = false;
            } else {
                if (!supportedTokens.contains(token)) throw new IllegalArgumentException("Invalid token: " + token);
                if (token.equals(")")) {
                    if (!tokens.contains("("))
                        throw new IllegalArgumentException("Invalid equation, no opening bracket found for closing bracket");
                    while (!tokens.empty()) {
                        String nextToken = tokens.pop();
                        if ("(".equals(nextToken)) {
                            break;
                        }
                        postfix.add(nextToken);
                    }
                } else if ("(".equals(token)) {
                    tokens.push(token);
                } else {
                    if(wasLastItemOperator) throw new IllegalArgumentException("Equation should not have two operators next to each other");
                    int lastTokenPrecedence = tokens.isEmpty() ? -1 : precedence(tokens.peek());
                    int currTokenPrecedence = precedence(token);
                    while (lastTokenPrecedence >= currTokenPrecedence) {
                        postfix.add(tokens.pop());
                        lastTokenPrecedence = tokens.isEmpty() ? -1 : precedence(tokens.peek());
                    }
                    tokens.push(token);
                    wasLastItemOperator = true;
                }
            }
        }
        if(tokens.contains("(") || tokens.contains(")")) throw new IllegalArgumentException("Unclosed brackets found in the equation: " + equation);
        while(!tokens.empty()) {
            postfix.add(tokens.pop());
        }
        return postfix;
    }

    private static Double processPostfix(Queue<String> postfix) {
        if(postfix == null || postfix.isEmpty()) throw new IllegalArgumentException("Postfix should not be null or blank.");
        Stack<Double> numbers = new Stack<>();
        postfix.forEach(token -> {
            Double number = tryToParseDouble(token);
            if(number == null) {
                Double result = switch (token) {
                    case "+" -> add(numbers.pop(), numbers.pop());
                    case "-" -> subtract(numbers.pop(), numbers.pop());
                    case "*" -> multiply(numbers.pop(), numbers.pop());
                    case "/" -> divide(numbers.pop(), numbers.pop());
                    default -> throw new IllegalArgumentException("Token not supported: " + token);
                };
                numbers.push(result);
            } else {
                numbers.push(number);
            }
        });
        if (numbers.empty() || numbers.size() > 1) throw new IllegalArgumentException("Postifx notation incorrect: " + postfix);
        return numbers.pop();
    }

    private static Double divide(Double second, Double first) {
        return first / second;
    }

    private static Double multiply(Double second, Double first) {
        return first * second;
    }

    private static Double subtract(Double second, Double first) {
        return first - second;
    }

    private static Double add(Double second, Double first) {
        return first + second;
    }

    private static Double tryToParseDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
