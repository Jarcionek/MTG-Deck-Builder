package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.frontend.topics.SearchTopic;
import mtgdeckbuilder.frontend.topics.TagTopic;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.GridLayout;

public class TagViewer extends JPanel implements TagTopic.Subscriber, SearchTopic.Subscriber {

    private final TagsManager tagsManager;
    private final CardsDisplayPanel cardsDisplayPanel;
    private final TagTopic tagTopic;

    private final DefaultListModel<String> listModel;
    private final JList<String> list;

    public TagViewer(TagsManager tagsManager, CardsDisplayPanel cardsDisplayPanel, SearchTopic searchTopic, TagTopic tagTopic) {
        this.tagsManager = tagsManager;
        this.cardsDisplayPanel = cardsDisplayPanel;
        this.tagTopic = tagTopic;

        this.listModel = new DefaultListModel<>();
        this.list = new JList<>();

        configureComponents();
        setNames();
        createLayout();

        searchTopic.addSubscriber(this);
        tagTopic.addSubscriber(this);
    }

    private void configureComponents() {
        listModel.removeAllElements();
        for (String tag : tagsManager.getAvailableTags()) {
            listModel.addElement(tag);
        }

        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tagTopic.notifyTagSelected(list.getSelectedValue());
                cardsDisplayPanel.load(tagsManager.getCards(list.getSelectedValue()));
            }
        });
    }

    private void createLayout() {
        this.setLayout(new GridLayout(1, 1));
        JScrollPane scrollPane = new JScrollPane(list);
        this.add(scrollPane);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        Dimension size = list.getPreferredSize();
        size.width += 5;
        size.width += (int) scrollPane.getVerticalScrollBar().getPreferredSize().getWidth();

        this.setPreferredSize(size);
        this.setMinimumSize(size);
    }

    @TestCode
    private void setNames() {
        list.setName("jlist");
    }

    @Override
    public void cardTagged(String cardName, String tagName) {}

    @Override
    public void cardUntagged(String cardName, String tagName) {}

    @Override
    public void tagCreated(String tagName) {
        listModel.add(0, tagName);
    }

    @Override
    public void tagSelected(String tagName) {}

    @Override
    public void startingSearch() {
        list.clearSelection();
    }

}
