package tool;

import datamodel.Triple;

public class SimpleTool {
	/**
	 * 二分查找，找到该值在数组中的下标，否则为-1
	 */
	public static int binarySerach(int[] array, int key) {
	    int left = 0;
	    int right = array.length - 1;

	    // 这里必须是 <=
	    while (left <= right) {
	        int mid = (left + right) / 2;
	        if (array[mid] == key) {
	            return mid;
	        }else if (array[mid] < key) {
	            left = mid + 1;
	        }else {
	            right = mid - 1;
	        }//of if
	    }//of while

	    return -1;
	}//of binarySerach
	
	/**
	 *************************** 
	 * Print an int matrix, simply for test.
	 * 
	 * @param paraMatrix
	 *            The given matrix. Different rows may contain different number
	 *            of values.
	 *************************** 
	 */
	public static void printMatrix(int[][] paraMatrix) {
		if (paraMatrix.length == 0) {
			System.out.println("This is an empty matrix.");
			return;
		} else {
			System.out.println("This is an int matrix: ");
		}// Of if

		for (int i = 0; i < paraMatrix.length; i++) {
			if (paraMatrix[i] == null) { // modified by zhr 2014/3/30
				continue;
			}// of if
			for (int j = 0; j < paraMatrix[i].length; j++) {
				System.out.print("" + paraMatrix[i][j] + ",");
			}// Of for j
			System.out.println();
		}// Of for i
	}// Of printMatrix

	/**
	 *************************** 
	 * Print an int matrix, simply for test.
	 * 
	 * @param paraMatrix
	 *            The given matrix. Different rows may contain different number
	 *            of values.
	 *************************** 
	 */
	public static void printMatrix(double[][] paraMatrix) {
		if (paraMatrix.length == 0) {
			System.out.println("This is an empty matrix.");
			return;
		} else {
			System.out.println("This is a matrix: ");
		}// Of if

		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[i].length; j++) {
				System.out.print("" + paraMatrix[i][j] + "\t");
			}// Of for j
			System.out.println();
		}// Of for i
	}// Of printMatrix

	/**
	 *************************** 
	 * Print an int matrix, simply for test.
	 * 
	 * @param paraMatrix
	 *            The given matrix. Different rows may contain different number
	 *            of values.
	 *************************** 
	 */
	public static void printMatrix(boolean[][] paraMatrix) {
		if (paraMatrix.length == 0) {
			System.out.println("This is an empty matrix.");
			return;
		} else {
			System.out.println("This is an boolean matrix: ");
		}// Of if

		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[i].length; j++) {
				System.out.print("" + paraMatrix[i][j] + "\t");
			}// Of for j
			System.out.println();
		}// Of for i
	}// Of printMatrix
	
	/**
	 *************************** 
	 * Print an int array, simply for test.
	 * 
	 * @param paraIntArray
	 *            The given int array.
	 *************************** 
	 */
	public static void printIntArray(int[] paraIntArray) {
		if (paraIntArray == null || paraIntArray.length == 0) {
			System.out.println("This is an empty int array.");
			return;
		} else {
			System.out.print("This is an int array: ");
		}
		for (int i = 0; i < paraIntArray.length; i++) {
			System.out.print("" + paraIntArray[i] + "\t");
		}// Of for i
		System.out.println();
	}// Of paraIntArray

	/**
	 *************************** 
	 * Print a long array, simply for test.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 *************************** 
	 */
	public static void printLongArray(long[] paraLongArray) {
		for (int i = 0; i < paraLongArray.length; i++) {
			System.out.print("" + paraLongArray[i] + "\t");
		}// Of for i
		System.out.println();
	}// Of printLongArray
	
	/**
	 ************************* 
	 * Print a double array.
	 ************************* 
	 */
	public static void printDoubleArray(double[] paraDoubleArray) {
		for (int i = 0; i < paraDoubleArray.length; i++) {
			System.out.print(paraDoubleArray[i] + " ");
		}// Of for i
		System.out.println();
	}// Of printDoubleArray

	/**
	 *************************** 
	 * Print a long array, zero is not printed. Simply for test.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 *************************** 
	 */
	public static void printLongArrayNoZero(long[] paraLongArray) {
		for (int i = 0; i < paraLongArray.length; i++) {
			if (paraLongArray[i] == 0)
				continue;
			System.out.print("" + paraLongArray[i] + "\t");
		}// Of for i
	}// Of printLongArrayNoZero
	
	/**
	 *************************** 
	 * Print an triple, simply for test.
	 * 
	 * @param paraTriple
	 *            The given triple object.
	 *            of values.
	 *************************** 
	 */
	public static void printTriple(Triple paraTriple) {
		System.out.println("user: " + paraTriple.i + " item: " + paraTriple.j + " rate: " + paraTriple.rating +
				" userGroupID: " + paraTriple.userGrp + " userGroupPosition: " + paraTriple.userGrpPos );
	}// Of printTriple
	
	/**
	 *************************** 
	 * Print an triple, simply for test.
	 * 
	 * @param paraTriple
	 *            The given triple object.
	 *            of values.
	 *************************** 
	 */
	public static void printTriple(Triple[] paraTriple) {
		for(int i = 0; i < paraTriple.length; i ++){
			System.out.println("user: " + paraTriple[i].i + " item: " + paraTriple[i].j + " rate: " + paraTriple[i].rating +
					" userGroupID: " + paraTriple[i].userGrp + " userGroupPosition: " + paraTriple[i].userGrpPos );
		}//Of for i
		
	}// Of printTriple
}//Of class SimpleTool
