package com.chua.searchforreddit.foo.level2;

public class Solution {
    public static int solution(int total_lambs) {
        int totalLambs = total_lambs;

        int maxPossibleMan = 1;
        int minPossibleMan = 1;

        int sumOfMaxGivenLambs = 1;
        int maxSubordinate1 = 0;
        int maxSubordinate2 = 1;

        int sumOfMinGivenLambs = 1;
        int minSubordinate1 = 1;

        int maxNext = maxSubordinate1 + maxSubordinate2;

        int minNext = minSubordinate1 * 2;

        while (sumOfMaxGivenLambs + maxNext <= totalLambs) {
            sumOfMaxGivenLambs += maxNext;
            maxPossibleMan++;

            maxSubordinate1 = maxSubordinate2;
            maxSubordinate2 = maxNext;
            maxNext = maxSubordinate1 + maxSubordinate2;
        }

        while (sumOfMinGivenLambs + minNext <= totalLambs) {
            sumOfMinGivenLambs += minNext;
            minPossibleMan++;

            minSubordinate1 = minNext;
            minNext = minSubordinate1 * 2;
        }

        return maxPossibleMan - minPossibleMan;
    }
}