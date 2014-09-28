package mtgdeckbuilder.util;

import java.awt.Container;

public class NoUniqueComponentException extends RuntimeException {

    public NoUniqueComponentException(Container container, String componentName) {
        super("No unique component named " + componentName + " in container " + container);
    }

}
