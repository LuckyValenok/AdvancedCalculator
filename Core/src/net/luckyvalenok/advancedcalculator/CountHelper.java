package net.luckyvalenok.advancedcalculator;

import java.util.List;

public class CountHelper {
    
    private final List<String> list;
    private final int index;
    private int offset;
    
    public CountHelper(List<String> list, int index) {
        this.list = list;
        this.index = index;
    }
    
    public String remove(int index) {
        return list.remove(index);
    }
    
    public void add(int index, String s) {
        list.add(index, s);
    }
    
    public int getOffset() {
        return offset;
    }
    
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public double[] getDoubles(int offset) {
        remove(index);
        String one = remove(index - offset);
        double[] numbers = new double[1];
        if (offset != 1) {
            numbers = new double[2];
            String two = remove(index - offset);
            numbers[1] = Double.parseDouble(two);
        }
        numbers[0] = Double.parseDouble(one);
        setOffset(index - offset);
        return numbers;
    }
}
