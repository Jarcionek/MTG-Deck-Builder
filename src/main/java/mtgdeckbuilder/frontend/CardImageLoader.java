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
        File file = new File(cardsDirectory, "low/" + cardName + ".jpg");
        try {
            return new JLabel(new ImageIcon(ImageIO.read(file)));
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }

    public JLabel loadHighRes(String cardName) {
        File file = new File(cardsDirectory, "high/" + cardName + ".jpg");
        try {
            return new JLabel(new ImageIcon(ImageIO.read(file)));
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }

}
