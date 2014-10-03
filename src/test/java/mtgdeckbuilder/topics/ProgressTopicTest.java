package mtgdeckbuilder.topics;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ProgressTopicTest {

    private ProgressTopic.Subscriber subscriberOne = mock(ProgressTopic.Subscriber.class);
    private ProgressTopic.Subscriber subscriberTwo = mock(ProgressTopic.Subscriber.class);

    private ProgressTopic topic = new ProgressTopic();

    @Before
    public void addSubscribers() {
        topic.addSubscriber(subscriberOne);
        topic.addSubscriber(subscriberTwo);

    }

    @Test
    public void notifiesSubscribersAboutWorkStarted() {
        topic.notifyWorkStarted(15);

        verify(subscriberOne, times(1)).workStarted(15);
        verify(subscriberTwo, times(1)).workStarted(15);
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

    @Test
    public void notifiesSubscribersAboutWorkUpdate() {
        topic.notifyWorkUpdate(2);

        verify(subscriberOne, times(1)).workUpdate(2);
        verify(subscriberTwo, times(1)).workUpdate(2);
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

    @Test
    public void notifiesSubscribersAboutWorkFinished() {
        topic.notifyWorkFinished();

        verify(subscriberOne, times(1)).workFinished();
        verify(subscriberTwo, times(1)).workFinished();
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

}
