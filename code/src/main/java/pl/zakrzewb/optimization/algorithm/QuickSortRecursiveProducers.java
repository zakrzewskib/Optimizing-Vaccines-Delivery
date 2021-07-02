package pl.zakrzewb.optimization.algorithm;

import pl.zakrzewb.optimization.data.Producer;

import java.util.ArrayList;

public class QuickSortRecursiveProducers {

    public void sort(ArrayList<Producer> unsortedVector) {
        if (unsortedVector == null) {
            throw new IllegalArgumentException("Unsorted vector cannot be null.");
        }

        quicksort(unsortedVector);
    }

    private void quicksort(ArrayList<Producer> data) {
        int left = 0;
        int right = data.size() - 1;
        quicksort(data, left, right);
    }

    private void quicksort(ArrayList<Producer> data, int left, int right) {
        if (left < right) {
            int pivot = splitData(data, left, right);
            quicksort(data, left, pivot - 1);
            quicksort(data, pivot + 1, right);
        }
    }

    private int splitData(ArrayList<Producer> data, int start, int end) {
        int left = start + 1;
        int right = end;

        while (left < right) {
            while (left < right && data.get(left).getId() < data.get(start).getId()) {
                left++;
            }

            while (left < right && data.get(right).getId() >= data.get(start).getId()) {
                right--;
            }

            swap(data, left, right);
        }

        if (data.get(left).getId() >= data.get(start).getId()) {
            left--;
        }

        swap(data, left, start);

        return left;
    }

    private void swap(ArrayList<Producer> data, int firstId, int secondId) {
        if (firstId != secondId) {
            Producer firstValue = data.get(firstId);
            data.set(firstId, data.get(secondId));
            data.set(secondId, firstValue);
        }
    }
}
