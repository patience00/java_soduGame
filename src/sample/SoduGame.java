package sample;


import javax.swing.JFrame;

public class SoduGame {


    public static void main(String[] args) {
//        launch(args);

        SoduFrame myf = new SoduFrame();
        myf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置主界面的名称
        myf.setTitle("数据计算器");

        //设置界面的大小
        myf.setSize(500, 500);

        //设置主程序可见
        myf.setVisible(true);
    }
}
