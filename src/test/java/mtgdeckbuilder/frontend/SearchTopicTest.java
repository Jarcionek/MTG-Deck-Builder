package mtgdeckbuilder.frontend;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class SearchTopicTest {

    private SearchTopic topic = new SearchTopic();

    @Test
    public void notifiesSubscribers() {
        SearchTopic.Subscriber subscriberOne = mock(SearchTopic.Subscriber.class);
        SearchTopic.Subscriber subscriberTwo = mock(SearchTopic.Subscriber.class);
        topic.addSubscriber(subscriberOne);
        topic.addSubscriber(subscriberTwo);

        topic.notifySearchFinished();

        verify(subscriberOne, times(1)).searchFinished();
        verify(subscriberTwo, times(1)).searchFinished();
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

}
