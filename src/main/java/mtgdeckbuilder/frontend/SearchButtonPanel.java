package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;

import javax.swing.JLabel;

//TODO Jarek: implement!
/*
 * - contains a search button
 * - (delegates) takes filters from ActiveFiltersPanel, converts to url and makes a query
 * - contains label to display progress/errors
 * - (delegates) converts retrieved json to CardImageInfos
 * - (delegates) downloads card images
 * - notifies about finished job (some listener)
 */
public class SearchButtonPanel {

    private ActiveFiltersPanel activeFiltersPanel;
    private FilterToUrlConverter filterToUrlConverter;
    private UrlDownloader urlDownloader;
    private JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter;
    private CardImageDownloader cardImageDownloader;
    private JLabel infoLabel;

}
