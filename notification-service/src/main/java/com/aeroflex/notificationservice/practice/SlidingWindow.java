package com.aeroflex.notificationservice.practice;

public class SlidingWindow {


    static int SubarraySumSizeEqualsToKNaive(int[] array, int k, int n)
    {

        int maxSum = Integer.MIN_VALUE;

        for(int i = 0; i < n-k+1; i++)
        {
            int currentSum = 0;

            for(int j = 0; j < k; j++)
            {
                currentSum += array[i+j];
            }

            maxSum = Math.max(currentSum, maxSum);
        }

        return maxSum;
    }

    static int SubarraySumSizeEqualsToKWithSlidingWindow(int[] array, int k, int n)
    {

        if(n<=k)
        {
            return -1;
        }

        int maxSum = 0;

        for(int i=0;i<k;i++)
        {
            maxSum += array[i];
        }

        int curWindowSum = maxSum;
        for(int j=k;j<n;j++)
        {
            curWindowSum += array[j]-array[j-k];
            maxSum = Math.max(maxSum, curWindowSum);
        }

        return maxSum;
    }

    public static void main(String[] args) {

        int[] array = new int[]{5, 2, -1, 0, 3};
        int k=3;

        int n = array.length;

        int naiveMaxSum = SubarraySumSizeEqualsToKNaive(array, k, n);
        System.out.println("Naive Max Sum: " + naiveMaxSum);

        int slidingWindowMaxSum =  SubarraySumSizeEqualsToKWithSlidingWindow(array, k, n);
        System.out.println("Sliding Window Max Sum: " + slidingWindowMaxSum);

    }
}
