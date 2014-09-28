package mtgdeckbuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class CardsInfoDownloaderTest {

    private Url url;
    private File file;

    @Before
    public void setUp() throws IOException {
        url = new Url("http://api.mtgdb.info/cards/?fields=id,name&limit=2");
        file = new File(CardsInfoDownloaderTest.class.getSimpleName() + "-url-as-text");
        Logger.getAnonymousLogger().info("Creating file " + file.getAbsolutePath());
        if (!file.createNewFile()) {
            throw new RuntimeException("Could not create file: " + file);
        }
    }

    @After
    public void tearDown() {
        Logger.getAnonymousLogger().info("Deleting file " + file.getAbsolutePath());
        if (!file.delete()) {
            throw new RuntimeException("Could not delete file: " + file);
        }
    }

    @Test
    public void test() throws FileNotFoundException {
        CardsInfoDownloader cardsInfoDownloader = new CardsInfoDownloader();
        cardsInfoDownloader.set(url, file);

        cardsInfoDownloader.download();

        assertThat(asString(file), is(equalTo("[{\"id\":1,\"name\":\"Ankh of Mishra\"},{\"id\":2,\"name\":\"Basalt Monolith\"}]")));
    }

    private static String asString(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        assertFalse("Only one line was expected but there are more", scanner.hasNextLine());
        scanner.close();
        return line;
    }

}
