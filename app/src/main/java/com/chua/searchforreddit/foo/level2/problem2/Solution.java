package com.chua.searchforreddit.foo.level2.problem2;

class Scratch {
    public static void main(String[] args) {
        int[] testCase1 = {2, 0, 2, 2, 0};
        String output1 = "8";
        runner(testCase1, output1);

        int[] testCase2 = {-2, -3, 4, -5};
        String output2 = "60";
        runner(testCase2, output2);

        int[] testCase3 = { -3, 0, 0, 0, 0 };
        String output3 = "0";
        runner(testCase3, output3);

        int[] testCase5 = { -3 };
        String output5 = "-3";
        runner(testCase5, output5);

        int[] testCustom = { -2, -5, 3};
        String custom = "30";
        runner(testCustom, custom);

        int[] testCustom2 = { -99, -99};
        String custom2 = "9801";
        runner(testCustom2, custom2);

        int[] testCustom2_1 = { -99, -99, -98};
        String custom2_1 = "9801";
        runner(testCustom2_1, custom2_1);

        int[] testCustom3 = { 1, 2147483647};
        String custom3 = "2147483647";
        runner(testCustom3, custom3);

        int[] testCustom4 = { -1, -2147483647};
        String custom4 = "2147483647";
        runner(testCustom4, custom4);

        int[] testCustom5 = { 0 };
        String custom5 = "0";
        runner(testCustom5, custom5);

        int[] testCustom5_1 = { 0, 0, 0 };
        String custom5_1 = "0";
        runner(testCustom5_1, custom5_1);

        int[] testCustom6 = { -2, -2147483648};
        String custom6 = "4294967296";
        runner(testCustom6, custom6);

    }

    public static void runner(int[] input, String output) {
        for(int i : input) System.out.print(i +" ");
        System.out.println();
        String actual = Solution.solution(input);
        System.out.println("expected: " + output + " actual: " + actual + " " + output.equals(actual));
        System.out.println();
    }
}

class Solution {

    public static String solution(int[] xs) {

        if(xs.length == 1) {
            return String.valueOf(xs[0]);
        }

        int positiveNumbers = 0, negativeNumbers = 0;
        for (int number : xs) {
            if (number > 0) positiveNumbers++;
            else if (number < 0) negativeNumbers++;
        }

        int[] negative = new int[negativeNumbers];
        int[] positive = new int[positiveNumbers];

        int p = 0, n = 0;
        for (int x : xs) {
            if (x > 0) {
                positive[p] = x;
                p++;
            }
            if (x < 0) {
                negative[n] = x;
                n++;
            }
        }

        if (negative.length % 2 != 0) {
            sort(negative, true);
            negative = pop(negative);
        }

        if (positive.length > 0 || negative.length > 0) {
            long product = 1;
            for (int signedNumber : merge(positive, negative))
                product *= signedNumber;

            return String.valueOf(product);
        }

        return String.valueOf(0);
    }

    public static int[] merge(int[] array1, int[] array2) {
        int length = array1.length + array2.length;
        int[] merged = new int[length];
        int pos = 0;
        for (int number : array1) {
            merged[pos] = number;
            pos++;
        }
        for (int number : array2) {
            merged[pos] = number;
            pos++;
        }

        return merged;
    }

    public static int[] sort(int[] array, boolean ascending) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                int temp;
                if (ascending) {
                    if (array[i] > array[j]) {
                        temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                } else {
                    if (array[i] < array[j]) {
                        temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }
            }
        }

        return array;
    }

    public static int[] pop(int[] array) {
        int n = array.length - 1;
        int[] tempArray = new int[n];
        for (int i = 0; i < n; i++) {
            tempArray[i] = array[i];
        }
        return tempArray;
    }
}
