package mtgdeckbuilder.topics;

import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class SearchTopicTest {

    private static final Iterable<String> CARD_NAMES = newArrayList("abc", "def", "ghi");

    private SearchTopic topic = new SearchTopic();

    @Test
    public void notifiesSubscribers() {
        SearchTopic.Subscriber subscriberOne = mock(SearchTopic.Subscriber.class);
        SearchTopic.Subscriber subscriberTwo = mock(SearchTopic.Subscriber.class);
        topic.addSubscriber(subscriberOne);
        topic.addSubscriber(subscriberTwo);

        topic.notifySearchFinished(CARD_NAMES);

        verify(subscriberOne, times(1)).searchFinished(CARD_NAMES);
        verify(subscriberTwo, times(1)).searchFinished(CARD_NAMES);
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

}
