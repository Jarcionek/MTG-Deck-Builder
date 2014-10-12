package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.frontend.swingworkers.SearchProgressHarvest;
import mtgdeckbuilder.frontend.swingworkers.SearchSwingWorkerManager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchButtonPanel extends JPanel {

    private final ActiveFiltersPanel activeFiltersPanel;
    private final CardsDisplayPanel cardsDisplayPanel;
    private final SearchSwingWorkerManager searchSwingWorkerManager;

    private final JButton searchButton;
    private final JLabel searchLabel;

    public SearchButtonPanel(ActiveFiltersPanel activeFiltersPanel, CardsDisplayPanel cardsDisplayPanel, SearchSwingWorkerManager searchSwingWorkerManager) {
        this.activeFiltersPanel = activeFiltersPanel;
        this.cardsDisplayPanel = cardsDisplayPanel;
        this.searchSwingWorkerManager = searchSwingWorkerManager;

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
                searchSwingWorkerManager.searchAndDownloadCardsInBackground(filters, new GuiUpdater());
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

    private class GuiUpdater implements SearchProgressHarvest {

        private int numberOfParts;

        @Override
        public void started(int numberOfParts) {
            this.numberOfParts = numberOfParts;
            searchLabel.setText("downloading - 0/" + numberOfParts);
        }

        @Override
        public void partDone(int partNumber) {
            searchLabel.setText("downloading - " + partNumber + "/" + numberOfParts);
        }

        @Override
        public void finished() {
            searchLabel.setText("showing " + numberOfParts + " cards");
            searchButton.setEnabled(true);
            cardsDisplayPanel.load(searchSwingWorkerManager.getFoundCards());
        }

        @Override
        public void error() {
            searchLabel.setText("error");
            searchButton.setEnabled(true);
        }

        @Override
        public void cancelled() {}

    }

}
