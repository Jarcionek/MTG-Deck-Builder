package mtgdeckbuilder;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class Utils {

    public static void assertImagesEqual(BufferedImage expected, BufferedImage actual) {
        assertEquals("Image size mismatch", new Dimension(expected.getWidth(), expected.getHeight()), new Dimension(actual.getWidth(), actual.getHeight()));
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

    public static File cardsDirectory() {
        try {
            return new File(Utils.class.getResource("cards").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
