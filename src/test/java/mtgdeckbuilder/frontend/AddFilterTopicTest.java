package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.Field;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Function;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AddFilterTopicTest {

    private static final Filter FILTER = new Filter(Field.artist, Function.gte, "123");

    private AddFilterTopic addFilterTopic = new AddFilterTopic();

    @Test
    public void notifiesSubscribers() {
        AddFilterTopic.Subscriber subscriberOne = mock(AddFilterTopic.Subscriber.class);
        AddFilterTopic.Subscriber subscriberTwo = mock(AddFilterTopic.Subscriber.class);
        addFilterTopic.addSubscriber(subscriberOne);
        addFilterTopic.addSubscriber(subscriberTwo);

        addFilterTopic.post(FILTER);

        verify(subscriberOne, times(1)).filterAdded(FILTER);
        verify(subscriberTwo, times(1)).filterAdded(FILTER);
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

}
