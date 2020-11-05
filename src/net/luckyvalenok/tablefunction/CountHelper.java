package net.luckyvalenok.tablefunction;

import java.util.List;

public class CountHelper {
    
    private final List<String> list;
    private final int index;
    private int offset;
    
    public CountHelper(List<String> list, int index) {
        this.list = list;
        this.index = index;
    }
    
    public int getIndex() {
        return index;
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
}
