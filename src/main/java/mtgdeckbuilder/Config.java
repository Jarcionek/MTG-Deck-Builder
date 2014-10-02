package mtgdeckbuilder;

import java.io.File;

public class Config {

    private Config() {}

    public static File cardsDirectory() {
        return new File("C:\\Users\\Jarcionek\\Desktop\\Cards");
    }

}
