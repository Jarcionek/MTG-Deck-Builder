package mtgdeckbuilder.util;

import java.awt.Container;

public class NoSuchComponentException extends RuntimeException {

    public NoSuchComponentException(Container container, String componentName) {
        super("No component named " + componentName + " in container " + container);
    }

}
