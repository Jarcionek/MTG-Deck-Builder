package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Url;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

public class SearchButtonPanel extends JPanel {

    private final ActiveFiltersPanel activeFiltersPanel;
    private final FilterToUrlConverter filterToUrlConverter;
    private final UrlDownloader urlDownloader;
    private final JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter;
    private final CardImageDownloader cardImageDownloader;
    private final SearchTopic searchTopic;

    private final JButton searchButton;

    public SearchButtonPanel(ActiveFiltersPanel activeFiltersPanel,
                             FilterToUrlConverter filterToUrlConverter,
                             UrlDownloader urlDownloader,
                             JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter,
                             CardImageDownloader cardImageDownloader,
                             SearchTopic searchTopic) {
        this.activeFiltersPanel = activeFiltersPanel;
        this.filterToUrlConverter = filterToUrlConverter;
        this.urlDownloader = urlDownloader;
        this.jsonToCardsImageInfosConverter = jsonToCardsImageInfosConverter;
        this.cardImageDownloader = cardImageDownloader;
        this.searchTopic = searchTopic;

        this.searchButton = new JButton("Search");
        setNames();
        configureComponents();

        this.add(searchButton);
    }

    private void configureComponents() {
        this.searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Filter> filters = activeFiltersPanel.getFilters();
                if (filters.isEmpty()) {
                    return;
                }
                String url = filterToUrlConverter.convert(filters);
                String json = urlDownloader.download(new Url(url));
                Set<CardImageInfo> cardImageInfos = jsonToCardsImageInfosConverter.convert(json);
                cardImageDownloader.download(cardImageInfos);
                searchTopic.notifySearchFinished();
            }
        });
    }

    @TestCode
    private void setNames() {
        this.searchButton.setName("searchButton");
    }

}
