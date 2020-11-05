package net.luckyvalenok.tablefunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public enum Operator {
    
    OPENING_BRACKET("(", null, 0),
    CLOSING_BRACKET(")", null, 0),
    PLUS("+", doubles -> doubles.pop() + doubles.pop(), 1),
    MINUS("-", doubles -> -doubles.pop() + doubles.pop(), 1),
    MULTIPLICATION("*", doubles -> doubles.pop() * doubles.pop(), 2),
    DIVISION("/", doubles -> {
        double one = doubles.pop();
        double two = doubles.pop();
        return two / one;
    }, 2),
    POW("^", doubles -> Math.pow(doubles.pop(), doubles.pop()), 3),
    SINUS("sin", doubles -> Math.sin(doubles.pop()), 3),
    FACTORIAL("!", doubles -> calculateFactorial(doubles.pop()), 3),
    COSINE("cos", doubles -> Math.cos(doubles.pop()), 3),
    ROUND("round", doubles -> (double) Math.round(doubles.pop()), 3),
    LOGARITHM("log", doubles -> Math.log(doubles.pop()) / Math.log(doubles.pop()), 3);
    
    private static final Map<String, Operator> operators = new HashMap<>();
    
    static {
        for (Operator operator : values()) {
            if (operator.getFunction() != null) {
                operators.put(operator.getSymbol(), operator);
            }
        }
    }
    
    private final String symbol;
    private final Function<Stack<Double>, Double> function;
    private final int priority;
    
    Operator(String symbol, Function<Stack<Double>, Double> function, int priority) {
        this.symbol = symbol;
        this.function = function;
        this.priority = priority;
    }

    private static double calculateFactorial(double i) {
        if (i <= 0)
            return 1;
        else
            return i * calculateFactorial(i - 1);
    }
    
    public static Operator getOperator(String symbol) {
        return operators.get(symbol);
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public Function<Stack<Double>, Double> getFunction() {
        return function;
    }
    
    public int getPriority() {
        return priority;
    }
}
