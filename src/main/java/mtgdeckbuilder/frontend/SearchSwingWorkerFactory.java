package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.topics.SearchTopic;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.List;

public class SearchSwingWorkerFactory {

    private final FilterToUrlConverter filterToUrlConverter;
    private final UrlDownloader urlDownloader;
    private final JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter;
    private final CardImageDownloader cardImageDownloader;
    private final SearchTopic searchTopic;

    public SearchSwingWorkerFactory(FilterToUrlConverter filterToUrlConverter,
                                    UrlDownloader urlDownloader,
                                    JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter,
                                    CardImageDownloader cardImageDownloader,
                                    SearchTopic searchTopic) {
        this.filterToUrlConverter = filterToUrlConverter;
        this.urlDownloader = urlDownloader;
        this.jsonToCardsImageInfosConverter = jsonToCardsImageInfosConverter;
        this.cardImageDownloader = cardImageDownloader;
        this.searchTopic = searchTopic;
    }

    public SearchSwingWorker newSearchSwingWorker(JButton searchButton, JLabel searchLabel, List<Filter> filters) {
        return new SearchSwingWorker(
                filterToUrlConverter,
                urlDownloader,
                jsonToCardsImageInfosConverter,
                cardImageDownloader,
                searchTopic,
                cardImageDownloader.getProgressTopic(),
                searchButton,
                searchLabel,
                filters
        );
    }

}
