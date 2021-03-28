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

public class Main {
    
    public static void main(String[] args) throws Exception {
        File input = new File("input.txt");
        if (!input.exists() && input.createNewFile()) {
            System.out.println("Файл был создан. Введите в него следующие данные: начало диапазона, конец диапазона, шаг построения, функцию (используйте x)");
            return;
        }
        
        FileReader fileReader = new FileReader(input);
        
        Scanner scanner = new Scanner(fileReader);
        double start = readDouble(scanner, "начало диапазона");
        double stop = readDouble(scanner, "конец диапазона");
        if (start < stop) {
            throw new Exception("Начало диапазона должно быть меньше конца диапазона");
        }
        double step = readDouble(scanner, "шаг построения");
        if (step <= 0) {
            throw new Exception("Шаг построения не может быть <= 0");
        }
        String function = readString(scanner, "функцию");
        scanner.close();
        fileReader.close();
        
        try {
            Calculator calculator = new Calculator(start, stop, step, function);
            saveFile(calculator.getFilledMap());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    
    private static void saveFile(Map<Double, Double> map) throws IOException {
        FileWriter output = new FileWriter("output.txt");
        List<String> strings = map.values().stream().collect(ArrayList::new, (strings1, aDouble) -> strings1.add(aDouble + ""), ArrayList::addAll);
        map.keySet().forEach(aDouble -> strings.add(aDouble + ""));
        Optional<String> optional = strings.stream()
            .max(Comparator.comparingInt(aDouble -> String.valueOf(aDouble).length()));
        if (optional.isPresent()) {
            int maxLength = optional.get().length();
            output.write(StringUtils.repeat('-', maxLength * 2 + 3) + "\n");
            output.write(String.format("|%s|%s|\n", StringUtils.center("X", maxLength), StringUtils.center("Y", maxLength)));
            output.write(StringUtils.repeat('-', maxLength * 2 + 3) + "\n");
            for (Map.Entry<Double, Double> entry : map.entrySet()) {
                output.write(String.format("|%" + maxLength + "s|%" + maxLength + "s|\n", entry.getKey(), entry.getValue()));
            }
            output.write(StringUtils.repeat('-', maxLength * 2 + 3));
        } else {
            output.write("Программа не смогла произвести подсчеты");
        }
        output.close();
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
}
