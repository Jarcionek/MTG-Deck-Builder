package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.CardImageInfo;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;

//TODO Jarek: make some cool effects here instead of just scroll pane!
public class CardsDisplayPanel extends JPanel {

    private final CardImageLoader cardImageLoader;

    private final JPanel innerPanel;

    @TestCode private int labelCount = 0;

    public CardsDisplayPanel(CardImageLoader cardImageLoader) {
        this.cardImageLoader = cardImageLoader;
        this.innerPanel = new JPanel();
        this.setLayout(new GridLayout(1, 1));
        this.add(new JScrollPane(innerPanel));
    }

    public void load(Iterable<CardImageInfo> cardImageInfos) {
        for (CardImageInfo cardImageInfo : cardImageInfos) {
            JLabel label = cardImageLoader.loadLowRes(cardImageInfo.getName());
            setLabelName(label);
            innerPanel.add(label);
        }

    }

    @TestCode
    public void setLabelName(JLabel label) {
        label.setName("cardLabel" + labelCount++);
    }

}
