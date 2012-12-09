package com.x3.tictactoe.utils;

public class ArrayUtils {
	
	public static int NOT_FOUND = -1;

	public static int minElement(int[] array) {
		int minElement = array[0];
		for (int i = 1; i < array.length; i++) {
			if (minElement > array[i]) {
				minElement = array[i];
			}
		}
		return minElement;
	}
	
	public static int maxElement(int[] array) {
		int maxElement = array[0];
		for (int i = 1; i < array.length; i++) {
			if (maxElement < array[i]) {
				maxElement = array[i];
			}
		}
		return maxElement;
	}
	
	public static int search(int[] array, int el) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == el) {
				return i;
			}
		}
		return NOT_FOUND;
	}
	
}
