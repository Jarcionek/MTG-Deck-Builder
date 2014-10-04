package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.Filter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchButtonPanel extends JPanel {

    private final ActiveFiltersPanel activeFiltersPanel;
    private final SearchSwingWorkerFactory searchSwingWorkerFactory;

    private final JButton searchButton;
    private final JLabel searchLabel;

    public SearchButtonPanel(ActiveFiltersPanel activeFiltersPanel, SearchSwingWorkerFactory searchSwingWorkerFactory) {
        this.activeFiltersPanel = activeFiltersPanel;
        this.searchSwingWorkerFactory = searchSwingWorkerFactory;

        this.searchButton = new JButton("Search");
        this.searchLabel = new JLabel("");
        setNames();
        configureComponents();
        createLayout();
    }

    private void configureComponents() {
        this.searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Filter> filters = activeFiltersPanel.getFilters();
                if (filters.isEmpty()) {
                    searchLabel.setText("you need at least one filter to search");
                    return;
                }
                searchButton.setEnabled(false);
                searchLabel.setText("fetching data");
                searchSwingWorkerFactory.newSearchSwingWorker(searchButton, searchLabel, filters).execute();
            }
        });
    }

    @TestCode
    private void setNames() {
        this.searchButton.setName("searchButton");
        this.searchLabel.setName("searchLabel");
    }

    private void createLayout() {
        this.setLayout(new BorderLayout());
        this.add(searchButton, BorderLayout.EAST);
        this.add(searchLabel, BorderLayout.CENTER);
        searchLabel.setHorizontalAlignment(JLabel.CENTER);
    }

}
