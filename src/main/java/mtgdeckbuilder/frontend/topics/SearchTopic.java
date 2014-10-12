package mtgdeckbuilder.frontend.topics;

import java.util.HashSet;
import java.util.Set;

public class SearchTopic {

    private final Set<Subscriber> subscribers = new HashSet<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notifySearchStarted() {
        for (Subscriber subscriber : subscribers) {
            subscriber.searchStarted();
        }
    }

    public static interface Subscriber {
        void searchStarted();
    }

}
