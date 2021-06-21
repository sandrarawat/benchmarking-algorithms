package ie.gmit.dip;

import java.util.Arrays;
import java.util.Random;

public class Benchmarking {
	/** Main function **/

	public static void main(final String[] args) {
		executionTimeReport();
	}

	/** Test data generator **/

	// Generates a random array of size 'size'.
	// Part of the array is sorted, while the rest is chosen uniformly
	// at random; the 'randomness' parameter sets what percent of the
	// array is chosen at random.
	public static int[] generateSample(int size, int randomness) {
		int[] sample = new int[size];

		Random random = new Random();
		int previousElement = 0;
		for (int i = 0; i < size; i++) {
			if (random.nextInt(100) >= randomness) {
				int randomOffset = random.nextInt(3);
				int currentElement = previousElement + randomOffset;
				sample[i] = currentElement;
				previousElement = currentElement;
			} else {
				sample[i] = random.nextInt(size);
			}
		}

		return sample;
	}

	public static int[] generateQuickSortBestSample(int size) {
		int[] array = new int[size];
		generateQuickSortBestSampleStep(array, 0, size-1, 0, size-1, true);
		return array;
	}

	private static void generateQuickSortBestSampleStep(int[] arr, int from, int to, int fromVal, int toVal, boolean pivotStart) {
		if (to - from == 0) {
			arr[from] = fromVal;
			return;
		}
		if (to - from == 1) {
			arr[from] = fromVal;
			arr[to] = toVal;
			return;
		}

		int pivotPos = (to - from) / 2 + from;
		int pivot = (to - from) / 2 + fromVal;
		if (pivotStart) {
			arr[from] = pivot;
			generateQuickSortBestSampleStep(arr, from + 1, pivotPos, fromVal, pivot-1, false);
			generateQuickSortBestSampleStep(arr, pivotPos + 1, to, pivot + 1, toVal, true);
		}
		else {
			arr[to] = pivot;
			generateQuickSortBestSampleStep(arr, from, pivotPos-1, fromVal, pivot-1, true);
			generateQuickSortBestSampleStep(arr, pivotPos, to - 1, pivot + 1, toVal, false);
		}
	}

	/**
	 * Reverses an array. It is used for calculating the worst case of some
	 * algorithms.
	 *
	 * @param arr - the array will be reversed
	 * @return
	 */
	public static void reverseArray(int arr[]) {
		for (int i = 0; i < arr.length / 2; i++) {
			int temp = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = temp;
		}
	}

	/** Auxiliary code, that measures performance of sorting algorithms **/

	private static int[] SAMPLE_SIZES = new int[] { 250, 500, 1000, 1500, 2000, 2500, 5000, 7500, 10000 };

	private static void executionTimeReport() {
		for (int size : SAMPLE_SIZES) {
			executionTimeReport(size);
		}
	}

	public static interface Function<A, B> {
		public B apply(A arg);
	}

