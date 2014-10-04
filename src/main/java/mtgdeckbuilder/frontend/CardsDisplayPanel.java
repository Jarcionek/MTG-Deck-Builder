package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.CardImageInfo;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

//TODO Jarek: make some cool effects here!
public class CardsDisplayPanel extends JPanel {

    private static final int MARGIN;
    private static final double[] WEIGHTS;

    static {
        double[] weights = {0.03, 0.10, 0.25};

        MARGIN = weights.length + 1;
        WEIGHTS = new double[weights.length * 2 + 3];
        for (int i = 0; i < weights.length; i++) {
            WEIGHTS[i + 1] = weights[i];
            WEIGHTS[WEIGHTS.length - i - 2] = 1 - weights[i];
        }
        WEIGHTS[0] = 0;
        WEIGHTS[WEIGHTS.length - 1] = 1;
        WEIGHTS[WEIGHTS.length / 2] = 0.5;
    }

    private final CardImageLoader cardImageLoader;

    private List<JLabel> labels;
    private int selectedCard;

    @TestCode private int labelCount = 0;

    public CardsDisplayPanel(CardImageLoader cardImageLoader) {
        this.cardImageLoader = cardImageLoader;
        this.setLayout(null);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getX() < 0.4 * getWidth()) {
                    if (selectedCard > 0) {
                        selectedCard--;
                        display();
                    }
                }
                if (e.getX() > 0.6 * getWidth()) {
                    if (selectedCard < labels.size() - 1) {
                        selectedCard++;
                        display();
                    }
                }
            }
        });
    }

    public void load(Iterable<CardImageInfo> cardImageInfos) {
        labels = new ArrayList<>();
        selectedCard = 0;

        for (CardImageInfo cardImageInfo : cardImageInfos) {
            JLabel label = cardImageLoader.loadLowRes(cardImageInfo.getName());
            setLabelName(label);
            labels.add(label);
        }

        display();
    }

    private void display() {
        removeAll();
        for (int i = selectedCard - MARGIN; i <= selectedCard + MARGIN; i++) {
            if (i < 0 || i >= labels.size()) {
                continue;
            }

            JLabel label = labels.get(i);

            int labelWidth = (int) label.getPreferredSize().getWidth();
            int labelHeight = (int) label.getPreferredSize().getHeight();
            int panelWidth = (int) getSize().getWidth();

            int xLength = panelWidth - labelWidth;

            int diff = (int) (xLength * WEIGHTS[i - selectedCard + MARGIN]) ;
            label.setBounds(diff, 0, labelWidth, labelHeight);
        }

        add(labels.get(selectedCard));
        for (int i = selectedCard + 1; i <= selectedCard + MARGIN; i++) {
            if (i >= 0 && i < labels.size()) {
                add(labels.get(i));
            }
        }
        for (int i = selectedCard - 1; i >= selectedCard - MARGIN; i--) {
            if (i >= 0 && i < labels.size()) {
                add(labels.get(i));
            }
        }

        revalidate();
        repaint();
    }

    @TestCode
    public void setLabelName(JLabel label) {
        label.setName("cardLabel" + labelCount++);
    }

}
