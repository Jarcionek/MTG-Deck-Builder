package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.frontend.swingworkers.SearchProgressHarvest;
import mtgdeckbuilder.frontend.swingworkers.SearchSwingWorkerManager;
import mtgdeckbuilder.frontend.topics.SearchTopic;
import mtgdeckbuilder.frontend.topics.TagTopic;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchButtonPanel extends JPanel implements TagTopic.Subscriber {

    private final ActiveFiltersPanel activeFiltersPanel;
    private final CardsDisplayPanel cardsDisplayPanel;
    private final SearchSwingWorkerManager searchSwingWorkerManager;

    private final SearchTopic searchTopic;

    private final JButton searchButton;
    private final JLabel searchLabel;

    public SearchButtonPanel(ActiveFiltersPanel activeFiltersPanel,
                             CardsDisplayPanel cardsDisplayPanel,
                             SearchSwingWorkerManager searchSwingWorkerManager,
                             SearchTopic searchTopic,
                             TagTopic tagTopic) {
        this.activeFiltersPanel = activeFiltersPanel;
        this.cardsDisplayPanel = cardsDisplayPanel;
        this.searchSwingWorkerManager = searchSwingWorkerManager;
        this.searchTopic = searchTopic;

        this.searchButton = new JButton("Search");
        this.searchLabel = new JLabel("");
        setNames();
        configureComponents();
        createLayout();

        tagTopic.addSubscriber(this);
    }

    private void configureComponents() {
        this.searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTopic.notifyStartingSearch();
                List<Filter> filters = activeFiltersPanel.getFilters();
                if (filters.isEmpty()) {
                    searchLabel.setText("you need at least one filter to search");
                    return;
                }
                searchLabel.setText("fetching data");
                searchSwingWorkerManager.searchAndDownloadCardsInBackground(filters, new StatusUpdater());
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

    @Override
    public void cardTagged(String cardName, String tagName) {}

    @Override
    public void cardUntagged(String cardName, String tagName) {}

    @Override
    public void tagCreated(String tagName) {}

    @Override
    public void tagSelected(String tagName) {
        searchSwingWorkerManager.cancel();
        searchLabel.setText("showing " + tagName);
    }

    private class StatusUpdater implements SearchProgressHarvest {

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
            cardsDisplayPanel.load(searchSwingWorkerManager.getFoundCards());
        }

        @Override
        public void error() {
            searchLabel.setText("error");
        }

    }

}
