package mtgdeckbuilder.topics;

import java.util.HashSet;
import java.util.Set;

public class TagTopic {

    private final Set<Subscriber> subscribers = new HashSet<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notifyCardTagged(String cardName, String tagName) {
        for (Subscriber subscriber : subscribers) {
            subscriber.cardTagged(cardName, tagName);
        }
    }

    public void notifyCardUntagged(String cardName, String tagName) {
        for (Subscriber subscriber : subscribers) {
            subscriber.cardUntagged(cardName, tagName);
        }
    }

    public void notifyTagCreated(String tagName) {
        for (Subscriber subscriber : subscribers) {
            subscriber.tagCreated(tagName);
        }
    }

    public static interface Subscriber {
        void cardTagged(String cardName, String tagName);
        void cardUntagged(String cardName, String tagName);
        void tagCreated(String tagName);
    }

}