package mtgdeckbuilder.frontend.topics;

import mtgdeckbuilder.data.Filter;

import java.util.HashSet;
import java.util.Set;

public class AddFilterTopic {

    private final Set<Subscriber> subscribers = new HashSet<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void post(Filter filter) {
        for (Subscriber subscriber : subscribers) {
            subscriber.filterAdded(filter);
        }
    }

    public static interface Subscriber {
        void filterAdded(Filter filter);
    }

}
