package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.CardImageInfo;
import org.junit.Test;

import javax.swing.JLabel;
import java.util.HashSet;
import java.util.Set;

import static mtgdeckbuilder.util.FrontEndTestingUtils.containsComponentRecursively;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardsDisplayPanelTest {

    private CardImageLoader cardImageLoader = mock(CardImageLoader.class);

    private CardsDisplayPanel cardsDisplayPanel = new CardsDisplayPanel(cardImageLoader);

    @Test
    public void loadsSingleCard() {
        when(cardImageLoader.loadLowRes("Some Card Name")).thenReturn(new JLabel("Some Label"));

        cardsDisplayPanel.load(set("Some Card Name"));

        assertThat("contains card", containsComponentRecursively(cardsDisplayPanel, "cardLabel0"), is(equalTo(true)));
    }

    @Test
    public void loadsTwoCards() {
        when(cardImageLoader.loadLowRes("Card 1")).thenReturn(new JLabel("Label 1"));
        when(cardImageLoader.loadLowRes("Card 2")).thenReturn(new JLabel("Label 2"));

        cardsDisplayPanel.load(set("Card 1", "Card 2"));

        assertThat("contains card 1", containsComponentRecursively(cardsDisplayPanel, "cardLabel0"), is(equalTo(true)));
        assertThat("contains card 2", containsComponentRecursively(cardsDisplayPanel, "cardLabel1"), is(equalTo(true)));
    }


    private static Set<CardImageInfo> set(String... cardNames) {
        Set<CardImageInfo> set = new HashSet<>();
        for (String cardName : cardNames) {
            set.add(new CardImageInfo(-1, cardName));
        }
        return set;
    }

}