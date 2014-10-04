package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.topics.SearchTopic;

import java.util.Set;

public class SearchedCardsDisplayPanel extends CardsDisplayPanel implements SearchTopic.Subscriber {

    public SearchedCardsDisplayPanel(CardImageLoader cardImageLoader, SearchTopic searchTopic) {
        super(cardImageLoader);
        searchTopic.addSubscriber(this);
    }

    @Override
    public void searchFinished(Set<CardImageInfo> cardImageInfos) {
        load(cardImageInfos);
    }

}
