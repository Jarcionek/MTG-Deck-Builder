package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.frontend.topics.AddFilterTopic;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ActiveFiltersPanel extends JPanel implements AddFilterTopic.Subscriber {

    private final JPanel innerPanel;
    private final List<Filter> filters;
    private final GridBagConstraints gridBagConstraints;

    @TestCode private int filterCount = 0;

    public ActiveFiltersPanel(AddFilterTopic addFilterTopic) {
        addFilterTopic.addSubscriber(this);

        this.innerPanel = new JPanel(new GridBagLayout());
        this.setLayout(new GridLayout(1, 1));
        this.add(new JScrollPane(innerPanel));
        this.filters = new ArrayList<>();

        this.gridBagConstraints = new GridBagConstraints();
        this.gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.gridBagConstraints.weightx = 1;
        this.gridBagConstraints.gridx = 0;
    }

    @Override
    public void filterAdded(final Filter filter) {
        filters.add(filter);

        final JPanel rowPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(filter.getField() + " " + filter.getFunction() + " " + filter.getArgument());
        label.setBorder(new EmptyBorder(0, 8, 0, 0));
        label.setFont(new Font("arial", Font.PLAIN, 12));
        rowPanel.add(label, BorderLayout.CENTER);

        JButton button = new JButton("-");
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActiveFiltersPanel.this.filters.remove(filter);
                ActiveFiltersPanel.this.innerPanel.remove(rowPanel);
                ActiveFiltersPanel.this.revalidate();
                ActiveFiltersPanel.this.repaint();
            }
        });
        rowPanel.add(button, BorderLayout.EAST);

        setNames(label, button);

        this.innerPanel.add(rowPanel, gridBagConstraints);
        this.revalidate();
        this.repaint();
    }

    @TestCode
    private void setNames(JLabel label, JButton button) {
        label.setName("filterLabel" + filterCount);
        button.setName("deleteFilterButton" + filterCount);
        filterCount++;
    }

    public List<Filter> getFilters() {
        return filters;
    }

}
