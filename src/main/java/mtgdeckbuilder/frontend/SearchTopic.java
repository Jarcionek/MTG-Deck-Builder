package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.CardImageInfo;

import java.util.HashSet;
import java.util.Set;

public class SearchTopic {

    private final Set<Subscriber> subscribers = new HashSet<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notifySearchFinished(Set<CardImageInfo> cardImageInfos) {
        for (Subscriber subscriber : subscribers) {
            subscriber.searchFinished(cardImageInfos);
        }
    }

    public static interface Subscriber {
        void searchFinished(Set<CardImageInfo> cardImageInfos);
    }

}
