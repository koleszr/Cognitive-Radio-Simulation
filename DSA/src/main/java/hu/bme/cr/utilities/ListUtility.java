package hu.bme.cr.utilities;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtility {
	
	public static List<String> formatDoubleList(List<Double> doubles, Function<Double, String> f) {
		return doubles.stream().map(f).collect(Collectors.toList());
	}
}
