package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.frontend.swingworkers.SearchProgressHarvest;
import mtgdeckbuilder.frontend.swingworkers.SearchSwingWorkerManager;
import mtgdeckbuilder.frontend.topics.SearchTopic;
import mtgdeckbuilder.frontend.topics.TagTopic;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static mtgdeckbuilder.data.Field.color;
import static mtgdeckbuilder.data.Field.manacost;
import static mtgdeckbuilder.data.Function.eq;
import static mtgdeckbuilder.data.Function.lt;
import static mtgdeckbuilder.util.FrontEndTestingUtils.click;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class SearchButtonPanelTest {

    private static final List<Filter> FILTERS = asList(new Filter(color, eq, "blue"), new Filter(manacost, lt, "3"));
    private static final List<Filter> NO_FILTERS = emptyList();

    private ActiveFiltersPanel activeFiltersPanel = mock(ActiveFiltersPanel.class);
    private CardsDisplayPanel cardsDisplayPanel = mock(CardsDisplayPanel.class);
    private SearchSwingWorkerManager searchSwingWorkerManager = mock(SearchSwingWorkerManager.class);
    private SearchTopic searchTopic = mock(SearchTopic.class);
    private TagTopic tagTopic = mock(TagTopic.class);

    private SearchButtonPanel searchButtonPanel = new SearchButtonPanel(activeFiltersPanel, cardsDisplayPanel, searchSwingWorkerManager, searchTopic, tagTopic);

    @Test
    public void callsSearchSwingWorkerManagerWhenButtonIsPressed() {
        JButton searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        JLabel searchLabel = findComponentRecursively(searchButtonPanel, "searchLabel", JLabel.class);
        when(activeFiltersPanel.getFilters()).thenReturn(FILTERS);

        click(searchButton);

        assertEquals("unexpected searchLabel's text", "fetching data", searchLabel.getText());
        verify(searchSwingWorkerManager, times(1)).searchAndDownloadCardsInBackground(same(FILTERS), any(SearchProgressHarvest.class));
    }

    @Test
    public void doesNothingWhenThereAreNoFiltersWhenButtonIsPressed() {
        JButton searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        JLabel searchLabel = findComponentRecursively(searchButtonPanel, "searchLabel", JLabel.class);
        when(activeFiltersPanel.getFilters()).thenReturn(NO_FILTERS);

        click(searchButton);

        assertEquals("unexpected searchLabel's text", "you need at least one filter to search", searchLabel.getText());
        verifyZeroInteractions(searchSwingWorkerManager);
    }

    @Test
    public void subscribesToTagTopic() {
        verify(tagTopic, times(1)).addSubscriber(searchButtonPanel);
    }

    @Test
    public void setsTextOnLabelWhenTagTopicNotifiesAboutTagSelected() {
        JLabel searchLabel = findComponentRecursively(searchButtonPanel, "searchLabel", JLabel.class);

        searchButtonPanel.tagSelected("this-is-a-tag-name");

        assertEquals("unexpected searchLabel's text", "showing this-is-a-tag-name", searchLabel.getText());
    }

    @Test
    public void cancelsBackgroundThreadWhenTagTopicNotifiesAboutTagSelected() {
        searchButtonPanel.tagSelected("any tag");

        verify(searchSwingWorkerManager).cancel();
    }

    @Test
    public void notifiesSearchTopicAboutStartingSearchWhenPressingSearchButtonWhenThereAreSomeFilters() {
        JButton searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        when(activeFiltersPanel.getFilters()).thenReturn(FILTERS);

        click(searchButton);

        verify(searchTopic, times(1)).notifyStartingSearch();
    }

    @Test
    public void notifiesSearchTopicAboutStartingSearchWhenPressingSearchButtonWhenThereAreNoFilters() {
        JButton searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        when(activeFiltersPanel.getFilters()).thenReturn(NO_FILTERS);

        click(searchButton);

        verify(searchTopic, times(1)).notifyStartingSearch();
    }

}
