package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.topics.TagTopic;
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
    private TagTopic tagTopic = mock(TagTopic.class);

    private TagViewer tagViewer;

    @Test
    public void subscribesToTopic() {
        List<String> availableTags = newArrayList("one", "two", "three");
        when(tagsManager.getAvailableTags()).thenReturn(availableTags);

        tagViewer = new TagViewer(tagsManager, tagTopic);

        verify(tagTopic).addSubscriber(tagViewer);
    }

    @Test
    public void displaysAvailableTagsImmediatelyAfterCreation() {
        List<String> availableTags = newArrayList("one", "two", "three");
        when(tagsManager.getAvailableTags()).thenReturn(availableTags);

        tagViewer = new TagViewer(tagsManager, tagTopic);

        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);
        jlist.setSelectedIndices(new int[] {0, 1, 2, 3, 4, 5, 6});
        assertEquals(availableTags, jlist.getSelectedValuesList());
    }

    @Test
    public void reloadsTagsOnRefresh() {
        List<String> tagsOne = newArrayList("one", "two", "three");
        List<String> tagsTwo = newArrayList("ein", "zwei", "drei");
        when(tagsManager.getAvailableTags()).thenReturn(tagsOne).thenReturn(tagsTwo);
        tagViewer = new TagViewer(tagsManager, tagTopic);

        tagViewer.refresh(); //TODO Jarek: should this method be private and this test go away?

        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);
        jlist.setSelectedIndices(new int[] {0, 1, 2, 3, 4, 5, 6});
        assertEquals(tagsTwo, jlist.getSelectedValuesList());
    }

    @Test
    public void addsNewTagToTheTopOfTheListWhenTopicNotifiesAboutCreatingNewTag() {
        List<String> tags = newArrayList("1", "2", "3");
        when(tagsManager.getAvailableTags()).thenReturn(tags);
        tagViewer = new TagViewer(tagsManager, tagTopic);

        tagViewer.tagCreated("4");

        List<String> expectedTags = newArrayList("4", "1", "2", "3");
        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);
        jlist.setSelectedIndices(new int[] {0, 1, 2, 3, 4, 5, 6});
        assertEquals(expectedTags, jlist.getSelectedValuesList());
    }

    //TODO Jarek: selection listener and load cards in card viewer on selection change - make the list single-selection only

}