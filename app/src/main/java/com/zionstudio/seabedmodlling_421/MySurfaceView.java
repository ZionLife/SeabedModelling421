package com.zionstudio.seabedmodlling_421;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import static com.zionstudio.seabedmodlling_421.MainActivity.HEIGHT;
import static com.zionstudio.seabedmodlling_421.MainActivity.WIDTH;

public class MySurfaceView extends GLSurfaceView {
    private final String TAG = getClass().getSimpleName();
    static float direction = 0;//视线方向

    static float cx = 100;
    static float cy = 70;
    static float cz = -220;

    static float fixedCx = 100;
    static float fixedCy = 100;
    static float fixedCz = -220;

    static float tx = 0;//观察目标点x坐标
    static float tz = 0;//观察目标点z坐标
    static final float DEGREE_SPAN = (float) (3.0 / 180.0f * Math.PI);//摄像机每次转动的角度
    //线程循环的标志位
    boolean flag = true;
    float x;
    float y;
    float Offset = 20;
    Renderer mRender;
    float preX;
    float preY;

    boolean allowRotate = true;  //是否允许旋转视角

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
//        mRender = new SceneRenderer();    //创建场景渲染器

        mRender = new MyTestRenderer(this);
        setRenderer(mRender);                //设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }

    public void clear() {
        Log.i(TAG, "收到消息");
        ((MyTestRenderer) mRender).clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!allowRotate) {
            return true;
        }
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = true;
                new Thread() {
                    @Override
                    public void run() {
                        while (flag) {
                            if (x > 0 && x < WIDTH / 2 && y > 0 && y < HEIGHT / 2) {//向前
                                cx = cx - (float) Math.sin(direction) * 1.0f;
                                cz = cz - (float) Math.cos(direction) * 1.0f;
                            } else if (x > WIDTH / 2 && x < WIDTH && y > 0 && y < HEIGHT / 2) {//向后
                                cx = cx + (float) Math.sin(direction) * 1.0f;
                                cz = cz + (float) Math.cos(direction) * 1.0f;
                            } else if (x > 0 && x < WIDTH / 2 && y > HEIGHT / 2 && y < HEIGHT) {
                                direction = direction + DEGREE_SPAN;
                            } else if (x > WIDTH / 2 && x < WIDTH && y > HEIGHT / 2 && y < HEIGHT) {
                                direction = direction - DEGREE_SPAN;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                break;
            case MotionEvent.ACTION_UP:
                flag = false;
                break;
        }

//        //设置新的观察目标点XZ坐标
//        tx = (float) (cx - Math.sin(direction) * Offset);//观察目标点x坐标
//        tz = (float) (cz - Math.cos(direction) * Offset);//观察目标点z坐标

        //设置新的摄像机位置
        MatrixState.setCamera(cx, cy, cz, tx, 1, tz, 0, 1, 0);
        return true;
    }

    private void setCameraToInit() {
        MatrixState.setCamera(fixedCx, fixedCy, fixedCz, tx, 1, tz, 0, 1, 0);
    }
}