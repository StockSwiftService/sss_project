package org.example.stockswiftservice.global.util;

import java.util.Random;

public class ColorUtil {

    public static String generateRandomColor() {
        Random random = new Random();
        int rgb = random.nextInt(0xFFFFFF + 1);
        return String.format("#%06X", rgb);
    }
}
