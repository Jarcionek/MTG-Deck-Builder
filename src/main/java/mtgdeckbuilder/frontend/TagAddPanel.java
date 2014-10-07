package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.topics.TagTopic;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TagAddPanel extends JPanel {

    private final TagsManager tagsManager;
    private final TagTopic tagTopic;

    private final JTextField textField;
    private final JButton createButton;

    public TagAddPanel(TagsManager tagsManager, TagTopic tagTopic) {
        this.tagsManager = tagsManager;
        this.tagTopic = tagTopic;

        textField = new JTextField();
        createButton = new JButton("create");

        setNames();
        addListeners();
        createLayout();
    }

    private void addListeners() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().isEmpty()) {
                    tagsManager.createEmptyTag(textField.getText());
                    tagTopic.notifyTagCreated(textField.getText());
                    textField.setText("");
                }
            }
        };
        textField.addActionListener(actionListener);
        createButton.addActionListener(actionListener);
    }

    private void createLayout() {
        this.add(textField);
        this.add(createButton);
    }

    @TestCode
    private void setNames() {
        textField.setName("newTagField");
        createButton.setName("createTagButton");
    }

}
