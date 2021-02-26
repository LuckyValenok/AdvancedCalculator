package net.luckyvalenok.advancedcalculator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestingClass {
    public Calculator calculator;
    
    @Before
    public void setup() {
        calculator = new Calculator();
    }
    
    @Test
    public void testCalculator1() throws Exception {
        String expression = "1 + 3";
        Assert.assertArrayEquals(calculator.parseExpression(expression).toArray(new String[0]), new String[] {"1", "3", "+"});
    }
    
    @Test
    public void testCalculator2() throws Exception {
        String expression = "(1 + 3) * 2";
        Assert.assertArrayEquals(calculator.parseExpression(expression).toArray(new String[0]), new String[] {"1", "3", "+", "2", "*"});
    }
    
    @Test
    public void testCalculator3() throws Exception {
        String expression = "log(1+3)";
        Assert.assertArrayEquals(calculator.parseExpression(expression).toArray(new String[0]), new String[] {"1", "3", "+", "log"});
    }
    
    @Test
    public void testCalculator4() throws Exception {
        String expression = "log(log(1 + 3))";
        Assert.assertArrayEquals(calculator.parseExpression(expression).toArray(new String[0]), new String[] {"1", "3", "+", "log", "log"});
    }
    
    @Test(expected = Exception.class)
    public void testCalculator5() throws Exception {
        String expression = "log(1+3";
        Assert.assertArrayEquals(calculator.parseExpression(expression).toArray(new String[0]), new String[] {"1", "3", "+", "log"});
    }
    
    @Test(expected = Exception.class)
    public void testCalculator6() throws Exception {
        String expression = "log 1+3)";
        Assert.assertArrayEquals(calculator.parseExpression(expression).toArray(new String[0]), new String[] {"1", "3", "+", "log"});
    }
    
    @Test(expected = Exception.class)
    public void testCalculator7() throws Exception {
        String expression = "1 . 2";
        Assert.assertArrayEquals(calculator.parseExpression(expression).toArray(new String[0]), new String[] {"1", "2", "."});
    }
    
    @Test
    public void testCalculator8() throws Exception {
        String expression = "1 + 2";
        Assert.assertEquals(calculator.getResult(calculator.parseExpression(expression)), "3.0");
    }
    
    @Test
    public void testCalculator9() throws Exception {
        String expression = "(1 + 1) * 3";
        Assert.assertEquals(calculator.getResult(calculator.parseExpression(expression)), "6.0");
    }
    
    @Test
    public void testCalculator10() throws Exception {
        String expression = "(1 + 0.5) * 3";
        Assert.assertEquals(calculator.getResult(calculator.parseExpression(expression)), "4.5");
    }
}
