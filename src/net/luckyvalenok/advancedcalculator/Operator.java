package net.luckyvalenok.advancedcalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public enum Operator {
    
    OPENING_BRACKET("(", null, 0),
    CLOSING_BRACKET(")", null, 0),
    PLUS("+", helper -> {
        double[] doubles = helper.getDoubles(2);
        helper.add(helper.getOffset(), doubles[0] + doubles[1] + "");
    }, 1),
    MINUS("-", helper -> {
        double[] doubles = helper.getDoubles(2);
        helper.add(helper.getOffset(), doubles[0] - doubles[1] + "");
    }, 1),
    MULTIPLICATION("*", helper -> {
        double[] doubles = helper.getDoubles(2);
        helper.add(helper.getOffset(), doubles[0] * doubles[1] + "");
    }, 2),
    DIVISION("/", helper -> {
        double[] doubles = helper.getDoubles(2);
        helper.add(helper.getOffset(), doubles[0] / doubles[1] + "");
    }, 2),
    POW("^", helper -> {
        double[] doubles = helper.getDoubles(2);
        helper.add(helper.getOffset(), Math.pow(doubles[0], doubles[1]) + "");
    }, 3),
    SINUS("sin", helper -> {
        double[] doubles = helper.getDoubles(1);
        helper.add(helper.getOffset(), Math.sin(doubles[0]) + "");
    }, 3),
    FACTORIAL("!", helper -> {
        double[] doubles = helper.getDoubles(1);
        helper.add(helper.getOffset(), calculateFactorial(doubles[0]) + "");
    }, 3),
    COSINE("cos", helper -> {
        double[] doubles = helper.getDoubles(1);
        helper.add(helper.getOffset(), Math.cos(doubles[0]) + "");
    }, 3),
    ROUND("round", helper -> {
        double[] doubles = helper.getDoubles(1);
        helper.add(helper.getOffset(), Math.round(doubles[0]) + "");
    }, 3),
    LOGARITHM("log", helper -> {
        double[] doubles = helper.getDoubles(2);
        helper.add(helper.getOffset(), Math.log(doubles[0]) / Math.log(doubles[1]) + "");
    }, 3);
    
    private static final Map<String, Operator> operators = new HashMap<>();
    
    static {
        for (Operator operator : values()) {
            if (operator.getConsumer() != null) {
                operators.put(operator.getSymbol(), operator);
            }
        }
    }
    
    private final String symbol;
    private final Consumer<CountHelper> consumer;
    private final int priority;
    Operator(String symbol, Consumer<CountHelper> consumer, int priority) {
        this.symbol = symbol;
        this.consumer = consumer;
        this.priority = priority;
    }
    
    public static Operator getOperator(String symbol) {
        return operators.get(symbol);
    }
    
    private static double calculateFactorial(double i) {
        if (i <= 0)
            return 1;
        else
            return i * calculateFactorial(i - 1);
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public Consumer<CountHelper> getConsumer() {
        return consumer;
    }
    
    public int getPriority() {
        return priority;
    }
}
