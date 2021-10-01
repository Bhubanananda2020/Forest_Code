package com.forestcode.stream;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamMethod {

	public static void main(String[] args) {

		/* Filter Method */
		List<String> namelist = List.of("Akash", "ravan", "human", "Arman");
		List<String> collect = namelist.stream().filter(e -> e.startsWith("A")).collect(Collectors.toList());
		System.out.println(collect);

		/* Map Method */
		List<Integer> numlist = List.of(29,88,1, 5, 9, 25);
		List<Integer> collect2 = numlist.stream().map(e -> e * e).collect(Collectors.toList());
		System.out.println(collect2);

		/* list print with foreach loop */
		namelist.stream().forEach(e -> {
			System.out.println(e);
		});

		/* List print with :: operator */
		namelist.stream().forEach(System.out::println);

		/* Sort the list of number print with :: operator */
		numlist.stream().sorted().forEach(System.out::println);		

		Integer integer = numlist.stream().min((x,y)-> x.compareTo(y)).get();
		System.out.println("min num " +integer);
	
		
		Integer integer2 = numlist.stream().max((x,y)-> x.compareTo(y)).get();
		System.out.println("Max Num "+integer2);
		
		
		
		
	}

}
