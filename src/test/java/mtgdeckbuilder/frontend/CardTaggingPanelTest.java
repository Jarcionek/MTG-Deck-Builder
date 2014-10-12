package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.frontend.topics.TagTopic;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import javax.swing.JButton;
import javax.swing.JComboBox;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static mtgdeckbuilder.util.FrontEndTestingUtils.click;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CardTaggingPanelTest {

    private static final String TAG_1 = "tag-1";
    private static final String TAG_2 = "tag-2";
    private static final String TAG_3 = "tag-3";
    private static final String TAG_4 = "tag-4";
    private static final List<String> AVAILABLE_TAGS = newArrayList(TAG_1, TAG_2, TAG_3);

    private CardsDisplayPanel cardsDisplayPanel = mock(CardsDisplayPanel.class);
    private TagsManager tagsManager = mock(TagsManager.class);
    private TagTopic tagTopic = mock(TagTopic.class);

    private CardTaggingPanel cardTaggingPanel;

    @Before
    public void setupMockAndInstantiateCardTaggingPanel() {
        when(tagsManager.getAvailableTags()).thenReturn(AVAILABLE_TAGS);

        cardTaggingPanel = new CardTaggingPanel(cardsDisplayPanel, tagsManager, tagTopic);
    }

    @Test
    public void showsComboBoxWithAvailableTagsAfterPanelIsCreated() {
        JComboBox<?> tagsComboBox = findComponentRecursively(cardTaggingPanel, "tagsComboBox", JComboBox.class);
        assertEquals(AVAILABLE_TAGS, itemsIn(tagsComboBox));
    }

    @Test
    public void tagsCardAndNotifiesTopicWhenPressingTagButton() {
        when(cardsDisplayPanel.getSelectedCard()).thenReturn("cardName");

        JComboBox<?> tagsComboBox = findComponentRecursively(cardTaggingPanel, "tagsComboBox", JComboBox.class);
        tagsComboBox.setSelectedItem(TAG_1);

        JButton tagButton = findComponentRecursively(cardTaggingPanel, "tagButton", JButton.class);
        click(tagButton);

        InOrder inOrder = inOrder(tagsManager, tagTopic);
        inOrder.verify(tagsManager).tag(TAG_1, "cardName");
        inOrder.verify(tagTopic, times(1)).notifyCardTagged(TAG_1, "cardName");
    }

    @Test
    public void untagsCardAndNotifiesTopicWhenPressingUntagButton() {
        when(cardsDisplayPanel.getSelectedCard()).thenReturn("cardName");

        JComboBox<?> tagsComboBox = findComponentRecursively(cardTaggingPanel, "tagsComboBox", JComboBox.class);
        tagsComboBox.setSelectedItem(TAG_3);

        JButton tagButton = findComponentRecursively(cardTaggingPanel, "untagButton", JButton.class);
        click(tagButton);

        InOrder inOrder = inOrder(tagsManager, tagTopic);
        inOrder.verify(tagsManager).untag(TAG_3, "cardName");
        inOrder.verify(tagTopic, times(1)).notifyCardUntagged(TAG_3, "cardName");
    }

    @Test
    public void subscribesToTagTopic() {
        verify(tagTopic).addSubscriber(cardTaggingPanel);
    }

    @Test
    public void addsNewlyCreatedTagToComboBox() {
        cardTaggingPanel.tagCreated(TAG_4);

        JComboBox<?> tagsComboBox = findComponentRecursively(cardTaggingPanel, "tagsComboBox", JComboBox.class);
        assertThat(itemsIn(tagsComboBox), containsInAnyOrder(TAG_1, TAG_2, TAG_3, TAG_4));
        assertEquals("newly added tag should be selected", TAG_4, tagsComboBox.getSelectedItem());
    }

    //TODO Jarek: crashes when no card is selected (e.g. search with no results)
    //TODO Jarek: make tag/untag buttons disabled depending whether selected card is in selected tag or not

    private static List<String> itemsIn(JComboBox<?> comboBox) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            list.add((String) comboBox.getItemAt(i));
        }
        return list;
    }

}