	public static Function<int[], int[]> heapSort = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] array) {
			SortAlgorithms.heapSort(array);
			return array;
		}
	};

	public static Function<int[], int[]> bubbleSort = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] array) {
			SortAlgorithms.bubbleSort(array);
			return array;
		}
	};

	public static Function<int[], int[]> bucketSort = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] array) {
			SortAlgorithms.bucketSort(array);
			return array;
		}
	};

	public static Function<int[], int[]> quickSort = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] array) {
			SortAlgorithms.quickSort(array, 0, array.length - 1);
			return array;
		}
	};

	public static Function<int[], int[]> insertionSort = new Function<int[], int[]>() {
		@Override
		public int[] apply(int[] array) {
			SortAlgorithms.insertionSort(array);
			return array;
		}
	};

	// Execute an algorithm on an input and return its runtime.
	private static String execute(Function<int[], int[]> algorithm, int[] input, int[] reference) {
		// To get accurate results even for small inputs, we repeat
		// the algorithm several times in a row and count the total time.
		// We pick the number of repetitions automatically so that
		// the total time is at least 10ms.
		//
		// To pick the number of repetitions, we start by assuming
		// that one repetition will be enough. We then execute the
		// algorithm and measure how long it takes. If it took less
		// than 10ms, we scale up the number of repetitions by
		// an appropriate factor. E.g., if the algorithm only took
		// 1ms, we will multiply the number of repetitions by 10.
		// We then repeat this whole process with the new number of
		// repetitions.
		//
		// Once the repetitions take more than 10ms, we try it three
		// times and take the smallest measured runtime. This avoids
		// freakish results due to e.g. the garbage collector kicking
		// in at the wrong time.

		// The final result of the algorithm.
		int[] result = {};
		// How many repetitions we use for warm-up
		int warmUp = 3;
		// How many repetitions we use to calculate average
		int repetitions = 10;
		// The lowest runtime we saw with the current number of repetitions.
		long runtime = 0;
		try {
			// Build the input arrays in advance to avoid memory
			// allocation during testing.
			// Try to reduce unpredictability
			int totalRepetitions = repetitions + warmUp;
			int[][] inputs = new int[totalRepetitions][];
			for (int i = 0; i < totalRepetitions; i++)
				inputs[i] = Arrays.copyOf(input, input.length);

			System.gc();
			Thread.yield();
			for (int i = 0; i < totalRepetitions; i++) {
				// Run the algorithm
				long startTime = System.nanoTime();
				result = algorithm.apply(inputs[i]);
				long endTime = System.nanoTime();
				if (i >= warmUp) {
					runtime += endTime - startTime;
				}
			}
		} catch (UnsupportedOperationException uop) {
			return "-";
		} catch (Exception e) {
			return "EXCEPTION";
		} catch (StackOverflowError e) {
			return "STACK OVERFLOW";
		}
		if (Arrays.equals(result, reference)) {
			return String.format("%6.3f", (double) runtime / ((long) repetitions * 1000000)) + "ms";
		} else {
			return "INCORRECT";
		}
	}

	private static void executionTimeReport(int size) {
		int[] sortedSample = generateSample(size, 0);
		int[] reverseArray = generateSample(size, 100);
		int[] randomSample = generateSample(size, 100);
		reverseArray(reverseArray);

		int[] sortedSampleReference = Arrays.copyOf(sortedSample, sortedSample.length);
		Arrays.sort(sortedSampleReference);

		int[] reverseArrayReference = Arrays.copyOf(reverseArray, reverseArray.length);
		Arrays.sort(reverseArrayReference);

		int[] randomSampleReference = Arrays.copyOf(randomSample, randomSample.length);
		Arrays.sort(randomSampleReference);

		int[] quicksortBestSample = generateQuickSortBestSample(size);
		int[] quicksortBestSampleReference = Arrays.copyOf(quicksortBestSample, quicksortBestSample.length);
		Arrays.sort(quicksortBestSampleReference);

		System.out.println(String.format(
				"### Arrays of length %d\n" + "=================================================================\n"
						+ "| Algorithm      | %14s | %14s | %14s |\n" + "| Bubble sort    | %14s | %14s | %14s |\n"
						+ "| Insertion Sort | %14s | %14s | %14s |\n" + "| Heap sort      | %14s | %14s | %14s |\n"
						+ "| Bucket Sort    | %14s | %14s | %14s |\n" + "| Quicksort      | %14s | %14s | %14s |\n",
				size, "Avg", "Worst ", "Best",
				
				execute(bubbleSort, randomSample, randomSampleReference),
				execute(bubbleSort, reverseArray, reverseArrayReference),
				execute(bubbleSort, sortedSample, sortedSampleReference),

				execute(insertionSort, randomSample, randomSampleReference),
				execute(insertionSort, reverseArray, reverseArrayReference),
				execute(insertionSort, sortedSample, sortedSampleReference),
				
				execute(heapSort, randomSample, randomSampleReference),
				execute(heapSort, reverseArray, reverseArrayReference), // For heapsort indeed no best or worst case in general: every time heapify is executed for the whole heap height
				execute(heapSort, sortedSample, sortedSampleReference),
		
				execute(bucketSort, randomSample, randomSampleReference),
				execute(bucketSort, reverseArray, reverseArrayReference),
				execute(bucketSort, sortedSample, sortedSampleReference),

				execute(quickSort, randomSample, randomSampleReference),
				execute(quickSort, sortedSample, sortedSampleReference), // When array is sorted, quicksort is the least effective
				execute(quickSort, quicksortBestSample, quicksortBestSampleReference)));
	}
}

