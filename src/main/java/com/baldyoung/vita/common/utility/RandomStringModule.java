package com.baldyoung.vita.common.utility;

import java.util.Random;

/**
 * 随机字符串生成模块
 */
public class RandomStringModule {
    private static String defaultDictionary = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int defaultLength = 3;

    public static String getRandomString(String dictionary, int length) {
        if (null == dictionary || 0 == dictionary.length()) {
            dictionary = RandomStringModule.defaultDictionary;
        }
        Random random = new Random();
        char[] chars = new char[length];
        int k;
        for (int i=0; i<length; i++) {
            k = random.nextInt(dictionary.length());
            chars[i] = dictionary.charAt(k);
        }
        return String.valueOf(chars);
    }

    public static String getRandomString(String dictionary) {
        return getRandomString(dictionary, RandomStringModule.defaultLength);
    }

    public static String getRandomString(int length) {
        return getRandomString(defaultDictionary, length);
    }

    public static String getRandomString() {
        return getRandomString(defaultDictionary, defaultLength);
    }
}
