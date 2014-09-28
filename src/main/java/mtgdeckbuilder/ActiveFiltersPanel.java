package mtgdeckbuilder;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActiveFiltersPanel extends JPanel {

    private final JPanel innerPanel;

    @TestCode private int filterCount = 0;

    public ActiveFiltersPanel() {
        this.innerPanel = new JPanel(new GridLayout(0, 1));
        this.setLayout(new GridLayout(1, 1));
        this.add(new JScrollPane(innerPanel));
    }

    public void addFilter(Filter filter) {
        final JPanel rowPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(filter.getField() + " " + filter.getFunction() + " " + filter.getArgument());
        label.setFont(new Font("arial", Font.PLAIN, 12));
        rowPanel.add(label, BorderLayout.CENTER);

        JButton button = new JButton("-");
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActiveFiltersPanel.this.innerPanel.remove(rowPanel);
                ActiveFiltersPanel.this.revalidate();
                ActiveFiltersPanel.this.repaint();
            }
        });
        rowPanel.add(button, BorderLayout.EAST);

        setNames(label, button);

        this.innerPanel.add(rowPanel);
    }

    @TestCode
    private void setNames(JLabel label, JButton button) {
        label.setName("filterLabel" + filterCount);
        button.setName("deleteFilterButton" + filterCount);
        filterCount++;
    }

}
