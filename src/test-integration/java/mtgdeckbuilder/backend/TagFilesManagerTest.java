package mtgdeckbuilder.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TagFilesManagerTest {

    private static final File TAGS_DIRECTORY = new File(TagFilesManagerTest.class.getSimpleName() + "-dir");

    private final String tagOne = "tagNameOne";
    private final String tagTwo = "tagNameTwo";
    private final File tagFileOne = new File(TAGS_DIRECTORY, tagOne);
    private final File tagFileTwo = new File(TAGS_DIRECTORY, tagTwo);
    private final String cardNameOne = "cardNameOne";
    private final String cardNameTwo = "cardNameTwo";
    private final String cardNameThree = "cardNameThree";
    private final List<String> cardsOne = newArrayList(cardNameOne);
    private final List<String> cardsTwo = newArrayList(cardNameTwo, cardNameThree);

    private TagFilesManager tagFilesManager = new TagFilesManager(TAGS_DIRECTORY);

    @Before
    @After
    public void deleteFiles() {
        tagFileOne.delete();
        tagFileTwo.delete();
        TAGS_DIRECTORY.delete();
    }

    @Test
    public void savesToFiles() {
        tagFilesManager.save(tagOne, cardsOne);
        tagFilesManager.save(tagTwo, cardsTwo);

        assertEquals("tagFileOne exists", true, tagFileOne.exists());
        assertEquals("tagFileTwo exists", true, tagFileTwo.exists());
        assertThat(fileAsString(tagFileOne), containsString(cardNameOne));
        assertThat(fileAsString(tagFileTwo), both(containsString(cardNameTwo)).and(containsString(cardNameThree)));
    }

    @Test
    public void overridesExistingFile() {
        tagFilesManager.save(tagOne, cardsOne);
        tagFilesManager.save(tagOne, cardsTwo);

        assertThat(fileAsString(tagFileOne), not(containsString(cardNameOne)));
        assertThat(fileAsString(tagFileOne), both(containsString(cardNameTwo)).and(containsString(cardNameThree)));
    }

    @Test
    public void loadsFromSavedFile() {
        tagFilesManager.save(tagOne, cardsTwo);

        List<String> loadedCards = tagFilesManager.load(tagOne);

        assertEquals(cardsTwo, loadedCards);
    }

    @Test
    public void returnsEmptyListWhenLoadingNonExistingFile() {
        List<String> loadedCards = tagFilesManager.load(tagTwo);

        assertThat(loadedCards, is(empty()));
    }


    private static String fileAsString(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String string = "";
            while (scanner.hasNextLine()) {
                string += scanner.nextLine() + System.getProperty("line.separator");
            }
            scanner.close();
            return string;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}