package com.forestcode.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Stream_Object_Work_2 {

	public static void main(String[] args) {

		// 1 - empty Stream object create
		Stream<Object> es = Stream.empty();
		es.forEach(e -> {
			System.out.println(e);
		});

		// 2 - existing array Stream object create
		String name[] = { "hello", "abcd", "xyz", "new", "human" };
		Stream<String> st1 = Stream.of(name);
		st1.forEach(e -> {
			System.out.println(e);
		});

		// 3 - Using Stream Builder create Stream object create
		Stream<Object> build = Stream.builder().build();
		build.forEach(e -> {
			System.out.println(e);
		});

		// 4 - Using new arrays create Stream object create
		IntStream intStream = Arrays.stream(new int[] { 2, 3, 4, 88, 15, 86, 50 });
		intStream.forEach(e -> {
			System.out.println(e);
		});
		
		System.out.println();		System.out.println();

		// 5 - Using new arrays create Stream object create
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		arrayList.add(25);
		arrayList.add(28);
		arrayList.add(38);
		arrayList.add(43);
		arrayList.add(59);

		Stream<Integer> streamarray = arrayList.stream();
		streamarray.forEach(e -> {
			System.out.println(e);
		});
	}
	
	
	
	
}
