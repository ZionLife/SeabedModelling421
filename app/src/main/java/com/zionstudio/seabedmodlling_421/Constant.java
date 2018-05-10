package com.zionstudio.seabedmodlling_421;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Constant {
    public static float[][] highArrs;
    public static final float HIGH_BASE = 2f;
    public static final float HIGHEST = 80f;
    public static int minX = 0;
    public static int minY = 0;
    public static int maxX;
    public static int maxY; //

    public static float[][] getLand(Resources res, int index) {
        Bitmap bt = BitmapFactory.decodeResource(res, index);
        int colsPlusOne = bt.getWidth();
        int rowsPlusOne = bt.getHeight();
        maxX = colsPlusOne - 1;
        maxY = rowsPlusOne - 1;

        float[][] result = new float[rowsPlusOne][colsPlusOne];
        for (int i = 0; i < rowsPlusOne; i++) {
            for (int j = 0; j < colsPlusOne; j++) {
                int color = bt.getPixel(j, i);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int h = (r + g + b) / 3;
//                result[i][j] = h * HIGHEST / 255 + HIGH_BASE;
                result[i][j] = r * HIGHEST / 255 + HIGH_BASE;
            }
        }
        return result;
    }
}