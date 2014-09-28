package mtgdeckbuilder;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActiveFiltersPanel extends JPanel {

    private int filterCount = 0;

    public ActiveFiltersPanel() {
        this.setLayout(new GridLayout(0, 1));
    }

    public void addFilter(Filter filter) {
        final JPanel rowPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(filter.getField() + " " + filter.getFunction() + " " + filter.getArgument());
        label.setName("filterLabel" + filterCount);
        label.setFont(new Font("arial", Font.PLAIN, 12));
        rowPanel.add(label, BorderLayout.CENTER);

        JButton button = new JButton("-");
        button.setName("deleteFilterButton" + filterCount++);
        button.setFocusable(false);
        rowPanel.add(button, BorderLayout.EAST);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActiveFiltersPanel.this.remove(rowPanel);
                ActiveFiltersPanel.this.revalidate();
                ActiveFiltersPanel.this.repaint();
            }
        });

        this.add(rowPanel);
    }

}
