package hu.bme.cr.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtility {
	
	private ListUtility() {
		
	}
	
	public static List<String> formatDoubleList(List<Double> doubles, Function<Double, String> f) {
		return doubles.stream().map(f).collect(Collectors.toList());
	}
	
	public static List<Integer> getIndexesDescending(List<Double> doubles) {
		Set<Double> copySet = new HashSet<>(doubles);
		List<Double> copy = new ArrayList<>(copySet);
		Collections.sort(copy);
		Collections.reverse(copy);
		

		List<Integer> indexes = new ArrayList<>();
		for (Double d : copy) {
			if (Collections.frequency(doubles, d) > 1) {
				List<Integer> tmp = new ArrayList<>();
				for (int i = 0; i < doubles.size(); i++) {
					if (doubles.get(i).equals(d)) {
						tmp.add(i);
					}
				}
				Collections.shuffle(tmp);
				indexes.addAll(tmp);
			}
			else {
				indexes.add(doubles.indexOf(d));
			}
		}
		
		return indexes;
	}
	
	public static <T> List<List<T>> getInitial2DList(Class<T> cls, int x, int y) {
		List<List<T>> result = new ArrayList<>(x);
		
		for (int i = 0; i < x; i++) {
			result.add(new ArrayList<>(y));
		}
		
		return result;
	}
	
	public static <T> List<T> fillListWithNValues(T value, int n) {
		List<T> result = new ArrayList<>(n);
		
		for (int j = 0; j < n; j++) {
			result.add(value);
		}
		
		return result;
	}
	
	public static void swapToMax(List<Double> doubles, List<Integer> order, int i) {
		if (Collections.max(doubles).equals(doubles.get(i)) 
				&& !order.get(0).equals(i)) {
			
			Collections.swap(order, 0, order.indexOf(i));
		}
	}
}
