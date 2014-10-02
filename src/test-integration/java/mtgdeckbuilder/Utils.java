package mtgdeckbuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Utils {

    public static void assertImagesEqual(BufferedImage expected, BufferedImage actual) {
        assertEquals("Image width mismatch", expected.getWidth(), actual.getWidth());
        assertEquals("Image height mismatch", expected.getHeight(), actual.getHeight());
        for (int x = 0; x < actual.getWidth(); x++) {
            for (int y = 0; y < actual.getHeight(); y++) {
                assertEquals("Pixel mismatch (width=" + x + ",height=" + y + ")", expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }

    public static BufferedImage loadResourceImage(String resourceName) {
        try {
            return ImageIO.read(Utils.class.getResource(resourceName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
