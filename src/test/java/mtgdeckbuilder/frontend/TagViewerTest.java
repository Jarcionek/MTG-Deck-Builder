package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.frontend.topics.SearchTopic;
import mtgdeckbuilder.frontend.topics.TagTopic;
import org.junit.Test;

import javax.swing.JList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagViewerTest {

    private TagsManager tagsManager = mock(TagsManager.class);
    private CardsDisplayPanel cardsDisplayPanel = mock(CardsDisplayPanel.class);
    private SearchTopic searchTopic = mock(SearchTopic.class);
    private TagTopic tagTopic = mock(TagTopic.class);

    private TagViewer tagViewer;

    @Test
    public void subscribesToTagTopic() {
        // given
        List<String> availableTags = newArrayList("one", "two", "three");
        when(tagsManager.getAvailableTags()).thenReturn(availableTags);

        // when
        tagViewer = new TagViewer(tagsManager, cardsDisplayPanel, searchTopic, tagTopic);

        // then
        verify(tagTopic).addSubscriber(tagViewer);
    }

    @Test
    public void displaysAvailableTagsImmediatelyAfterCreation() {
        // given
        List<String> availableTags = newArrayList("one", "two", "three");
        when(tagsManager.getAvailableTags()).thenReturn(availableTags);

        // when
        tagViewer = new TagViewer(tagsManager, cardsDisplayPanel, searchTopic, tagTopic);

        // then
        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);
        assertEquals(availableTags, valuesIn(jlist));
    }

    @Test
    public void addsNewTagToTheTopOfTheListWhenTopicNotifiesAboutCreatingNewTag() {
        // given
        List<String> tags = newArrayList("1", "2", "3");
        when(tagsManager.getAvailableTags()).thenReturn(tags);
        tagViewer = new TagViewer(tagsManager, cardsDisplayPanel, searchTopic, tagTopic);

        // when
        tagViewer.tagCreated("4");

        // then
        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);
        List<String> expectedTags = newArrayList("4", "1", "2", "3");
        assertEquals(expectedTags, valuesIn(jlist));
    }

    @Test
    public void notifiesTopicWhenSelectingTag() {
        // given
        List<String> tags = newArrayList("1", "tag", "3");
        when(tagsManager.getAvailableTags()).thenReturn(tags);
        tagViewer = new TagViewer(tagsManager, cardsDisplayPanel, searchTopic, tagTopic);
        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);

        // when
        jlist.setSelectedIndex(1);

        // then
        verify(tagTopic).notifyTagSelected("tag");
    }

    @Test
    public void loadsCardsFromTagWhenSelectingTag() {
        // given
        List<String> tags = newArrayList("tag-1", "tag-2", "tag-3", "tag-4");
        List<String> cards = newArrayList("card-1", "card-2", "card-3");
        when(tagsManager.getAvailableTags()).thenReturn(tags);
        when(tagsManager.getCards("tag-4")).thenReturn(cards);
        tagViewer = new TagViewer(tagsManager, cardsDisplayPanel, searchTopic, tagTopic);
        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);

        // when
        jlist.setSelectedIndex(3);

        // then
        verify(cardsDisplayPanel).load(cards);
    }

    @Test
    public void subscribesToSearchTopic() {
        // given
        when(tagsManager.getAvailableTags()).thenReturn(newArrayList("one", "two", "three"));

        // when
        tagViewer = new TagViewer(tagsManager, cardsDisplayPanel, searchTopic, tagTopic);

        // then
        verify(searchTopic).addSubscriber(tagViewer);
    }

    @Test
    public void clearsSelectionWhenSearchTopicNotifiesAboutStartingSearch() {
        // given
        when(tagsManager.getAvailableTags()).thenReturn(newArrayList("one", "two", "three"));
        tagViewer = new TagViewer(tagsManager, cardsDisplayPanel, searchTopic, tagTopic);
        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);
        jlist.setSelectedIndex(2);

        // when
        tagViewer.startingSearch();

        // then
        assertEquals("Should have no selection but selected index was incorrect", -1, jlist.getSelectedIndex());
    }


    private static List<?> valuesIn(JList<?> jlist) {
        List<Object> list = newArrayList();
        for (int i = 0; i < jlist.getModel().getSize(); i++) {
            list.add(jlist.getModel().getElementAt(i));
        }
        return list;
    }

}