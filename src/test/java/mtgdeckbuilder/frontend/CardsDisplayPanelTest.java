package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.CardImageInfo;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.JLabel;
import java.awt.Dimension;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static mtgdeckbuilder.util.FrontEndTestingUtils.containsComponentRecursively;
import static mtgdeckbuilder.util.FrontEndTestingUtils.displayAndWait;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardsDisplayPanelTest {

    private CardImageLoader cardImageLoader = mock(CardImageLoader.class);

    private CardsDisplayPanel cardsDisplayPanel = new CardsDisplayPanel(cardImageLoader);

    @Test
    public void loadsSingleCardAsSet() {
        when(cardImageLoader.loadLowRes("Some Card Name")).thenReturn(new JLabel("Some Label"));

        cardsDisplayPanel.load(set("Some Card Name"));

        assertThat("contains card", containsComponentRecursively(cardsDisplayPanel, "cardLabel0"), is(equalTo(true)));
    }

    @Test
    public void loadsTwoCardsAsSet() {
        when(cardImageLoader.loadLowRes("Card 1")).thenReturn(new JLabel("Label 1"));
        when(cardImageLoader.loadLowRes("Card 2")).thenReturn(new JLabel("Label 2"));

        cardsDisplayPanel.load(set("Card 1", "Card 2"));

        assertThat("contains card 1", containsComponentRecursively(cardsDisplayPanel, "cardLabel0"), is(equalTo(true)));
        assertThat("contains card 2", containsComponentRecursively(cardsDisplayPanel, "cardLabel1"), is(equalTo(true)));
    }

    @Test
    public void loadsCardsAsList() {
        when(cardImageLoader.loadLowRes("Card 1")).thenReturn(new JLabel("Label 2"));
        when(cardImageLoader.loadLowRes("Card 2")).thenReturn(new JLabel("Label 1"), new JLabel("Label 3"));

        cardsDisplayPanel.load(list("Card 2", "Card 1", "Card 2"));

        assertThat("cardLabel0", findComponentRecursively(cardsDisplayPanel, "cardLabel0", JLabel.class).getText(), is(equalTo("Label 1")));
        assertThat("cardLabel1", findComponentRecursively(cardsDisplayPanel, "cardLabel1", JLabel.class).getText(), is(equalTo("Label 2")));
        assertThat("cardLabel2", findComponentRecursively(cardsDisplayPanel, "cardLabel2", JLabel.class).getText(), is(equalTo("Label 3")));
    }


    @Ignore
    @Test
    public void manualFrontEndTestWithFiveDifferentCards() throws URISyntaxException {
        cardsDisplayPanel = new CardsDisplayPanel(new CardImageLoader(new File(this.getClass().getResource("cards").toURI())));

        cardsDisplayPanel.setSize(new Dimension(800, 300));
        cardsDisplayPanel.setPreferredSize(new Dimension(800, 300));
        cardsDisplayPanel.load(set("AEther Adept", "Ajani's Pridemate", "Black Cat", "Cone of Flame", "Elvish Mystic"));

        displayAndWait(cardsDisplayPanel);
    }

    @Ignore
    @Test
    public void manualFrontEndTestWithManyCards() throws URISyntaxException {
        cardsDisplayPanel = new CardsDisplayPanel(new CardImageLoader(new File(this.getClass().getResource("cards").toURI())));

        List<CardImageInfo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.addAll(list("AEther Adept", "Ajani's Pridemate", "Black Cat", "Cone of Flame", "Elvish Mystic"));
        }

        cardsDisplayPanel.setSize(new Dimension(800, 300));
        cardsDisplayPanel.setPreferredSize(new Dimension(800, 300));
        cardsDisplayPanel.load(list);

        displayAndWait(cardsDisplayPanel);
    }


    private static Set<CardImageInfo> set(String... cardNames) {
        Set<CardImageInfo> set = new HashSet<>();
        for (String cardName : cardNames) {
            set.add(new CardImageInfo(-1, cardName));
        }
        return set;
    }

    private static List<CardImageInfo> list(String... cardNames) {
        List<CardImageInfo> list = new ArrayList<>();
        for (String cardName : cardNames) {
            list.add(new CardImageInfo(-1, cardName));
        }
        return list;
    }

}