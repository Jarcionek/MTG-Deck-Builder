package mtgdeckbuilder;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.NoSuchElementException;

public class FrontEndTestingUtils {

    public static <T extends Component> T findComponent(Container container, String componentName, Class<T> requestedClass) {
        for (Component component : container.getComponents()) {
            if (component.getName().equals(componentName)) {
                return requestedClass.cast(component);
            }
        }
        throw new NoSuchElementException("No component named " + componentName + " in container " + container);
    }

    public static void click(JButton button) {
        for (ActionListener actionListener : button.getActionListeners()) {
            actionListener.actionPerformed(
                    new ActionEvent(
                            button,
                            ActionEvent.ACTION_PERFORMED,
                            button.getText(),
                            System.currentTimeMillis(),
                            InputEvent.BUTTON1_MASK
                    )
            );
        }
    }

    public static void displayAndWait(Container container) {
        JFrame frame = new JFrame();
        frame.setContentPane(container);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final Thread t = Thread.currentThread();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                t.interrupt();
            }
        });


        try {
            Thread.sleep(60 * 60 * 1000); //TODO Jarek: fix that blocking hack
        } catch (InterruptedException expected) {}
    }

}
