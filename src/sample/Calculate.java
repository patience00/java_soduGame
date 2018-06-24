package sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class Calculate implements Runnable {
    // boo用于判断该格是否为空
    public static boolean[][] boo = new boolean[9][9];

    //计算指定行的值
    public static int upRow = 0;

    //计算指定列值
    public static int upColumn = 0;

    //将存储九宫格中的数据
    public static int[][] b = new int[9][9];

    //查找没有填入数值的空格
    public static void flyBack(boolean[][] judge, int row, int column) {
        // 生成临时变量s，具体下面会介绍
        int s = column * 9 + row;
        s--;

        // 取商的值，实际就是column的值
        int quotient = s / 9;

        // 取余数的值，实际是取(row-1)%9
        int remainder = s % 9;

        // 判断是否满足条件
        if (judge[remainder][quotient]) {
            flyBack(judge, remainder, quotient);
        } else {
            // 赋值给upRow
            upRow = remainder;
            // 赋值给upColumn
            upColumn = quotient;
        }

    }

    //遍历所有可能的值
    public static void arrayAdd(ArrayList<Integer> array, TreeSet<Integer> tree) {
        // 遍历1~10
        for (int z = 1; z < 10; z++) {
            // flag3默认为true，判断z是否符合条件
            boolean flag3 = true;

            // it就是一个迭代器
            Iterator<Integer> it = tree.iterator();

            // tree如果没有遍历完继续遍历
            while (it.hasNext()) {
                // 将列表中的值赋给b
                int b = it.next().intValue();
                if (z == b) {
                    flag3 = false;
                    break;
                }
            }

            // 如果判断z没有出现在过tree中，就将它添加进去
            if (flag3) {
                array.add(new Integer(z));
            }

            // 初始化flag3
            flag3 = true;
        }
    }

    public static ArrayList<Integer> assume(int row, int column) {
        // 创建数组array
        ArrayList<Integer> array = new ArrayList<Integer>();
        TreeSet<Integer> tree = new TreeSet<Integer>();

        // 添加同一列其他的元素值
        for (int a = 0; a < 9; a++) {

            // 如果该格不为空，就添加到tree中
            if (a != column && b[row][a] != 0) {
                tree.add(new Integer(b[row][a]));
            }
        }

        // 添加同行的其他元素
        for (int c = 0; c < 9; c++) {

            // 如果该格满足添加，就添加到tree中
            if (c != row && b[c][column] != 0) {
                tree.add(new Integer(b[c][column]));
            }
        }

        // 这里使用了整型除法只保留整数部分的特点，获取元素在同一个小九宫格的行
        for (int a = (row / 3) * 3; a < (row / 3 + 1) * 3; a++) {
            // 获取元素在同一个九宫格的列
            for (int c = (column / 3) * 3; c < (column / 3 + 1) * 3; c++) {
                // 如果元素满足条件都添加到tree中
                if ((!(a == row && c == column)) && b[a][c] != 0) {
                    tree.add(new Integer(b[a][c]));
                }
            }
        }
        arrayAdd(array, tree);
        return array;
    }

    @Override
    public void run() {
// 初始化变量行，列
        int row = 0, column = 0;
        long start = System.currentTimeMillis();

        // flag用来判断该格子是否填入正确
        boolean flag = true;
        for (int a = 0; a < 9; a++) {
            for (int c = 0; c < 9; c++) {
                if (b[a][c] != 0) {
                    /* boo的作用是找出填入数据的空格，
                     *  填入数据的空格是谜面，我们需要根据这些信息解迷题
                     */
                    boo[a][c] = true;
                } else {
                    // 为空的格子是需要填入数据的部分
                    boo[a][c] = false;
                }
            }
        }

        /* arraylist是一个二维的序列，它的每一个值都是一个数组指针，
         *  存放了该格子可能的解，当一个解错误时，调用下一个解，
         * 这也就是前面介绍的数独解法。
         */
        ArrayList<Integer>[][] utilization = new ArrayList[9][9];

        while (column < 9) {
            if (flag == true) {
                row = 0;
            }
            while (row < 9) {
                if (b[row][column] == 0) {
                    if (flag) {
                        ArrayList<Integer> list = assume(row, column);//
                        utilization[row][column] = list;
                    }

                    // 如果没有找到可能的解，说明前面的值有错误，就回溯到之前的格子进行修改
                    if (utilization[row][column].isEmpty()) {
                        // 调用flyBack函数寻找合适的row和column
                        flyBack(boo, row, column);

                        // 将row返回到合适的位子
                        row = upRow;

                        // 将column返回到合适的位子
                        column = upColumn;

                        // 初始化有问题的格子
                        b[row][column] = 0;
                        column--;
                        flag = false;
                        break;
                    } else {
                        // 将备选数组中第一个值赋给b
                        b[row][column] = utilization[row][column].get(0);

                        // 因为上面已经赋值了，所以就删除掉第一个数值
                        utilization[row][column].remove(0);
                        flag = true;

                        //判断是否所有的格子都填入正确，然后将正确的结果输出到屏幕上
                        judge();
                    }
                } else {
                    // 如果r为false，说明还有格子没填入数据，就继续遍历
                    flag = true;
                }
                row++;
            }
            column++;
        }
        long end = System.currentTimeMillis();
        System.out.println("用时 = " + (end - start) + "毫秒");
    }

    //分析九宫格是否完成
    public void judge() {
        boolean r = true;

        // 查找还没有填入数据的格子
        for (int a1 = 0; a1 < 9; a1++) {
            for (int b1 = 0; b1 < 9; b1++) {
                if (r == false) {
                    break;
                }

                // 如果 b[a1][b1] 需要计算，就将它提取出来
                if (b[a1][b1] == 0) {
                    r = false;
                }
            }
        }

        // 如果r为true，则所有的格子都填入了数据，说明九宫格就完成了，此时输出结果到屏幕上
        if (r) {
            for (int a1 = 0; a1 < 9; a1++) {
                for (int b1 = 0; b1 < 9; b1++) {
                    Myframe.filed[a1][b1].setText(b[a1][b1] + "");
                }
            }
        }
    }
}
