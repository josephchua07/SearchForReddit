package com.chua.searchforreddit.foo.level1;

public class Solution {
    public static String main(String x) {

        String input = x;

        char[] encryptedChars = input.toCharArray();

        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        String reverse = "zyxwvutsrqponmlkjihgfedcba";

        String decrypted = "";
        for (int i = 0; i < encryptedChars.length; i++) {
            char c = encryptedChars[i];

            if (reverse.contains(Character.toString(c))) {
                int index = reverse.indexOf(c);
                decrypted += alphabet[index];
            } else {
                decrypted += c;
            }

        }
        return decrypted;
    }
}
