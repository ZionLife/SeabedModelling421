package com.zionstudio.seabedmodlling_421;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.zionstudio.seabedmodlling_421.Constant.getLand;
import static com.zionstudio.seabedmodlling_421.Constant.highArrs;

/**
 * Created by QiuXi'an on 2018/4/22 0022.
 * Email Zionlife1025@163.com
 */

public class MyTestRenderer implements GLSurfaceView.Renderer {
    private final String TAG = getClass().getSimpleName();
    static float fixedCx = 100;
    static float fixedCy = 70;
    static float fixedCz = -220;

    static float tx = 0;//观察目标点x坐标
    static float tz = 0;//观察目标点z坐标

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    Cube mCube;
    Land mLand;

    GLSurfaceView mGLView;
    MapView mMapView;
    //山的纹理id
    int grassId;
    int rockId;
    private boolean mDrawCube = false;

    public MyTestRenderer(GLSurfaceView view) {
        mGLView = view;
    }

    public MyTestRenderer(GLSurfaceView view, MapView mv) {
        mGLView = view;
        mMapView = mv;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        //开启深度测试
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        mCube = new Cube(mGLView);

        MatrixUtils.setInitStack();
        highArrs = getLand(mGLView.getResources(), R.mipmap.land8);
//        if (!getLand(mGLView.getResources(), R.mipmap.land8)) {
////            Toast.makeText(mGLView.getContext(), "图片过大，内存溢出", Toast.LENGTH_LONG).show();
////            ((Activity) mGLView.getContext()).finish();
//        }
        mLand = new Land(mGLView, highArrs, highArrs.length - 1, highArrs[0].length - 1);

        //初始化纹理
        grassId = initTexture(Constant.sGrassPath);
        rockId = initTexture(Constant.sRockPath);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
        //设置Viewport显示窗口的大小
        GLES30.glViewport(0, 0, width, height);
        //计算宽高比
        float ratio = (float) width / height;
        //根据屏幕宽高比计算透视投影矩阵
        MatrixUtils.setProjectFrustum(-ratio, ratio, -1, 1, 1, 1000);
        //生成摄像机位置矩阵
        MatrixUtils.setCamera(fixedCx, fixedCy, fixedCz, tx, 1, tz, 0, 1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        Log.i("Zion", "onDrawFrame");
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        MatrixUtils.pushMatrix();
        mLand.draw(grassId, rockId, gl);
        if (mDrawCube == true) {
            mCube.drawCube(mMVPMatrix);
        }
        MatrixUtils.popMatrix();

    }

    /**
     * 点击确定按钮后调用此方法
     */
    public void clear() {
        Log.i(TAG, "执行重绘");
        mDrawCube = true;
//        mCube = new Cube(mGLView);
        mCube.setBuffer();
//        mCube.setBuffer();
    }

    //生成纹理Id的方法
    public int initTexture(String path) {
        //生成纹理ID
        int[] textures = new int[1];
        GLES30.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId = textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);    //绑定纹理
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER,
                GLES30.GL_LINEAR_MIPMAP_LINEAR);        //使用MipMap线性纹理采样
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_LINEAR_MIPMAP_NEAREST);        //使用MipMap最近点纹理采样
        //ST方向纹理拉伸方式
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);

        //通过输入流加载图片
//        InputStream is = mGLView.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        bitmapTmp = BitmapFactory.decodeFile(path);
        //实际加载纹理
        GLUtils.texImage2D
                (
                        GLES30.GL_TEXTURE_2D,   //纹理类型
                        0,                      //纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmapTmp,              //纹理图像
                        0                      //纹理边框尺寸
                );
        //自动生成Mipmap纹理
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        //释放纹理图
        bitmapTmp.recycle();
        //返回纹理ID
        return textureId;
    }
}
