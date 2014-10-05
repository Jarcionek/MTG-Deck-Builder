package mtgdeckbuilder;

import java.io.File;

public class Config {

    private static final String INSTALLATION_DIRECTORY = "C:/Users/Jarcionek/Desktop/MTG";

    private Config() {}

    public static File cardsDirectory() {
        return new File(INSTALLATION_DIRECTORY, "cards");
    }

    public static File tagsDirectory() {
        return new File(INSTALLATION_DIRECTORY, "tags");
    }

}
