package net.luckyvalenok.tablefunction;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    
    private static double start;
    private static double stop;
    
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите шаг построения.");
        double step;
        while ((step = readDouble(reader)) <= 0) {
            System.out.println("Шаг построения должен быть больше нуля. Попробуйте снова.");
        }
        
        readRange(reader);
        
        Map<Double, Double> doubleMap = new TreeMap<>();
        for (; start <= stop; start += step)
            doubleMap.put(start, count(start));
        
        doubleMap.values().stream()
            .max(Comparator.comparingInt(aDouble -> (aDouble + "").length()))
            .ifPresent(aDouble -> {
                    int length = (aDouble + "").length();
                    printLine(length);
                    System.out.format("|%s|%s|\n", StringUtils.center("X", length), StringUtils.center("Y", length));
                    printLine(length);
                    for (Map.Entry<Double, Double> entry : doubleMap.entrySet()) {
                        System.out.format("|%" + length + "s|%" + length + "s|\n", entry.getKey(), entry.getValue());
                    }
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
    
    private static double count(double x) {
        return Math.exp(1 + x * x) / Math.cbrt(Math.cos(x)) * Math.pow(Math.E, -(x * x));
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
