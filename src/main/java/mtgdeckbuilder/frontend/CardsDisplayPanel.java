package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.CardImageInfo;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Set;

public class CardsDisplayPanel extends JPanel {

    private final CardImageLoader cardImageLoader;

    @TestCode private int labelCount = 0;

    public CardsDisplayPanel(CardImageLoader cardImageLoader) {
        this.cardImageLoader = cardImageLoader;
    }

    public void load(Set<CardImageInfo> cardImageInfos) {
        for (CardImageInfo cardImageInfo : cardImageInfos) {
            JLabel label = cardImageLoader.loadLowRes(cardImageInfo.getName());

            setLabelName(label);

            add(label);
        }

    }

    @TestCode
    public void setLabelName(JLabel label) {
        label.setName("cardLabel" + labelCount++);
    }

}
