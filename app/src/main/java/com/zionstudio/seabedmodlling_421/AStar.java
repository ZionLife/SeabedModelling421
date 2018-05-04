package com.zionstudio.seabedmodlling_421;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiuXi'an on 2018/4/27.
 */
public class AStar {
    private final byte G_OFFSET = 1; // 每个图块G值的增加值
    private final byte canMoveIndex = 1; // 阻挡
    private int gridWidth = 0; // 地图行数
    private int gridHeight = 0; // 地图列数
    private byte[][] map; // 地图数据

    public AStar(byte[][] grid) {
        this.map = grid;
        this.gridWidth = grid[0].length;
        this.gridHeight = grid.length;
        // this.gridWidth=grid.length;
        // this.gridHeight=grid[0].length;
    }

    public byte[][] getMap() {
        return map;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    /**
     * 寻路
     *
     * @param
     * @return
     */
    public final int[][] getPath(int startX, int startY, int endX, int endY) {
        if (!isNotObstruct(startX, startY) || !isNotObstruct(endX, endY)) {
            return null;
        }

        // long tartTime = System.currentTimeMillis();
        List<MGE_Node> path = searchPath(startX, startY, endX, endY);
        if (path == null) {
            // System.out.println("寻路用时:" + (System.currentTimeMillis() -
            // tartTime) + "<" + startX + "." + startY + ">" + "<" + endX + "."
            // + endY + ">");
            return null;
        }

        int[][] way = new int[path.size()][2];
        int i = 0;
        for (MGE_Node node : path) {
            way[i][0] = node.x;
            way[i++][1] = node.y;
        }
        // System.out.println("寻路用时:" + (System.currentTimeMillis() - tartTime)
        // + "<" + startX + "." + startY + ">" + "<" + endX + "." + endY + ">");
        return way;
    }

    private final List<MGE_Node> searchPath(int startX, int startY, int endX, int endY) {
        List<MGE_Node> closeNode = new ArrayList<MGE_Node>(); // 节点关闭列表
        List<MGE_Node> openNode = new ArrayList<MGE_Node>(); // 节点开启别表

        MGE_Node startNode = new MGE_Node();
        startNode.x = startX;
        startNode.y = startY;
        startNode.g = 0;
        startNode.h = getH(startNode.x, startNode.y, endX, endY);
        startNode.f = startNode.g + startNode.h;
        openNode.add(startNode);
        startNode = null;

        MGE_Node bestNode = null;
        ;
        boolean isSuccess = false;
        while (true) {
            bestNode = this.getBesetNode(openNode);
            if (bestNode == null) { // 未找到路径
                return null;
            } else if (bestNode.x == endX && bestNode.y == endY) {
                isSuccess = true;
                break;
            } else {
                seachSeccessionNode1(bestNode, openNode, closeNode, endX, endY);
            }
        }

        if (isSuccess) {
            List<MGE_Node> path = new ArrayList<MGE_Node>();
            MGE_Node _Node = bestNode;
            while (_Node.parent != null) {
                path.add(0, _Node);
                _Node = _Node.parent;
            }

            if (path.isEmpty()) {
                return null;
            } else {
                path.remove(0);
                return path;
            }
        } else {
            return null;
        }
    }

    /**
     * 根据传入的节点生成子节点
     */
    private void seachSeccessionNode1(MGE_Node bestNode, List<MGE_Node> openNode, List<MGE_Node> closeNode, int endX, int endY) {
        int x, y;
        // 左部节点
        if (isNotObstruct(x = bestNode.x - 1, y = bestNode.y)) {
            creatSeccessionNode(bestNode, x, y, openNode, closeNode, endX, endY);
        }
        // 右部节点
        if (isNotObstruct(x = bestNode.x + 1, y = bestNode.y)) {
            creatSeccessionNode(bestNode, x, y, openNode, closeNode, endX, endY);
        }
        // 上部节点
        if (isNotObstruct(x = bestNode.x, y = bestNode.y - 1)) {
            creatSeccessionNode(bestNode, x, y, openNode, closeNode, endX, endY);
        }
        // 下部节点
        if (isNotObstruct(x = bestNode.x, y = bestNode.y + 1)) {
            creatSeccessionNode(bestNode, x, y, openNode, closeNode, endX, endY);
        }
        closeNode.add(bestNode);
        for (int i = 0; i < openNode.size(); i++) {
            if (openNode.get(i).x == bestNode.x && openNode.get(i).y == bestNode.y) {
                openNode.remove(i);
                break;
            }
        }
    }

    private void creatSeccessionNode(MGE_Node bestNode, int x, int y, List<MGE_Node> openNode, List<MGE_Node> closeNode, int endX, int endY) {
        MGE_Node oldNode = null;
        int g = bestNode.g + G_OFFSET;
        if (!isInClose(x, y, closeNode)) {
            if ((oldNode = checkOpen(x, y, openNode)) != null) {
                if (oldNode.g < g) {
                    oldNode.parent = bestNode;
                    oldNode.g = g;
                    oldNode.f = g + oldNode.h;
                }
            } else {
                MGE_Node node = new MGE_Node();
                node.parent = bestNode;
                node.g = g;
                node.h = getH(x, y, endX, endY);
                node.f = node.g + node.h;
                node.x = x;
                node.y = y;
                openNode.add(node);
            }
        }
    }

    private MGE_Node checkOpen(int x, int y, List<MGE_Node> openNode) {
        for (MGE_Node node : openNode) {
            if (node.x == x && node.y == y) {
                return node;
            }
        }
        return null;
    }

    private boolean isInClose(int x, int y, List<MGE_Node> closeNode) {
        for (MGE_Node node : closeNode) {
            if (node.x == x && node.y == y) {
                return true;
            }
        }
        return false;
    }

    // 得到最优节点
    private MGE_Node getBesetNode(List<MGE_Node> openNode) {
        MGE_Node bestNode = null;
        int f = Integer.MAX_VALUE;
        for (MGE_Node node : openNode) {
            if (node.f < f) {
                f = node.f;
                bestNode = node;
            }
        }
        return bestNode;
    }

    // 得到该图块的H值
    private int getH(int x, int y, int endX, int endY) {
        return (Math.abs(endX - x) + Math.abs(endY - y));
    }

    /**
     * 查看该位置是否有阻挡
     *
     * @return true无阻挡
     */
    public boolean isNotObstruct(int x, int y) {
        if (x < 0 || x >= gridWidth) {
            return false;
        } else if (y < 0 || y >= gridHeight) {
            return false;
        } else {
            return (map[y][x] != canMoveIndex);
        }
    }

    /**
     * 点是否在地图内
     *
     * @return true:在地图内
     */
    public boolean isInMap(int x, int y) {
        if (x < 0 || x >= gridWidth) {
            return false;
        } else if (y < 0 || y >= gridHeight) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取一条直线
     *
     * @param x         起点坐标
     * @param y         起点坐标
     * @param step      步长
     * @param direction 方向(0:上 )
     * @return
     */
    public int[][] getLinePath(int x, int y, int step, int direction) {

        int nowX = x;
        int nowY = y;

        int[][] path = new int[step + 1][2];
        path[0][0] = x;
        path[0][1] = y;

        int i = 1;
        myWhile:
        while (step-- > 0) {
            switch (direction) {
                case 0:
                    if (isNotObstruct(nowX + 1, nowY - 1)) {
                        nowX++;
                        nowY--;
                        break;
                    }
                case 1:
                    if (isNotObstruct(nowX + 1, nowY)) {
                        nowX++;
                        break;
                    }
                case 2:
                    if (isNotObstruct(nowX + 1, nowY + 1)) {
                        nowX++;
                        nowY++;
                        break;
                    }
                case 3:
                    if (isNotObstruct(nowX, nowY + 1)) {
                        nowY++;
                        break;
                    }
                case 4:
                    if (isNotObstruct(nowX - 1, nowY + 1)) {
                        nowX--;
                        nowY++;
                        break;
                    }
                case 5:
                    if (isNotObstruct(nowX - 1, nowY)) {
                        nowX--;
                        break;
                    }
                case 6:
                    if (isNotObstruct(nowX - 1, nowY - 1)) {
                        nowX--;
                        nowY--;
                        break;
                    }
                case 7:
                    if (isNotObstruct(nowX, nowY - 1)) {
                        nowY--;
                        break;
                    }
                default:
                    break myWhile;
            }
            path[i][0] = nowX;
            path[i++][1] = nowY;
        }

        if (i < path.length) {
            int[][] temp = new int[i][2];
            System.arraycopy(path, 0, temp, 0, i);
            path = temp;
        }
        return path;
    }

}

/**
 * 节点类
 */
class MGE_Node {
    int f; // 该节点路径评分
    int g; // 从起始点到该节点的预估距离
    int h; // 从该节点到终点的曼哈顿距离（忽略障碍水平垂直移动到终点的距离）
    int x; // 该节点所在行
    int y; // 该节点所在列

    MGE_Node parent; // 该节点的父节点
    MGE_Node[] child = new MGE_Node[8]; // 该节点的子节点，最多8个
}
