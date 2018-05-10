package com.zionstudio.seabedmodlling_421;

import android.opengl.GLES30;
import android.util.Log;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.zionstudio.seabedmodlling_421.ShaderUtil.loadShader;

/**
 * Created by QiuXi'an on 2018/4/22 0022.
 * Email Zionlife1025@163.com
 */

public class Cube {
    private String TAG = getClass().getSimpleName();
    private float r = 10f; //立方体边长的一半

    public static float x = 0;
    public static float z = 0;
    public static final float y = 50;

    private FloatBuffer vertexBuffer, colorBuffer;
    private ShortBuffer indexBuffer;
    private String vertexShaderCode;
    private String fragmentShaderCode;
    private int mProgram;

    final int COORDS_PER_VERTEX = 3;
//    final float cubePositions[] = {
//            -r, r, r,    //正面左上0
//            -r, -r, r,   //正面左下1
//            r, -r, r,    //正面右下2
//            r, r, r,     //正面右上3
//            -r, r, -r,    //反面左上4
//            -r, -r, -r,   //反面左下5
//            r, -r, -r,    //反面右下6
//            r, r, -r,     //反面右上7
//    };

    final float cubePositions[] = {
            x - r, y + r, z + r,    //正面左上0
            x - r, y - r, z + r,   //正面左下1
            x + r, y - r, z + r,    //正面右下2
            x + r, y + r, z + r,     //正面右上3
            x - r, y + r, z - r,    //反面左上4
            x - r, y - r, z - r,   //反面左下5
            x + r, y - r, z - r,    //反面右下6
            x + r, y + r, z - r,     //反面右上7
    };

    final short index[] = {
            6, 7, 4, 6, 4, 5,    //后面
            6, 3, 7, 6, 2, 3,    //右面
            6, 5, 1, 6, 1, 2,    //下面
            0, 3, 2, 0, 2, 1,    //正面
            0, 1, 5, 0, 5, 4,    //左面
            0, 7, 3, 0, 4, 7,    //上面
    };

    float color[] = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };

    private int mPositionHandle;
    private int mColorHandle;

    private int mMatrixHandler;

    //顶点个数
    private final int vertexCount = cubePositions.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节


    public Cube(View mView) {
//        setBuffer();
        vertexShaderCode = ShaderUtil.readTextFileFromResource(MyApplication.mContext, R.raw.vertex_cube);
        fragmentShaderCode = ShaderUtil.readTextFileFromResource(MyApplication.mContext, R.raw.frag_cube);


        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        //创建一个空的OpenGLES程序
        mProgram = GLES30.glCreateProgram();
        //将顶点着色器加入到程序
        GLES30.glAttachShader(mProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES30.glAttachShader(mProgram, fragmentShader);
        //连接到着色器程序
        GLES30.glLinkProgram(mProgram);
    }

    public void setBuffer() {

//        vertexBuffer.clear();
//        colorBuffer.clear();
//        indexBuffer.clear();
        Log.i("测试异步", "setBuffer：" + "x：" + x + "； z:" + z);
        ByteBuffer bb = ByteBuffer.allocateDirect(
                cubePositions.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(cubePositions);
        vertexBuffer.position(0);

        ByteBuffer dd = ByteBuffer.allocateDirect(
                color.length * 4);
        dd.order(ByteOrder.nativeOrder());
        colorBuffer = dd.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        ByteBuffer cc = ByteBuffer.allocateDirect(index.length * 2);
        cc.order(ByteOrder.nativeOrder());
        indexBuffer = cc.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);
    }

    public void drawCube(float[] mMVPMatrix) {
        Log.i("测试异步", "drawCube");
        //将程序加入到OpenGLES3.0环境
        GLES30.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES30.glGetUniformLocation(mProgram, "vMatrix");
        //指定vMatrix的值
        GLES30.glUniformMatrix4fv(mMatrixHandler, 1, false, MatrixState.getFinalMatrix(), 0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES30.glVertexAttribPointer(mPositionHandle, 3,
                GLES30.GL_FLOAT, false,
                0, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
        //设置绘制三角形的颜色
//        GLES20.glUniform4fv(mColorHandle, a, color, 0);
        GLES30.glEnableVertexAttribArray(mColorHandle);
        GLES30.glVertexAttribPointer(mColorHandle, 4,
                GLES30.GL_FLOAT, false,
                0, colorBuffer);
        //索引法绘制正方体
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, index.length, GLES30.GL_UNSIGNED_SHORT, indexBuffer);
        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(mPositionHandle);
    }
}

