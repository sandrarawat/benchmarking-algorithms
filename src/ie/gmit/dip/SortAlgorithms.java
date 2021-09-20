package ie.gmit.dip;
import java.util.Stack;

/**
 * 
 *
 *
 */
public class SortAlgorithms {
	public static void heapSort(int[] arr) {
		for (int i = arr.length; i > 0; i--) {
			max_heapify(arr, i);

			int temp = arr[0];
			arr[0] = arr[i - 1];
			arr[i - 1] = temp;
		}
	}

	private static void max_heapify(int[] arr, int limit) {
		if (arr.length <= 0 || arr.length < limit)
			return;
		int parentIdx = limit / 2;

		for (; parentIdx >= 0; parentIdx--) {
			if (parentIdx * 2 >= limit) {
				continue;
			}
			int left = parentIdx * 2;
			int right = (left + 1) >= limit ? left : (left + 1);

			int maxChildId = arr[left] >= arr[right] ? left : right;
			if (arr[maxChildId] > arr[parentIdx]) {
				int temp = arr[parentIdx];
				arr[parentIdx] = arr[maxChildId];
				arr[maxChildId] = temp;
			}
		}
	}
	public static void bubbleSort(int[] arr) {
		for (int i = arr.length - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (arr[j] > arr[j + 1]) {
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}

	public static void bucketSort(int arr[]) {
		int n = arr.length;
		for (int i = 0; i < n - 1; i++)
			for (int j = 0; j < n - i - 1; j++)
				if (arr[j] > arr[j + 1]) {
					// swap arr[j+1] and arr[i]
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
	}

	public static void quickSort(int[] arr, int low, int high) {
		if (arr.length <= 0)
			return;
		if (low >= high)
			return;
		int left = low;
		int right = high;

		int temp = arr[left];
		while (left < right) {
			while (left < right && arr[right] >= temp) {
				right--;
			}
			arr[left] = arr[right];
			while (left < right && arr[left] <= temp) {
				left++;
			}
			arr[right] = arr[left];
		}
		arr[left] = temp;
		quickSort(arr, low, left - 1);
		quickSort(arr, left + 1, high);
	}

	public static void quickSortByStack(int[] arr) {
		if (arr.length <= 0)
			return;
		Stack<Integer> stack = new Stack<Integer>();

		stack.push(0);
		stack.push(arr.length - 1);
		while (!stack.isEmpty()) {
			int high = stack.pop();
			int low = stack.pop();

			int pivotIdx = partition(arr, low, high);

			if (pivotIdx > low) {
				stack.push(low);
				stack.push(pivotIdx - 1);
			}
			if (pivotIdx < high && pivotIdx >= 0) {
				stack.push(pivotIdx + 1);
				stack.push(high);
			}
		}
	}

	private static int partition(int[] arr, int low, int high) {
		if (arr.length <= 0)
			return -1;
		if (low >= high)
			return -1;
		int l = low;
		int r = high;

		int pivot = arr[l]; //
		while (l < r) {
			while (l < r && arr[r] >= pivot) { //
				r--;
			}
			arr[l] = arr[r];
			while (l < r && arr[l] <= pivot) { //
				l++;
			}
			arr[r] = arr[l];
		}
		arr[l] = pivot; //
		return l;
	}

	public static void insertionSort(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = i + 1; j > 0; j--) {
				if (arr[j - 1] <= arr[j])
					break;
				int temp = arr[j];
				arr[j] = arr[j - 1];
				arr[j - 1] = temp;
			}
		}
	}

}
