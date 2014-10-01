package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.Url;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ImageDownloaderTest {

    private static final Url URL = new Url("http://api.mtgdb.info/content/hi_res_card_images/386623.jpg");
    private static final File FILE = new File(ImageDownloaderTest.class.getSimpleName() + "-file.jpg");
    private static final File FILE_IN_NON_EXISTING_DIRECTORY = new File(ImageDownloaderTest.class.getSimpleName() + "-dir",
                                                                        ImageDownloaderTest.class.getSimpleName() + "-file.jpg");

    private static final BufferedImage EXPECTED_IMAGE = loadExpectedImage();

    private ImageDownloader imageDownloader = new ImageDownloader();

    @Before
    @After
    public void deleteFile() {
        FILE.delete();
        FILE_IN_NON_EXISTING_DIRECTORY.delete();
        FILE_IN_NON_EXISTING_DIRECTORY.getParentFile().delete();
    }

    @Test
    public void downloadsImageToFile() throws IOException {
        imageDownloader.download(URL, FILE);

        assertEquals("file.exists()", FILE.exists(), true);
        assertImagesEqual(EXPECTED_IMAGE, ImageIO.read(FILE));
    }

    @Test
    public void createsParentDirectoryIfItDoesNotExist() throws IOException {
        imageDownloader.download(URL, FILE_IN_NON_EXISTING_DIRECTORY);

        assertEquals("file.exists()", FILE_IN_NON_EXISTING_DIRECTORY.exists(), true);
        assertImagesEqual(EXPECTED_IMAGE, ImageIO.read(FILE_IN_NON_EXISTING_DIRECTORY));
    }


    private static void assertImagesEqual(BufferedImage expected, BufferedImage actual) {
        assertEquals("Image width mismatch", expected.getWidth(), actual.getWidth());
        assertEquals("Image height mismatch", expected.getHeight(), actual.getHeight());
        for (int x = 0; x < actual.getWidth(); x++) {
            for (int y = 0; y < actual.getHeight(); y++) {
                assertEquals("Pixel mismatch (width=" + x + ",height=" + y + ")", expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }

    private static BufferedImage loadExpectedImage() {
        try {
            return ImageIO.read(ImageDownloaderTest.class.getResource("ImageDownloaderTest-expectedfile.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
