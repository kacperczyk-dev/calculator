import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

public class Calculator {

    private static Set<String> supportedTokens = Set.of("+", "-", "*", "/", "(", ")");

    public static Double process(String formula) {
        if(formula == null || formula.isBlank()) throw new IllegalArgumentException("Formula should not be null or blank");

        Queue<String> postfix = convertToPostfix(formula);


        return 0.0;
    }

    public static Queue<String> convertToPostfix(String infix) {
        infix = infix.trim();

        Stack<String> tokens = new Stack<>();
        Queue<String> postfix  = new ArrayDeque<>();

        Stream.of(infix.split(" ")).forEach(token -> {
            Double value = null;
            try {
                value = Double.parseDouble(token.trim());
            } catch (NumberFormatException e) {
                // do nothing
            }
            if (value == null && !supportedTokens.contains(token)) {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
            if (value != null) {
                postfix.add(token);
            } else {
                if(tokens.isEmpty()) {
                    if(")".equals(token)) throw new IllegalArgumentException("Invalid token, formula cannot start with a closing bracket");
                    tokens.push(token);
                } else {
                    String lastSymbol = tokens.peek();
                    if ((token.equals("+") || token.equals("-")) && ("*".equals(lastSymbol) || "/".equals(lastSymbol))) {
                        postfix.add(tokens.pop());
                        tokens.push(token);
                    } else if (token.equals(")")) {
                        if(!tokens.contains("(")) throw new IllegalArgumentException("Invalid formula, no opening bracket found");
                        while(!tokens.empty()) {
                            if(!"(".equals(postfix.peek())) {
                                postfix.add(tokens.pop());
                            }
                            tokens.pop();
                        }
                    } else {
                        tokens.push(token);
                    }
                }
            }
        });

        postfix.addAll(tokens);

        return postfix;
    }

}
