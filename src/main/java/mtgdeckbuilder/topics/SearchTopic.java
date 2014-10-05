package mtgdeckbuilder.topics;

import java.util.HashSet;
import java.util.Set;

public class SearchTopic {

    private final Set<Subscriber> subscribers = new HashSet<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notifySearchFinished(Iterable<String> cardNames) {
        for (Subscriber subscriber : subscribers) {
            subscriber.searchFinished(cardNames);
        }
    }

    public static interface Subscriber {
        void searchFinished(Iterable<String> cardNames);
    }

}
