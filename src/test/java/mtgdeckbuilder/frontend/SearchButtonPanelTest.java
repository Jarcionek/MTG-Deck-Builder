package mtgdeckbuilder.frontend;

import com.google.common.collect.ImmutableSet;
import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Url;
import mtgdeckbuilder.topics.SearchTopic;
import org.junit.Test;

import javax.swing.JButton;
import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static java.util.Arrays.asList;
import static mtgdeckbuilder.data.Field.color;
import static mtgdeckbuilder.data.Field.manacost;
import static mtgdeckbuilder.data.Function.eq;
import static mtgdeckbuilder.data.Function.lt;
import static mtgdeckbuilder.util.FrontEndTestingUtils.click;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class SearchButtonPanelTest {

    private static final List<Filter> FILTERS = asList(new Filter(color, eq, "blue"), new Filter(manacost, lt, "3"));
    private static final String URL = "blah.com";
    private static final String JSON = "this is cards data in json format";
    private static final ImmutableSet<CardImageInfo> CARD_IMAGE_INFOS = ImmutableSet.of(new CardImageInfo(1, "name1"), new CardImageInfo(2, "name2"));

    private ActiveFiltersPanel activeFiltersPanel = mock(ActiveFiltersPanel.class);
    private FilterToUrlConverter filterToUrlConverter = mock(FilterToUrlConverter.class);
    private UrlDownloader urlDownloader = mock(UrlDownloader.class);
    private JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter = mock(JsonToCardsImageInfosConverter.class);
    private CardImageDownloader cardImageDownloader = mock(CardImageDownloader.class);
    private SearchTopic searchTopic = mock(SearchTopic.class);

    private SearchButtonPanel searchButtonPanel = new SearchButtonPanel(activeFiltersPanel, filterToUrlConverter, urlDownloader, jsonToCardsImageInfosConverter, cardImageDownloader, searchTopic);

    @Test
    public void callsCardImageDownloaderAndNotifiesAboutSearchFinished() {
        when(activeFiltersPanel.getFilters()).thenReturn(FILTERS);
        when(filterToUrlConverter.convert(FILTERS)).thenReturn(URL);
        when(urlDownloader.download(argThat(sameBeanAs(new Url(URL))))).thenReturn(JSON);
        when(jsonToCardsImageInfosConverter.convert(JSON)).thenReturn(CARD_IMAGE_INFOS);

        JButton searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        click(searchButton);

        verify(cardImageDownloader, times(1)).download(CARD_IMAGE_INFOS);
        verify(searchTopic, times(1)).notifySearchFinished(CARD_IMAGE_INFOS);
    }

    @Test
    public void doesNothingWhenThereAreNoFilters() {
        when(activeFiltersPanel.getFilters()).thenReturn(Collections.<Filter>emptyList());

        JButton searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        click(searchButton);

        verifyNoMoreInteractions(filterToUrlConverter, urlDownloader, jsonToCardsImageInfosConverter, cardImageDownloader, searchTopic);
    }

}
