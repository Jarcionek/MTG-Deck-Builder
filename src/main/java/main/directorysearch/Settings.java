package main.directorysearch;

import java.io.File;

public class Settings {

    private static final File INSTALLATION_DIRECTORY = new File("C:/Users/Jarcionek/Desktop/mtg-install-dir");
    private static final File CARDS_IMAGES_DIRECTORY = new File(INSTALLATION_DIRECTORY, "cards");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
        INSTALLATION_DIRECTORY.mkdirs();
        CARDS_IMAGES_DIRECTORY.mkdirs();
    }

    public static File getInstallationDirectory() {
        return INSTALLATION_DIRECTORY;
    }

    public static File getCardsImagesDirectory() {
        return CARDS_IMAGES_DIRECTORY;
    }

}
