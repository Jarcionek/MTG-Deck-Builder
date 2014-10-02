package mtgdeckbuilder.frontend;

import java.util.HashSet;
import java.util.Set;

public class SearchTopic {

    private final Set<Subscriber> subscribers = new HashSet<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notifySearchFinished() {
        for (Subscriber subscriber : subscribers) {
            subscriber.searchFinished();
        }
    }

    public static interface Subscriber {
        void searchFinished();
    }

}
