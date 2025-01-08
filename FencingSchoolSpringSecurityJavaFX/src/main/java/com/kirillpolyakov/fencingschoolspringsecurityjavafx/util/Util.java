package com.kirillpolyakov.fencingschoolspringsecurityjavafx.util;

import java.util.Arrays;

public class Util {

    public static boolean notDigitsOrLetters(String s) {
        return Arrays.stream(s.split("")).anyMatch(x -> !Character.isLetterOrDigit(x.charAt(0)));
    }

    public static boolean notLetters(String s) {
        return Arrays.stream(s.split("")).anyMatch(x -> !Character.isLetter(x.charAt(0)));
    }

    public static boolean notDigits(String s) {
        return Arrays.stream(s.split("")).anyMatch(x -> !Character.isDigit(x.charAt(0)));
    }
}
