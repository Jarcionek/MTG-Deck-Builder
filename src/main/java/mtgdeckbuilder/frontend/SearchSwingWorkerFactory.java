package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.data.Filter;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.List;

public class SearchSwingWorkerFactory {

    private final FilterToUrlConverter filterToUrlConverter;
    private final UrlDownloader urlDownloader;
    private final JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter;
    private final CardImageDownloader cardImageDownloader;

    public SearchSwingWorkerFactory(FilterToUrlConverter filterToUrlConverter,
                                    UrlDownloader urlDownloader,
                                    JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter,
                                    CardImageDownloader cardImageDownloader) {
        this.filterToUrlConverter = filterToUrlConverter;
        this.urlDownloader = urlDownloader;
        this.jsonToCardsImageInfosConverter = jsonToCardsImageInfosConverter;
        this.cardImageDownloader = cardImageDownloader;
    }

    public SearchSwingWorker newSearchSwingWorker(JButton searchButton, JLabel searchLabel, List<Filter> filters) {
        return new SearchSwingWorker(
                filterToUrlConverter,
                urlDownloader,
                jsonToCardsImageInfosConverter,
                cardImageDownloader,
                cardImageDownloader.getProgressTopic(),
                searchButton,
                searchLabel,
                filters
        );
    }

}
