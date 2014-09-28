package main.directorysearch;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        Settings.init();
        System.out.println("Hello world!");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        JFrame frame = new JFrame("Hello Magic!");
        frame.setContentPane(new CardsTreeView());
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
