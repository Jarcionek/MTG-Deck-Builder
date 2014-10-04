package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.topics.SearchTopic;
import org.junit.Test;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SearchedCardsDisplayPanelTest {

    private static final Set<CardImageInfo> CARD_IMAGE_INFOS = newHashSet(new CardImageInfo(3, "asasf"));

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
        doNothing().when(searchedCardsDisplayPanel).load(CARD_IMAGE_INFOS);

        searchedCardsDisplayPanel.searchFinished(CARD_IMAGE_INFOS);

        verify(searchedCardsDisplayPanel, times(1)).load(CARD_IMAGE_INFOS);
    }

}