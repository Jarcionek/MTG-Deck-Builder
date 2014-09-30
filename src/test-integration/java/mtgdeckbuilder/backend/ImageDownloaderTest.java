package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.Url;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ImageDownloaderTest {

    private static final Url URL = new Url("http://api.mtgdb.info/content/hi_res_card_images/386623.jpg");
    private static final File FILE = new File(ImageDownloaderTest.class.getSimpleName() + "-file.jpg");

    private ImageDownloader imageDownloader = new ImageDownloader();

    @Before
    @After
    public void deleteFile() {
        FILE.delete();
    }

    @Test
    public void downloadsImageToFile() throws IOException {
        imageDownloader.download(URL, FILE);

        assertThat("file.exists()", FILE.exists(), is(equalTo(true)));

        BufferedImage expectedImage = ImageIO.read(ImageDownloaderTest.class.getResource("ImageDownloaderTest-expectedfile.jpg"));

        assertImagesEqual(expectedImage, ImageIO.read(FILE));
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

}
