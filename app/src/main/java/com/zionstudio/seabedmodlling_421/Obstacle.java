package com.zionstudio.seabedmodlling_421;

import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by QiuXi'an on 2018/3/8 0008.
 * Email Zionlife1025@163.com
 */

public class Obstacle {
    float r = 1f;
    // 定义立方体的8个顶点
    float[] cubeVertices = {
            //左面
            -r, r, r,
            -r, -r, r,
            -r, r, -r,
            -r, -r, -r,

            //右面
            r, r, r,
            r, -r, r,
            r, -r, -r,
            r, r, -r,

            //前面
            -r, r, r,
            -r, -r, r,
            r, -r, r,
            r, r, r,

            //后面
            r, -r, -r,
            r, r, -r,
            -r, r, -r,
            -r, -r, -r,

            //上面
            -r, r, r,
            r, r, r,
            r, r, -r,
            -r, r, -r,

            //下面
            -r, -r, r,
            r, -r, r,
            r, -r, -r,
            -r, -r, -r
    };
    //  颜色数组
    float[] cubeColors = {
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,

            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,

            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,

            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,


            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,

            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 0f, 1f,
    };
    //索引数组
    private short[] indices = {
            0, 1, 2,
            0, 2, 3,

            4, 5, 6,
            4, 6, 7,

            8, 9, 10,
            8, 10, 11,

            12, 13, 14,
            12, 14, 15,

            16, 17, 18,
            16, 18, 19,

            20, 21, 22,
            20, 22, 23,
    };


    // 控制旋转的角度
    private float rotate;

    FloatBuffer VerticesBuffer;
    FloatBuffer Colorbuffer;

    ShortBuffer indexbuffer;

    public Obstacle(float r) {
        this.r = r;
        init();
    }

    private void init() {
        //获取浮点形缓冲数据
        VerticesBuffer = Utils.getFloatBuffer(cubeVertices);
        //获取浮点型颜色数据
        Colorbuffer = Utils.getFloatBuffer(cubeColors);
        //获取浮点型索引数据
        indexbuffer = Utils.getShortBuffer(indices);
    }

    public void draw(GL10 gl) {
        Log.i("obstacle", "obstacle draw");
        // 启用顶点座标数据
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // 启用顶点颜色数据
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        // 重置当前的模型视图矩阵
        gl.glLoadIdentity();
        // 沿着Y轴旋转
        gl.glRotatef(rotate, 0f, 1f, 0f);

        // 设置顶点的位置数据
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, VerticesBuffer);

        // 设置顶点的颜色数据
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, Colorbuffer);

        //绘制三角形
        // gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP  , 0, 24);
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexbuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        // 旋转角度增加1
        rotate -= 1;
    }
}
