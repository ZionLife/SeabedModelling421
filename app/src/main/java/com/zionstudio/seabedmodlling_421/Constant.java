package com.zionstudio.seabedmodlling_421;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

public class Constant {
    public static float[][] highArrs;
    public static float HIGH_BASE = 2f;
    public static float HIGHEST = 80f;
    public static String sHeightmapPath;
    public static String sGrassPath;
    public static String sRockPath;
    public static int minX = 0;
    public static int minY = 0;
    public static int maxX;
    public static int maxY; //

    public static float[][] getLand(Resources res, int index) {
//        Bitmap bt = BitmapFactory.decodeResource(res, index);
        Log.i("图片地址", "" + sHeightmapPath);
        Bitmap bt = BitmapFactory.decodeFile(sHeightmapPath);
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

    public static float[][] getLand(String fileName, int index) {
//        Bitmap bt = BitmapFactory.decodeResource(res, index);
        Bitmap bt = BitmapFactory.decodeFile(fileName);
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

    public static void reset() {
        highArrs = null;
        HIGH_BASE = 2f;
        HIGHEST = 80f;
        sHeightmapPath = null;
        sGrassPath = null;
        sRockPath = null;
    }
}