package pl.zakrzewb.optimization.algorithm;

public class QuickSortRecursive {

    public double[] sort(double[] unsortedVector) {
        if (unsortedVector == null) {
            throw new IllegalArgumentException("Unsorted vector cannot be null.");
        }

        double[] vectorToSort = unsortedVector.clone();

        quicksort(vectorToSort);

        return vectorToSort;
    }

    private void quicksort(double[] data) {
        int left = 0;
        int right = data.length - 1;
        quicksort(data, left, right);
    }

    private void quicksort(double[] data, int left, int right) {
        if (left < right) {
            int pivot = splitData(data, left, right);
            quicksort(data, left, pivot - 1);
            quicksort(data, pivot + 1, right);
        }
    }

    private int splitData(double[] data, int start, int end) {
        int left = start + 1;
        int right = end;

        while (left < right) {
            while (left < right && data[left] < data[start]) {
                left++;
            }

            while (left < right && data[right] >= data[start]) {
                right--;
            }

            swap(data, left, right);
        }

        if (data[left] >= data[start]) {
            left--;
        }

        swap(data, left, start);

        return left;
    }

    private void swap(double[] data, int firstId, int secondId) {
        if (firstId != secondId) {
            double firstValue = data[firstId];
            data[firstId] = data[secondId];
            data[secondId] = firstValue;
        }
    }
}