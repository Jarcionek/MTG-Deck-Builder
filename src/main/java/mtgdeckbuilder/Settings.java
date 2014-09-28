package mtgdeckbuilder;

import java.io.File;

public class Settings {

    private static final File INSTALLATION_DIRECTORY = new File("C:/Users/Jarcionek/Desktop/mtg-install-dir-2");
    private static final File CARDS_IMAGES_DIRECTORY = new File(INSTALLATION_DIRECTORY, "cards/images");
//    private static final File CARDS_INFO_DIRECTORY = new File(INSTALLATION_DIRECTORY, "cards/info");
    private static final File ALL_CARDS_DATA_FILE = new File(INSTALLATION_DIRECTORY, "cards/_allcardsdata.json");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
        INSTALLATION_DIRECTORY.mkdirs();
        CARDS_IMAGES_DIRECTORY.mkdirs();
//        CARDS_INFO_DIRECTORY.mkdirs();
    }

    public static File getInstallationDirectory() {
        return INSTALLATION_DIRECTORY;
    }

    public static File getCardsImagesDirectory() {
        return CARDS_IMAGES_DIRECTORY;
    }

//    public static File getCardsInfoDirectory() {
//        return CARDS_INFO_DIRECTORY;
//    }

    public static File getAllCardsDataFile() {
        return ALL_CARDS_DATA_FILE;
    }

}
