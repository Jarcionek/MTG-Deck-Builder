package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.backend.TagsManager;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;
import java.awt.GridLayout;

public class TagViewer extends JPanel {

    private final TagsManager tagsManager;

    private final DefaultListModel<String> listModel;
    private final JList<String> list;

    public TagViewer(TagsManager tagsManager) {
        this.tagsManager = tagsManager;

        listModel = new DefaultListModel<>();
        refresh();

        list = new JList<>(listModel);
        setNames();
        createLayout();
    }

    public final void refresh() {
        listModel.removeAllElements();
        for (String tag : tagsManager.getAvailableTags()) {
            listModel.addElement(tag);
        }
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

}
