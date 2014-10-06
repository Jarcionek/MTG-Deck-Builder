package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.TagsManager;
import org.junit.Test;

import javax.swing.JList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TagViewerTest {

    private TagsManager tagsManager = mock(TagsManager.class);

    private TagViewer tagViewer;

    @Test
    public void displaysTagsImmediatelyAfterCreation() {
        List<String> availableTags = newArrayList("one", "two", "three");
        when(tagsManager.getAvailableTags()).thenReturn(availableTags);

        tagViewer = new TagViewer(tagsManager);

        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);
        jlist.setSelectedIndices(new int[] {0, 1, 2, 3, 4, 5, 6});
        assertEquals(availableTags, jlist.getSelectedValuesList());
        assertEquals(availableTags, jlist.getSelectedValuesList());
    }

    @Test
    public void reloadsTagsOnRefresh() {
        List<String> tagsOne = newArrayList("one", "two", "three");
        List<String> tagsTwo = newArrayList("ein", "zwei", "drei");
        when(tagsManager.getAvailableTags()).thenReturn(tagsOne).thenReturn(tagsTwo);
        tagViewer = new TagViewer(tagsManager);

        tagViewer.refresh();

        JList<?> jlist = findComponentRecursively(tagViewer, "jlist", JList.class);
        jlist.setSelectedIndices(new int[] {0, 1, 2, 3, 4, 5, 6});
        assertEquals(tagsTwo, jlist.getSelectedValuesList());
    }

}