package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.frontend.topics.TagTopic;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JTextField;

import static mtgdeckbuilder.util.FrontEndTestingUtils.click;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static mtgdeckbuilder.util.FrontEndTestingUtils.pressEnterIn;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class TagAddPanelTest {

    private static final String TAG_NAME_ONE = "this-is-a-new-tag";
    private static final String TAG_NAME_TWO = "tagName";
    private static final String TAG_NAME_THREE = "justAnotherTag";
    private static final String TAG_NAME_EMPTY_STRING = "";

    private TagsManager tagsManager = mock(TagsManager.class);
    private TagTopic tagTopic = mock(TagTopic.class);

    private TagAddPanel tagAddPanel = new TagAddPanel(tagsManager, tagTopic);

    @Test
    public void notifiesTheTopicWhenNewTagIsAddedByPressingButton() {
        setTextAndPressButton(TAG_NAME_ONE);

        verify(tagTopic, times(1)).notifyTagCreated(TAG_NAME_ONE);
    }

    @Test
    public void notifiesTheTopicWhenNewTagIsAddedByPressingEnter() {
        setTextAndPressEnter(TAG_NAME_TWO);

        verify(tagTopic, times(1)).notifyTagCreated(TAG_NAME_TWO);
    }

    @Test
    public void clearsTextFieldAfterPressingButton() {
        setTextAndPressButton(TAG_NAME_THREE);

        JTextField newTagField = findComponentRecursively(tagAddPanel, "newTagField", JTextField.class);
        assertThat("text field should have no text", newTagField.getText(), is(equalTo("")));
    }

    @Test
    public void clearsTextFieldAfterPressingEnter() {
        setTextAndPressEnter(TAG_NAME_ONE);

        JTextField newTagField = findComponentRecursively(tagAddPanel, "newTagField", JTextField.class);
        assertThat("text field should have no text", newTagField.getText(), is(equalTo("")));
    }

    @Test
    public void createsEmptyTagWhenNewTagIsAddedByPressingButton() {
        setTextAndPressButton(TAG_NAME_TWO);

        verify(tagsManager).createEmptyTag(TAG_NAME_TWO);
    }

    @Test
    public void createsEmptyTagWhenNewTagIsAddedByPressingEnter() {
        setTextAndPressEnter(TAG_NAME_THREE);

        verify(tagsManager).createEmptyTag(TAG_NAME_THREE);
    }

    @Test
    public void doesNotCreateEmptyTagWhenButtonIsPressedWhileTextFieldIsEmpty() {
        setTextAndPressButton(TAG_NAME_EMPTY_STRING);

        verifyZeroInteractions(tagsManager);
    }

    @Test
    public void doesNotCreateEmptyTagWhenEnterIsPressedWhileTextFieldIsEmpty() {
        setTextAndPressEnter(TAG_NAME_EMPTY_STRING);

        verifyZeroInteractions(tagsManager);
    }


    private void setTextAndPressButton(String tagNameOne) {
        JTextField newTagField = findComponentRecursively(tagAddPanel, "newTagField", JTextField.class);
        newTagField.setText(tagNameOne);

        JButton createTagButton = findComponentRecursively(tagAddPanel, "createTagButton", JButton.class);
        click(createTagButton);
    }

    private void setTextAndPressEnter(String tagNameEmptyString) {
        JTextField newTagField = findComponentRecursively(tagAddPanel, "newTagField", JTextField.class);
        newTagField.setText(tagNameEmptyString);

        pressEnterIn(newTagField);
    }

}
