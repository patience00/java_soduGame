package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Myframe extends JFrame {
    public static Object obj = new Object();
    // 创建九宫格界面
    public final static JTextField[][] filed = new JTextField[9][9];

    public Myframe() {
        // 初始化界面，让所有的格子都等于空
        for (int a = 0; a < 9; a++) {
            for (int b = 0; b < 9; b++) {
                filed[a][b] = new JTextField();
                filed[a][b].setText("");
                filed[a][b].setFont(new Font("微软雅黑",1,14));
            }
        }

        // 编写布局，把textfield添加到布局中
        JPanel jpan = new JPanel();
        jpan.setLayout(new GridLayout(9, 9));
        for (int a = 8; a > -1; a--) {
            for (int b = 0; b < 9; b++) {
                jpan.add(filed[b][a]);
            }
        }

        // 界面布局为居中
        add(jpan, BorderLayout.CENTER);
        JPanel jpb = new JPanel();

        // 设置两个按钮，计算和退出
        JButton button1 = new JButton("计算");
        button1.setFont(new Font("微软雅黑",1,20));
        button1.setSize(30,30);
        JButton button2 = new JButton("关闭");
        button2.setFont(new Font("微软雅黑",1,20));
        button2.setSize(30,30);
        // 将按钮添加到界面上
        jpb.add(button1);
        jpb.add(button2);

        // 给按钮添加监听器，就是添加事件响应函数
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                synchronized (obj) {
                    for (int a = 0; a < 9; a++) {
                        for (int b3 = 0; b3 < 9; b3++) {
                            int pp = 0;
                            // 获取九宫格中的已填入数据的值，这些就是谜面
                            if (!(filed[a][b3].getText().trim().equals(""))) {
                                pp = Integer.parseInt(filed[a][b3].getText()
                                        .trim());
                                Calculate.b[a][b3] = pp;
                            }
                        }
                    }
                }

                synchronized (obj) {
                    // 开启线程计算九宫格的答案
                    new Thread(new Calculate()).start();
                }
            }
        });

        // button2很简单，调用api关闭程序
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        // 设置界面的布局
        add(jpb, BorderLayout.SOUTH);
    }
}
