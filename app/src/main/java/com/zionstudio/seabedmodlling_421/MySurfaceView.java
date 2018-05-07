package com.zionstudio.seabedmodlling_421;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.zionstudio.seabedmodlling_421.Constant.loadLandforms;
import static com.zionstudio.seabedmodlling_421.Constant.yArray;
import static com.zionstudio.seabedmodlling_421.MainActivity.HEIGHT;
import static com.zionstudio.seabedmodlling_421.MainActivity.WIDTH;

public class MySurfaceView extends GLSurfaceView {
    private final String TAG = getClass().getSimpleName();
    static float direction = 0;//视线方向
//    static float cx=0;//摄像机x坐标
//    static float cz=20;//摄像机z坐标

//    static float cx = 0;
//    static float cy = 70;
//    static float cz = 20;

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

//    //生成纹理Id的方法
//    public int initTexture(int drawableId) {
//        //生成纹理ID
//        int[] textures = new int[1];
//        GLES30.glGenTextures
//                (
//                        1,          //产生的纹理id的数量
//                        textures,   //纹理id的数组
//                        0           //偏移量
//                );
//        int textureId = textures[0];
//        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);    //绑定纹理
//        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER,
//                GLES30.GL_LINEAR_MIPMAP_LINEAR);        //使用MipMap线性纹理采样
//        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,
//                GLES30.GL_LINEAR_MIPMAP_NEAREST);        //使用MipMap最近点纹理采样
//        //ST方向纹理拉伸方式
//        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
//        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);
//
//        //通过输入流加载图片
//        InputStream is = this.getResources().openRawResource(drawableId);
//        Bitmap bitmapTmp;
//        try {
//            bitmapTmp = BitmapFactory.decodeStream(is);
//        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //实际加载纹理
//        GLUtils.texImage2D
//                (
//                        GLES30.GL_TEXTURE_2D,   //纹理类型
//                        0,                      //纹理的层次，0表示基本图像层，可以理解为直接贴图
//                        bitmapTmp,              //纹理图像
//                        0                      //纹理边框尺寸
//                );
//        //自动生成Mipmap纹理
//        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
//        //释放纹理图
//        bitmapTmp.recycle();
//        //返回纹理ID
//        return textureId;
//    }
}