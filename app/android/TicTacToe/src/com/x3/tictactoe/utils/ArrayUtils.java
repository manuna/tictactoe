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
	
	public static int findFirstOf(int[] array, int value, int startIndex) {
		int elIndex = NOT_FOUND;
		for (int i = startIndex; i < array.length; i++) {
			if (array[i] == value) {
				elIndex = i;
				break;
			}
		}
		return elIndex;
	}
	
	public static int findFirstNotOf(int[] array, int value, int startIndex) {
		int elIndex = NOT_FOUND;
		for (int i = startIndex; i < array.length; i++) {
			if (array[i] != value) {
				elIndex = i;
				break;
			}
		}
		return elIndex;
	}
	
	public static int countOf(int[] array, int start, int end, int value) {
		int count = 0;
		for (int i = start; i <= end; i++) {
			if (array[i] == value) {
				count++;
			}
		}
		return count;
	}
	
	public static void rowSlice(int[][] matrix, int row, int[] slice) {
		for (int i = 0; i < slice.length; i++) {
			slice[i] = matrix[row][i];
		}
	}
	
	public static void applyRowSlice(int[][] matrix, int row, int[] slice) {
		for (int i = 0; i < slice.length; i++) {
			matrix[row][i] = slice[i];
		}
	}
	
	public static void columnSlice(int[][] matrix, int column, int[] slice) {
		for (int i = 0; i < slice.length; i++) {
			slice[i] = matrix[i][column];
		}
	}
	
	public static void applyColumnSlice(int[][] matrix, int column, int[] slice) {
		for (int i = 0; i < slice.length; i++) {
			matrix[i][column] = slice[i];
		}
	}
	
	public static void diagSlice(int[][] matrix, int row, int column,
			boolean forwards, int[] slice) {
		int sliceSize = Math.min(matrix.length - row,
				forwards ? matrix[0].length - column : column + 1);
		for (int i = 0; i < sliceSize; i++) {
			int columnIdx = forwards ? i : -i;
			slice[i] = matrix[row + i][column + columnIdx];
		}
		for (int i = sliceSize; i < slice.length; i++) {
			slice[i] = 0;
		}
	}
	
	public static void applyDiagSlice(int[][] matrix, int row, int column,
			boolean forwards, int[] slice) {
		int sliceSize = Math.min(matrix.length - row,
				forwards ? matrix[0].length - column : column + 1);
		for (int i = 0; i < sliceSize; i++) {
			int columnIdx = forwards ? i : -i;
			matrix[row + i][column + columnIdx] = slice[i];
		}
	}
	
	public static int maxRange(int[] array, int value) {
		return maxRange(array, 0, array.length - 1, value);
	}
	
	public static int maxRange(int[] array, int start, int end, int value) {
		int count = 0, maxCount = 0, i = start;
		while (i <= end) {
			if (array[i] == value) {
				count++;
			} else {
				count = 0;
			}
			maxCount = Math.max(maxCount, count);
			i++;
		}
		return Math.max(maxCount, count);
	}
	
}
