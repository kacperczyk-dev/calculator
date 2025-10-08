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

        Stack<String> symbols = new Stack<>();
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
                String lastSymbol = symbols.peek();
                if ((token.equals("+") || token.equals("-")) && (lastSymbol.equals("*") || lastSymbol.equals("/"))) {
                    postfix.add(symbols.pop());
                    symbols.push(token);
                } else if (token.equals(")")) {
                    if(!symbols.contains("(")) throw new IllegalArgumentException("Invalid formula, no opening bracket found");
                    while(!symbols.empty() && "(".equals(postfix.peek())) {
                        postfix.add(symbols.pop());
                    }
                } else {
                    symbols.push(token);
                }
            }
        });

        return postfix;
    }

}
