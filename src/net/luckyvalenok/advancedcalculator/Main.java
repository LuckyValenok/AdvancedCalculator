package net.luckyvalenok.advancedcalculator;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

public class Main {
    
    public static void main(String[] args) throws Exception {
        File input = new File("input.txt");
        if (!input.exists() && input.createNewFile()) {
            System.out.println("Файл был создан. Введите в него следующие данные: начало диапазона, конец диапазона, шаг построения, функцию (используйте x)");
            return;
        }
        
        FileReader fileReader = new FileReader(input);
        try {
            Map<Double, String> doubleMap = new TreeMap<>();
            fillMap(doubleMap, fileReader);
            saveFile(doubleMap);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    
    private static void saveFile(Map<Double, String> map) throws IOException {
        FileWriter output = new FileWriter("output.txt");
        Optional<String> optional = map.values().stream()
            .max(Comparator.comparingInt(aDouble -> String.valueOf(aDouble).length()));
        if (optional.isPresent()) {
            int maxLength = optional.get().length() + 5;
            output.write(StringUtils.repeat('-', maxLength * 2 + 3) + "\n");
            output.write(String.format("|%s|%s|\n", StringUtils.center("X", maxLength), StringUtils.center("Y", maxLength)));
            output.write(StringUtils.repeat('-', maxLength * 2 + 3) + "\n");
            for (Map.Entry<Double, String> entry : map.entrySet()) {
                output.write(String.format("|%" + maxLength + "s|%" + maxLength + "s|\n", entry.getKey(), entry.getValue()));
            }
            output.write(StringUtils.repeat('-', maxLength * 2 + 3));
        } else {
            output.write("Программа не смогла произвести подсчеты");
        }
        output.close();
    }
    
    private static void fillMap(Map<Double, String> doubleMap, FileReader reader) throws Exception {
        Scanner scanner = new Scanner(reader);
        double start = readDouble(scanner, "начало диапазона");
        double stop = readDouble(scanner, "конец диапазона");
        double step = readDouble(scanner, "шаг построения");
        if (step <= 0) {
            System.out.println("Шаг построения не может быть <= 0");
            return;
        }
        
        String function = readString(scanner, "функцию");
        List<String> parsedExpression = parseExpression(function);
        
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
        reader.close();
    }
    
    private static double readDouble(Scanner scanner, String name) throws Exception {
        hasNext(scanner, name);
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exception) {
            throw new Exception("Не удалось считать " + name + " из файла");
        }
    }
    
    private static String readString(Scanner scanner, String name) throws Exception {
        hasNext(scanner, name);
        return scanner.nextLine();
    }
    
    private static void hasNext(Scanner scanner, String name) throws Exception {
        if (!scanner.hasNext()) {
            throw new Exception("Не удалось считать " + name + " из файла");
        }
    }
    
    public static List<String> parseExpression(String expression) throws Exception {
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
    
    private static String getResult(List<String> blocks) {
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
