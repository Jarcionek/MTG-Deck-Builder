package mtgdeckbuilder.frontend;

import mtgdeckbuilder.topics.SearchTopic;

public class SearchedCardsDisplayPanel extends CardsDisplayPanel implements SearchTopic.Subscriber {

    public SearchedCardsDisplayPanel(CardImageLoader cardImageLoader, SearchTopic searchTopic) {
        super(cardImageLoader);
        searchTopic.addSubscriber(this);
    }

    @Override
    public void searchFinished(Iterable<String> cardNames) {
        load(cardNames);
    }

}
