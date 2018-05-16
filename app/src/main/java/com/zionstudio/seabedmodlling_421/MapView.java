package com.zionstudio.seabedmodlling_421;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @Description: Created by QiuXi'an on 2018/5/14.
 */
public class MapView extends View {
    private Paint mPaint;
    private Paint mRedPaint; //绘制红点的paint
    private float bmpWidth;
    private float bmpHeight;
    private static float centerX;
    private static float centerY;
    private float mRadius;
    private Context mContext;
    private Bitmap mBitmap;
    private String TAG = getClass().getSimpleName();

    private boolean mDrawRedPoint = false;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mRedPaint = new Paint();
        mRedPaint.setColor(getResources().getColor(R.color.redPaint));
        mRedPaint.setStrokeWidth(5);
        mBitmap = BitmapFactory.decodeFile(Constant.sHeightmapPath);
        if (mBitmap.getWidth() < 100 || mBitmap.getHeight() < 100) {
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * 3, mBitmap.getHeight() * 3, false);
        } else if (mBitmap.getWidth() < 150 || mBitmap.getHeight() < 150) {
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * 2, mBitmap.getHeight() * 2, false);
        }
//        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.land8);
        centerX = mBitmap.getWidth() / 2;
        centerY = mBitmap.getHeight() / 2;
        mRadius = 15;
        bmpWidth = mBitmap.getWidth();
        bmpHeight = mBitmap.getHeight();

        Log.i(TAG, "bmpWidth: " + bmpWidth + "; bmpHeight: " + bmpHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) bmpWidth, (int) bmpHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        if (mDrawRedPoint) {
            canvas.drawCircle(centerX, centerY, mRadius, mRedPaint);
        }
    }

    public void setCenter(float x, float y) {
//        centerX = x;
//        centerY = y;

        if (!mDrawRedPoint) {
            mDrawRedPoint = true;
        }

        float xRatio = (x + Constant.xSum) / (Constant.xSum * 2);
        float yRatio = (y + Constant.ySum) / (Constant.ySum * 2);

        centerX = xRatio * bmpWidth;
        centerY = yRatio * bmpHeight;

        Log.i(TAG, "xRatio: " + xRatio + "; yRatio: " + yRatio);
        Log.i(TAG, "centerX: " + centerX + "; centerY: " + centerY);
        invalidate();
    }
}
