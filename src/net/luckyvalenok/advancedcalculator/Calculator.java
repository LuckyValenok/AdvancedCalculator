package net.luckyvalenok.advancedcalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class Calculator {
    
    private double start;
    private final double stop;
    private final double step;
    private final List<String> parsedExpression;
    
    public Calculator(double start, double stop, double step, String function) throws Exception {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.parsedExpression = parseExpression(function);
    }
    
    public Map<Double, String> getFilledMap() {
        Map<Double, String> doubleMap = new TreeMap<>();
        for (; start <= stop; start += step) {
            List<String> cloneParsedExpression = new ArrayList<>(parsedExpression);
            double finalStart = start;
            cloneParsedExpression.replaceAll(s -> {
                if (s.equals("x")) {
                    s = finalStart + "";
                }
                return s;
            });
            doubleMap.put(start, getResult(cloneParsedExpression));
        }
        return doubleMap;
    }
    
    public Stack<String> parseExpression(String expression) throws Exception {
        Stack<String> sb = new Stack<>();
        Stack<Operator> op = new Stack<>();
        
        char[] chars = expression.toCharArray();
        int length = chars.length;
        
        for (int i = 0; i < length; i++) {
            char ch = chars[i];
            
            if (ch == ' ' || ch == ',') {
                continue;
            }
            
            if (Character.isDigit(ch)) {
                StringBuilder digit = new StringBuilder();
                boolean hasPoint = false;
                while (i < length && (Character.isDigit(chars[i]) || (!hasPoint && chars[i] == '.'))) {
                    if (!hasPoint)
                        hasPoint = chars[i] == '.';
                    digit.append(chars[i++]);
                }
                i--;
                if (digit.toString().endsWith("."))
                    digit = new StringBuilder(digit.substring(0, digit.length() - 1));
                sb.push(digit.toString());
            } else if (ch == 'x') {
                sb.push("x");
            } else if (ch == '(') {
                op.push(Operator.OPENING_BRACKET);
            } else if (ch == ')') {
                while (op.peek() != Operator.OPENING_BRACKET) {
                    sb.push(op.pop().getSymbol() + "");
                }
                op.pop();
            } else {
                StringBuilder operation = new StringBuilder();
                while (i < length && (Character.isLetter(chars[i]) || Operator.getOperator(chars[i] + "") != null)) {
                    operation.append(chars[i++]);
                }
                i--;
                Operator operator = Operator.getOperator(operation.toString());
                if (operator != null) {
                    while (!op.isEmpty() && op.peek().getPriority() >= operator.getPriority()) {
                        sb.push(op.pop().getSymbol() + "");
                    }
                    op.push(operator);
                } else {
                    throw new Exception("Недопустимая операция " + operation);
                }
            }
        }
        
        while (!op.isEmpty()) {
            Operator operator = op.pop();
            if (operator == Operator.OPENING_BRACKET || operator == Operator.CLOSING_BRACKET) {
                throw new Exception("Слишком много закрывающихся скобок или мало открывающихся");
            }
            sb.push(operator.getSymbol() + "");
        }
        
        return sb;
    }
    
    private String getResult(List<String> blocks) {
        String stringResult;
        
        try {
            int index = 0;
            while (blocks.size() != 1) {
                String block = blocks.get(index);
                Operator operator = Operator.getOperator(block);
                if (operator != null) {
                    CountHelper countHelper = new CountHelper(blocks, index);
                    operator.getConsumer().accept(countHelper);
                    index = countHelper.getOffset();
                } else {
                    index++;
                }
            }
            stringResult = blocks.get(0);
        } catch (Exception exception) {
            stringResult = exception.getMessage();
        }
        
        return stringResult;
    }
}
