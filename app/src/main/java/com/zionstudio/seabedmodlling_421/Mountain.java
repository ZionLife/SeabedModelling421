package com.zionstudio.seabedmodlling_421;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

public class Mountain {
    //单位长度
    float UNIT_SIZE = 3.0f;

    //自定义渲染管线的id
    int mProgram;
    //总变化矩阵引用的id
    int muMVPMatrixHandle;
    //顶点位置属性引用id
    int maPositionHandle;
    //顶点纹理坐标属性引用id
    int maTexCoorHandle;

    //草地的id
    int sTextureGrassHandle;
    //石头的id
    int sTextureRockHandle;
    //起始x值
    int landStartYYHandle;
    //长度
    int landYSpanHandle;

    //顶点数据缓冲和纹理坐标数据缓冲
    FloatBuffer mVertexBuffer;
    FloatBuffer mTexCoorBuffer;
    //顶点数量
    int vCount = 0;
    Obstacle mObstacle;

    boolean needDrawLand = false;

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

    public Mountain(GLSurfaceView mv, float[][] yArray, int rows, int cols) {
        initObstacle();
        initVertexData(yArray, rows, cols);
        initShader(mv);
    }

    private void initObstacle() {
        mObstacle = new Obstacle(1f);
//        //获取浮点形缓冲数据
//        VerticesBuffer = Utils.getFloatBuffer(cubeVertices);
//        //获取浮点型颜色数据
//        Colorbuffer = Utils.getFloatBuffer(cubeColors);
//        //获取浮点型索引数据
//        indexbuffer = Utils.getShortBuffer(indices);
    }

    //初始化地形顶点数据
    public void initVertexData(float[][] yArray, int rows, int cols) {
        //顶点坐标数据的初始化
        vCount = cols * rows * 2 * 3;//每个格子两个三角形，每个三角形3个顶点
        float vertices[] = new float[vCount * 3];//每个顶点xyz三个坐标
        int count = 0;//顶点计数器
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                //计算当前格子左上侧点坐标
                float zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE;
                float zsz = -UNIT_SIZE * rows / 2 + j * UNIT_SIZE;

                vertices[count++] = zsx;
                vertices[count++] = yArray[j][i];
                vertices[count++] = zsz;

                vertices[count++] = zsx;
                vertices[count++] = yArray[j + 1][i];
                vertices[count++] = zsz + UNIT_SIZE;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = yArray[j][i + 1];
                vertices[count++] = zsz;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = yArray[j][i + 1];
                vertices[count++] = zsz;

                vertices[count++] = zsx;
                vertices[count++] = yArray[j + 1][i];
                vertices[count++] = zsz + UNIT_SIZE;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = yArray[j + 1][i + 1];
                vertices[count++] = zsz + UNIT_SIZE;
            }
        }

        //创建顶点坐标数据缓冲
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置

        //顶点纹理坐标数据的初始化
        float[] texCoor = generateTexCoor(cols, rows);
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
    }

    //初始化着色器的方法
    public void initShader(GLSurfaceView mv) {
//        String mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
//        String mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());

        String mVertexShader = ShaderUtil.readTextFileFromResource(MyApplication.mContext, R.raw.vertex);
        String mFragmentShader = ShaderUtil.readTextFileFromResource(MyApplication.mContext, R.raw.frag);

        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用id
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");

        //纹理
        //草地
        sTextureGrassHandle = GLES30.glGetUniformLocation(mProgram, "sTextureGrass");
        //石头
        sTextureRockHandle = GLES30.glGetUniformLocation(mProgram, "sTextureRock");
        //x位置
        landStartYYHandle = GLES30.glGetUniformLocation(mProgram, "landStartY");
        //x最大
        landYSpanHandle = GLES30.glGetUniformLocation(mProgram, "landYSpan");
    }

    public void draw(int texId, int rock_textId, GL10 gl) {
//        mObstacle.draw(gl); //绘制立方体障碍物
        //指定使用某套着色器程序
        GLES30.glUseProgram(mProgram);
        //将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将顶点位置数据送入渲染管线
        GLES30.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES30.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer
                );
        //将纹理坐标数据传入渲染管线
        GLES30.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES30.GL_FLOAT,
                        false,
                        2 * 4,
                        mTexCoorBuffer
                );
        //启用顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        //启用纹理坐标数据数组
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);

        //绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, rock_textId);
        GLES30.glUniform1i(sTextureGrassHandle, 0);//使用0号纹理
        GLES30.glUniform1i(sTextureRockHandle, 1); //使用1号纹理

        GLES30.glUniform1f(landStartYYHandle, 0);
        GLES30.glUniform1f(landYSpanHandle, 50);

        //绘制纹理矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);

    }

    //自动切分纹理产生纹理数组的方法
    public float[] generateTexCoor(int bw, int bh) {
        float[] result = new float[bw * bh * 6 * 2];
        float sizew = 16.0f / bw;//列数
        float sizeh = 16.0f / bh;//行数
        int c = 0;
        for (int i = 0; i < bh; i++) {
            for (int j = 0; j < bw; j++) {
                //每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
                float s = j * sizew;
                float t = i * sizeh;

                result[c++] = s;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t;

                result[c++] = s + sizew;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t + sizeh;
            }
        }
        return result;
    }
}