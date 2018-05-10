package com.zionstudio.seabedmodlling_421;

/**
 * @Description: Created by QiuXi'an on 2018/5/8.
 */
public class Line {
    int UNIT_SIZE = 2;
    int vCount = 4; //因为是4个顶点
    float vertices[] = new float[]
            {
                    -4 * UNIT_SIZE, 0,
                    0, 0, -4 * UNIT_SIZE, 0,
                    4 * UNIT_SIZE, 0, 0,
                    0, 4 * UNIT_SIZE, 0
            };

    float colors[] = new float[]
            {
                    1, 1, 1, 0,
                    1, 1, 1, 0,
                    1, 1, 1, 0,
                    1, 1, 1, 0
            };

    public Line() {

    }
}
