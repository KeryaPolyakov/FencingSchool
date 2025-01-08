package com.kirillpolyakov.fencingschoolspringsecurityjavafx.util;

import java.util.List;
import java.util.Map;

public class Constants {

    public static final int MINUTES = 90;

    public static final String URL = "http://localhost:8080/";

    public static final String PREFERENCE_KEY_ID = "id";
    public static final String PREFERENCE_KEY_USERNAME = "username";
    public static final String PREFERENCE_KEY_PASSWORD = "password";

    public static Map<String, String> DAYS = Map.of("понедельник", "monday", "вторник", "tuesday",
            "среда", "wednesday", "четверг", "thursday", "пятница", "friday", "суббота",
            "saturday", "воскресенье", "sunday");

    public static final List<String> SORTED_DAYS = List.of("понедельник", "вторник", "среда", "четверг",
            "пятница", "суббота", "воскресенье");

    public static final List<String> TIME_START = List.of("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00",
            "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30");

    public static final List<String> TIME_END = List.of("09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00",
            "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00");

    public static final List<Integer> NUMBERS_GYM = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);


}
