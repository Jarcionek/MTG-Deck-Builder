package mtgdeckbuilder.frontend.topics;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TagTopicTest {

    private TagTopic.Subscriber subscriberOne = mock(TagTopic.Subscriber.class);
    private TagTopic.Subscriber subscriberTwo = mock(TagTopic.Subscriber.class);

    private TagTopic topic = new TagTopic();

    @Before
    public void addSubscribers() {
        topic.addSubscriber(subscriberOne);
        topic.addSubscriber(subscriberTwo);
    }

    @Test
    public void notifiesSubscribersAboutCardTagged() {
        topic.notifyCardTagged("card", "tag");

        verify(subscriberOne, times(1)).cardTagged("card", "tag");
        verify(subscriberTwo, times(1)).cardTagged("card", "tag");
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

    @Test
    public void notifiesSubscribersAboutCardUntagged() {
        topic.notifyCardUntagged("cardName", "tagName");

        verify(subscriberOne, times(1)).cardUntagged("cardName", "tagName");
        verify(subscriberTwo, times(1)).cardUntagged("cardName", "tagName");
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

    @Test
    public void notifiesSubscribersAboutNewTagCreated() {
        topic.notifyTagCreated("newTag");

        verify(subscriberOne, times(1)).tagCreated("newTag");
        verify(subscriberTwo, times(1)).tagCreated("newTag");
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

    @Test
    public void notifiesSubscribersAboutTagSelected() {
        topic.notifyTagSelected("newTag");

        verify(subscriberOne, times(1)).tagSelected("newTag");
        verify(subscriberTwo, times(1)).tagSelected("newTag");
        verifyNoMoreInteractions(subscriberOne, subscriberTwo);
    }

}