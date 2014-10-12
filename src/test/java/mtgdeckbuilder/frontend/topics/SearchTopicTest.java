package mtgdeckbuilder.frontend.topics;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class SearchTopicTest {

    private SearchTopic.Subscriber subscriberOne = mock(SearchTopic.Subscriber.class);
    private SearchTopic.Subscriber subscriberTwo = mock(SearchTopic.Subscriber.class);

    private SearchTopic topic = new SearchTopic();

    @Before
    public void addSubscribers() {
        topic.addSubscriber(subscriberOne);
        topic.addSubscriber(subscriberTwo);
    }

    @Test
    public void notifiesSubscribersAboutSearchStarted() {
        topic.notifySearchStarted();

        verify(subscriberOne, times(1)).searchStarted();
        verify(subscriberTwo, times(1)).searchStarted();
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

}