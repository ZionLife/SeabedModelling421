package com.zionstudio.seabedmodlling_421;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.zionstudio.seabedmodlling_421.Constant.loadLandforms;
import static com.zionstudio.seabedmodlling_421.Constant.yArray;

/**
 * Created by QiuXi'an on 2018/4/22 0022.
 * Email Zionlife1025@163.com
 */

public class MyTestRenderer implements GLSurfaceView.Renderer {
    static float fixedCx = 100;
    static float fixedCy = 70;
    static float fixedCz = -220;

    static float tx = 0;//观察目标点x坐标
    static float tz = 0;//观察目标点z坐标

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    Cube mCube;
    Mountain mMountain;

    GLSurfaceView mView;

    //山的纹理id
    int mountainId;
    int rockId;

    public MyTestRenderer(GLSurfaceView view) {
        mView = view;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //开启深度测试
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        mCube = new Cube(mView);

        MatrixState.setInitStack();
        yArray = loadLandforms(mView.getResources(), R.mipmap.land);
        mMountain = new Mountain(mView, yArray, yArray.length - 1, yArray[0].length - 1);

        //初始化纹理
        mountainId = initTexture(R.mipmap.grass);
        rockId = initTexture(R.mipmap.rock_01);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //绘制地形的相关代码
        //设置视窗大小及位置
        GLES30.glViewport(0, 0, width, height);
        //计算GLSurfaceView的宽高比
        float ratio = (float) width / height;
        //调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 1000);
        //调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(fixedCx, fixedCy, fixedCz, tx, 1, tz, 0, 1, 0);

//        //计算宽高比
//        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

        MatrixState.pushMatrix();
        mCube.drawCube(mMVPMatrix);
        mMountain.draw(mountainId, rockId, gl);
        MatrixState.popMatrix();

    }

    //生成纹理Id的方法
    public int initTexture(int drawableId) {
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
        InputStream is = mView.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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