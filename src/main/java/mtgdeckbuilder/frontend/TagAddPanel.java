package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.frontend.topics.TagTopic;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
        createButton = new JButton("create new tag");

        setNames();
        addListeners();
        createLayout();
    }

    private void addListeners() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().isEmpty()) {
                    //TODO Jarek: I don't think this should be happening if the tag already exists
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
        this.setLayout(new GridLayout(2, 1));
        this.add(textField, BorderLayout.CENTER);
        this.add(createButton, BorderLayout.EAST);
    }

    @TestCode
    private void setNames() {
        textField.setName("newTagField");
        createButton.setName("createTagButton");
    }

}
