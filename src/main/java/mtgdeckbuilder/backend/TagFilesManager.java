package mtgdeckbuilder.backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TagFilesManager {

    private final File tagsDirectory;

    public TagFilesManager(File tagsDirectory) {
        this.tagsDirectory = tagsDirectory;
    }

    public void save(String tagName, Iterable<String> cardNames) {
        File tagFile = new File(tagsDirectory, tagName);
        if (!tagFile.getParentFile().exists()) {
            tagFile.getParentFile().mkdirs();
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tagFile));
            for (String cardName : cardNames) {
                bufferedWriter.write(cardName);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException exception) {
            throw new RuntimeException("Could not save tag file " + tagFile.getAbsolutePath(), exception);
        }
    }

    public List<String> load(String tagName) {
        File tagFile = new File(tagsDirectory, tagName);

        try {
            List<String> cards = new ArrayList<>();

            Scanner scanner = new Scanner(tagFile);
            while (scanner.hasNextLine()) {
                cards.add(scanner.nextLine());
            }
            scanner.close();

            return cards;
        } catch (FileNotFoundException exception) {
            throw new RuntimeException("Could not load tag file " + tagFile.getAbsolutePath(), exception);
        }
    }

}
