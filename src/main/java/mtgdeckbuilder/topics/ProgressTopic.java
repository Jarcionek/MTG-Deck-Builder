package mtgdeckbuilder.topics;

import java.util.HashSet;
import java.util.Set;

public class ProgressTopic {

    private final Set<Subscriber> subscribers = new HashSet<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notifyWorkStarted(int totalParts) {
        for (Subscriber subscriber : subscribers) {
            subscriber.workStarted(totalParts);
        }
    }

    public void notifyWorkUpdate(int partDone) {
        for (Subscriber subscriber : subscribers) {
            subscriber.workUpdate(partDone);
        }
    }

    public void notifyWorkFinished() {
        for (Subscriber subscriber : subscribers) {
            subscriber.workFinished();
        }
    }

    public static interface Subscriber {
        void workStarted(int totalParts);
        void workUpdate(int partDone);
        void workFinished();
    }

}
