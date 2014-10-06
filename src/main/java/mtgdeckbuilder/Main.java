package mtgdeckbuilder;

import mtgdeckbuilder.frontend.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException("could not set system look and feel", e);
        }

        Config.tagsDirectory().mkdirs();
        try {
            new File(Config.tagsDirectory(), "test-tag-1.txt").createNewFile();
            new File(Config.tagsDirectory(), "test-tag-2.txt").createNewFile();
            new File(Config.tagsDirectory(), "test-tag-3.txt").createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }

}
