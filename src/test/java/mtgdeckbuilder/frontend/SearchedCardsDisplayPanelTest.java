package mtgdeckbuilder.frontend;

import mtgdeckbuilder.topics.SearchTopic;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SearchedCardsDisplayPanelTest {

    private static final Iterable<String> CARD_NAMES = newArrayList("one-card", "another-card");

    private CardImageLoader cardImageLoader = mock(CardImageLoader.class);
    private SearchTopic searchTopic = mock(SearchTopic.class);

    private SearchedCardsDisplayPanel searchedCardsDisplayPanel = new SearchedCardsDisplayPanel(cardImageLoader, searchTopic);

    @Test
    public void subscribesToTheTopic() {
        verify(searchTopic, times(1)).addSubscriber(searchedCardsDisplayPanel);
    }

    @Test
    public void loadsRequestedCardsWhenSearchFinished() {
        searchedCardsDisplayPanel = spy(searchedCardsDisplayPanel);
        doNothing().when(searchedCardsDisplayPanel).load(CARD_NAMES);

        searchedCardsDisplayPanel.searchFinished(CARD_NAMES);

        verify(searchedCardsDisplayPanel, times(1)).load(CARD_NAMES);
    }

}