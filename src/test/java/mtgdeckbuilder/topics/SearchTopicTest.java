package mtgdeckbuilder.topics;

import com.google.common.collect.Sets;
import mtgdeckbuilder.data.CardImageInfo;
import org.junit.Test;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class SearchTopicTest {

    private static final Set<CardImageInfo> CARD_IMAGE_INFOS = Sets.newHashSet(new CardImageInfo(1, "2"));

    private SearchTopic topic = new SearchTopic();

    @Test
    public void notifiesSubscribers() {
        SearchTopic.Subscriber subscriberOne = mock(SearchTopic.Subscriber.class);
        SearchTopic.Subscriber subscriberTwo = mock(SearchTopic.Subscriber.class);
        topic.addSubscriber(subscriberOne);
        topic.addSubscriber(subscriberTwo);

        topic.notifySearchFinished(CARD_IMAGE_INFOS);

        verify(subscriberOne, times(1)).searchFinished(CARD_IMAGE_INFOS);
        verify(subscriberTwo, times(1)).searchFinished(CARD_IMAGE_INFOS);
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

}
