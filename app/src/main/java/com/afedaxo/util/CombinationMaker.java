package com.afedaxo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationMaker<T> {
    private List<List<T>> intList;

    private void combinationUtil(T arr[], T data[], int start,
                                int end, int index, int r)
    {
        // Current combination is ready to be printed, print it
        if (index == r)
        {
            List<T> possibleComb = new ArrayList<>();
            for (int j=0; j<r; j++) {
                possibleComb.add(data[j]);
            }
            intList.add(possibleComb);
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            combinationUtil(arr, data, i+1, end, index+1, r);
        }
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    public List<List<T>> makeCombList(T arr[], int r)
    {
        intList = new ArrayList<>();
        // A temporary array to store all combination one by one
        T data[]= (T[]) new Object[r];

        // Print all combination using temprary array 'data[]'
        combinationUtil(arr, data, 0, arr.length-1, 0, r);
        return intList;
    }
}
