package com.forestcode.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamWork_1 {

	public static void main(String[] args) {

		// Declaring a immutable(not changeable list) List
		List<Integer> list1 = List.of(2, 3, 4, 55, 90, 12, 80, 19);
		System.out.println("immutable List" + list1);

		// Declaring a mutable(changeable list) List
		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(2);
		list2.add(3);
		list2.add(4);
		list2.add(55);
		list2.add(90);
		list2.add(12);
		list2.add(80);
		list2.add(19);
		System.out.println("mutable List" + list2);

		// Declaring a immutable(not changeable list) List
		List<Integer> List3 = Arrays.asList(2, 3, 4, 55, 90, 12, 80, 19);
		System.out.println("immutable List" + List3);
		System.out.println();


		/* List of even no form list1 */

		// without stream api
		List<Integer> evenlist = new ArrayList<Integer>();
		for (Integer i : list1) {
			if (i % 2 == 0) {
				evenlist.add(i);
			}
		}
		System.out.println("Even num list from list1  without Stream" + evenlist);
		System.out.println();
		
		
		// with stream api
//Method _ 1
		Stream<Integer> stream = list1.stream();
		List<Integer> newevenlist1 = stream.filter(e->e%2==0).collect(Collectors.toList());
		System.out.println("Even num list from list1 using stream method_1 " + newevenlist1);
//Method _ 2
		List<Integer> newevenlist2 = list1.stream().filter(e->e%2==0).collect(Collectors.toList());
		System.out.println("Even num list from list1 using stream method_2 " + newevenlist2);

		
		List<Integer> newevenlist3 = list1.stream().filter(e->e>60).collect(Collectors.toList());
		System.out.println("greater than 10 list from list1 using stream method_3 " + newevenlist3);

		
		
		
		
		
		
		
		
		
		
	}
}
