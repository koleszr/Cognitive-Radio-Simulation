package hu.bme.cr;

import java.util.Arrays;
import java.util.List;

import hu.bme.cr.strategies.StrategySpace;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        List<List<Boolean>> strategies = StrategySpace.getStrategySpace(Arrays.asList(true, true, false, false));
        
        strategies.stream().forEach(System.out::println);
    }
}
