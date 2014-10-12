package mtgdeckbuilder.util;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

public class FrontEndTestingUtils {

    public static boolean containsComponentRecursively(Container container, String componentName) {
        try {
            findComponentRecursively(container, componentName, Component.class);
            return true;
        } catch (NoSuchComponentException exception) {
            return false;
        }
    }

    public static <T extends Component> T findComponent(Container container, String componentName, Class<T> requestedClass) {
        for (Component component : container.getComponents()) {
            if (component.getName() == null) {
                continue; // component has no name
            }
            if (component.getName().equals(componentName)) {
                return requestedClass.cast(component);
            }
        }
        throw new NoSuchComponentException(container, componentName);
    }

    public static <T extends Component> T findComponentRecursively(Container container, String componentName, Class<T> requestedClass) {
        if (container == null) {
            throw new IllegalArgumentException("Container was null");
        }

        T componentFound = fcr(container, componentName, requestedClass);
        if (componentFound == null) {
            throw new NoSuchComponentException(container, componentName);
        } else {
            return componentFound;
        }
    }

    private static <T extends Component> T fcr(Container container, String componentName, Class<T> requestedClass) {
        T componentFound = null;
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                T nestedComponentFound = fcr((Container) component, componentName, requestedClass);
                if (nestedComponentFound != null) {
                    if (componentFound == null) {
                        componentFound = nestedComponentFound;
                    } else {
                        throw new NoUniqueComponentException(container, componentName);
                    }
                }
            }
            if (component.getName() == null) {
                continue; // component has no name
            }
            if (component.getName().equals(componentName)) {
                if (componentFound != null) {
                    throw new NoUniqueComponentException(container, componentName);
                } else {
                    componentFound = requestedClass.cast(component);
                }
            }
        }
        return componentFound;
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

    public static void pressEnterIn(JTextField textField) {
        for (ActionListener actionListener : textField.getActionListeners()) {
            actionListener.actionPerformed(
                    new ActionEvent(
                            textField,
                            ActionEvent.ACTION_PERFORMED,
                            textField.getText(),
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

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                countDownLatch.countDown();
            }
        });


        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {}
    }

}
