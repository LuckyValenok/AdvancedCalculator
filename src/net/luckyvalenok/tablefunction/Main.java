package net.luckyvalenok.tablefunction;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    
    private static double start, stop;
    
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите шаг построения.");
        double step = readDouble(reader);
        
        readRange(reader);
        
        Map<Double, Double> doubleMap = new TreeMap<>();
        for (; start <= stop; start += step)
            doubleMap.put(start, func(start));
        
        doubleMap.values().stream().max(Comparator.comparingInt(o -> String.valueOf(o).length())).ifPresent(doub -> {
                int length = doub.toString().length();
                printLine(length);
                System.out.format("|%s|%s|\n", StringUtils.center("X", length), StringUtils.center("Y", length));
                printLine(length);
                for (Map.Entry<Double, Double> entry : doubleMap.entrySet())
                    System.out.format("|%" + length + "s|%" + length + "s|\n", entry.getKey(), entry.getValue());
                printLine(length);
            }
        );
    }
    
    private static boolean readRange(BufferedReader reader) {
        System.out.println("Введите начало диапазона значений x.");
        start = readDouble(reader);
        
        System.out.println("Введите конец диапазона значений x.");
        stop = readDouble(reader);
        
        if (start > stop) {
            System.out.println("У вас начало диапазона больше, чем конец. Попробуйте снова.");
            return readRange(reader);
        }
        return true;
    }
    
    private static void printLine(int i) {
        System.out.println(StringUtils.repeat('-', i * 2 + 3));
    }
    
    private static double func(double x) {
        return Math.pow(Math.cos(20 * x) + Math.sqrt(Math.abs(x)) - 0.7, 0.99);
    }
    
    private static double readDouble(BufferedReader reader) {
        try {
            return Double.parseDouble(reader.readLine());
        } catch (NumberFormatException | IOException exception) {
            System.out.println("То, что вы ввели, не является числом.");
            return readDouble(reader);
        }
    }
}
