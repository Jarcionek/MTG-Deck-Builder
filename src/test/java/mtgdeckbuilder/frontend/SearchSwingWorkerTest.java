package mtgdeckbuilder.frontend;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Url;
import mtgdeckbuilder.topics.ProgressTopic;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.common.collect.Lists.newArrayList;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static mtgdeckbuilder.data.Field.color;
import static mtgdeckbuilder.data.Field.manacost;
import static mtgdeckbuilder.data.Function.eq;
import static mtgdeckbuilder.data.Function.lt;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class SearchSwingWorkerTest {

    private static final List<Filter> FILTERS = ImmutableList.of(new Filter(color, eq, "blue"), new Filter(manacost, lt, "3"));
    private static final String URL = "blah.com";
    private static final String JSON = "this is cards data in json format";
    private static final ImmutableSet<CardImageInfo> CARD_IMAGE_INFOS = ImmutableSet.of(new CardImageInfo(1, "name1"), new CardImageInfo(2, "name2"));

    private FilterToUrlConverter filterToUrlConverter = mock(FilterToUrlConverter.class);
    private UrlDownloader urlDownloader = mock(UrlDownloader.class);
    private JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter = mock(JsonToCardsImageInfosConverter.class);
    private CardImageDownloader cardImageDownloader = mock(CardImageDownloader.class);

    private JButton searchButton = mock(JButton.class);
    private JLabel searchLabel = mock(JLabel.class);

    private SearchSwingWorker searchSwingWorker;

    @Before
    public void createSearchSwingWorkerUsingSearchSwingWorkerFactory() {
        when(cardImageDownloader.getProgressTopic()).thenReturn(new ProgressTopic());
        searchSwingWorker = new SearchSwingWorkerFactory(filterToUrlConverter, urlDownloader, jsonToCardsImageInfosConverter, cardImageDownloader)
                .newSearchSwingWorker(searchButton, searchLabel, FILTERS);
    }

    @Test
    public void callsCardImageDownloader() {
        when(filterToUrlConverter.convert(FILTERS)).thenReturn(URL);
        when(urlDownloader.download(argThat(sameBeanAs(new Url(URL))))).thenReturn(JSON);
        when(jsonToCardsImageInfosConverter.convert(JSON)).thenReturn(CARD_IMAGE_INFOS);

        searchSwingWorker.doInBackground();

        verify(cardImageDownloader, times(1)).download(CARD_IMAGE_INFOS);
    }

    @Test
    public void setsTextOnSearchLabelWhenProcessMethodIsCalledOnEventDispatchThread() {
        searchSwingWorker.workStarted(4);
        searchSwingWorker.process(newArrayList(0));

        verify(searchLabel).setText("0/4");

        searchSwingWorker.workUpdate(1);
        searchSwingWorker.workUpdate(2);
        searchSwingWorker.workUpdate(3);
        searchSwingWorker.process(newArrayList(1, 2, 3));

        verify(searchLabel).setText("3/4");
        verifyNoMoreInteractions(searchLabel);
    }

    @Test
    public void asasf() throws ExecutionException, InterruptedException {
        searchSwingWorker.execute();
    }

}