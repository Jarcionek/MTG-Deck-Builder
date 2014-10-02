package mtgdeckbuilder.frontend;

import org.junit.Test;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;

import static mtgdeckbuilder.Utils.assertImagesEqual;
import static mtgdeckbuilder.Utils.cardsDirectory;
import static mtgdeckbuilder.Utils.loadResourceImage;

public class CardImageLoaderTest {

    private CardImageLoader cardImageLoader = new CardImageLoader(cardsDirectory());

    @Test
    public void loadsLowResolutionCard() {
        BufferedImage expectedBufferedImage = loadResourceImage("cards/low/Misty Rainforest.jpg");

        JLabel label = cardImageLoader.loadLowRes("Misty Rainforest");

        ImageIcon imageIcon = (ImageIcon) label.getIcon();
        BufferedImage bufferedImage = (BufferedImage) imageIcon.getImage();

        assertImagesEqual(expectedBufferedImage, bufferedImage);
    }

    @Test
    public void loadsHighResolutionCard() {
        BufferedImage expectedBufferedImage = loadResourceImage("cards/high/Karplusan Forest.jpg");

        JLabel label = cardImageLoader.loadHighRes("Karplusan Forest");

        ImageIcon imageIcon = (ImageIcon) label.getIcon();
        BufferedImage bufferedImage = (BufferedImage) imageIcon.getImage();

        assertImagesEqual(expectedBufferedImage, bufferedImage);
    }
    
}