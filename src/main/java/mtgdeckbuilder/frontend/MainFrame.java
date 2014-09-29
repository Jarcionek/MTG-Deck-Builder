package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.Filter;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {

    private final NewFilterPanel newFilterPanel;
    private final ActiveFiltersPanel activeFiltersPanel;

    public MainFrame() {
        this.setSize(640, 480);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.newFilterPanel = new NewFilterPanel();
        this.activeFiltersPanel = new ActiveFiltersPanel();

        AddFilterListener addFilterListener = new AddFilterListener() {
            @Override
            public void addedFilter(Filter filter) {
                activeFiltersPanel.addFilter(filter);
            }
        };
        newFilterPanel.setAddFilterListener(addFilterListener);

        this.setLayout(new BorderLayout());
        this.add(newFilterPanel, BorderLayout.NORTH);
        this.add(activeFiltersPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

}
