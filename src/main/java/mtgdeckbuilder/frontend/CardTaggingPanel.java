package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.frontend.topics.TagTopic;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CardTaggingPanel extends JPanel implements TagTopic.Subscriber {

    private final CardsDisplayPanel cardsDisplayPanel;
    private final TagsManager tagsManager;
    private final TagTopic tagTopic;

    private final JComboBox<String> jComboBox;
    private final JButton tagButton;
    private final JButton untagButton;

    public CardTaggingPanel(CardsDisplayPanel cardsDisplayPanel, TagsManager tagsManager, TagTopic tagTopic) {
        this.cardsDisplayPanel = cardsDisplayPanel;
        this.tagsManager = tagsManager;
        this.tagTopic = tagTopic;
        this.jComboBox = new JComboBox<>(array(tagsManager.getAvailableTags()));
        this.tagButton = new JButton("tag");
        this.untagButton = new JButton("untag");

        setNames();
        addListeners();
        createLayout();

        tagTopic.addSubscriber(this);
    }

    private void addListeners() {
        tagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagsManager.tag((String) jComboBox.getSelectedItem(), cardsDisplayPanel.getSelectedCard());
                tagTopic.notifyCardTagged((String) jComboBox.getSelectedItem(), cardsDisplayPanel.getSelectedCard());
            }
        });
        untagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagsManager.untag((String) jComboBox.getSelectedItem(), cardsDisplayPanel.getSelectedCard());
                tagTopic.notifyCardUntagged((String) jComboBox.getSelectedItem(), cardsDisplayPanel.getSelectedCard());
            }
        });
    }

    private void createLayout() {
        add(jComboBox);
        add(tagButton);
        add(untagButton);
    }

    @Override
    public void cardTagged(String cardName, String tagName) {}

    @Override
    public void cardUntagged(String cardName, String tagName) {}

    @Override
    public void tagCreated(String tagName) {
        jComboBox.addItem(tagName);
        jComboBox.setSelectedItem(tagName);
    }

    @Override
    public void tagSelected(String tagName) {}

    @TestCode
    private void setNames() {
        jComboBox.setName("tagsComboBox");
        tagButton.setName("tagButton");
        untagButton.setName("untagButton");
    }


    private static String[] array(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

}
