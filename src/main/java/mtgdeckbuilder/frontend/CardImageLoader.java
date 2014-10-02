package mtgdeckbuilder.frontend;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.File;
import java.io.IOException;

public class CardImageLoader {

    private final File cardsDirectory;

    public CardImageLoader(File cardsDirectory) {
        this.cardsDirectory = cardsDirectory;
    }

    public JLabel loadLowRes(String cardName) {
        try {
            return new JLabel(new ImageIcon(ImageIO.read(new File(cardsDirectory, "low/" + cardName + ".jpg"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JLabel loadHighRes(String cardName) {
        try {
            return new JLabel(new ImageIcon(ImageIO.read(new File(cardsDirectory, "high/" + cardName + ".jpg"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
