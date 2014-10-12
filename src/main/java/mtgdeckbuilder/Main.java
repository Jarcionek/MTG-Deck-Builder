package mtgdeckbuilder;

import mtgdeckbuilder.frontend.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException("could not set system look and feel", e);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }

}

/* //TODO Jarek: user interface is not intuitive and difficult to use:

    1. show "untag" button only when displaying chosen tag

    2. when untagging the card, remove it from the currently shown cards (in CardsDisplayPanel)

 */
