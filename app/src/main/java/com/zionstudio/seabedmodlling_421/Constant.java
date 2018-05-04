package com.zionstudio.seabedmodlling_421;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Constant {
    public static float[][] yArray;
    public static final float LAND_HIGH_ADJUST = 2f;
    public static final float LAND_HIGHEST = 60f;
    public static int minX = 0;
    public static int minY = 0;
    public static int maxX;
    public static int maxY; //

    public static float[][] loadLandforms(Resources resources, int index) {
        Bitmap bt = BitmapFactory.decodeResource(resources, index);
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
                result[i][j] = h * LAND_HIGHEST / 255 + LAND_HIGH_ADJUST;
            }
        }
        return result;
    }
}