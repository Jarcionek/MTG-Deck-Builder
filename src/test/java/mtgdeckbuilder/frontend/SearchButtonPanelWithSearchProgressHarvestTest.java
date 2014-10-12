package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.frontend.swingworkers.SearchProgressHarvest;
import mtgdeckbuilder.frontend.swingworkers.SearchSwingWorkerManager;
import mtgdeckbuilder.frontend.topics.TagTopic;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static mtgdeckbuilder.data.Field.power;
import static mtgdeckbuilder.data.Field.toughness;
import static mtgdeckbuilder.data.Function.gt;
import static mtgdeckbuilder.util.FrontEndTestingUtils.click;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class SearchButtonPanelWithSearchProgressHarvestTest {

    private static final List<Filter> FILTERS = asList(new Filter(power, gt, "3"), new Filter(toughness, gt, "3"));

    private final ActiveFiltersPanel activeFiltersPanel = mock(ActiveFiltersPanel.class);
    private final CardsDisplayPanel cardsDisplayPanel = mock(CardsDisplayPanel.class);
    private final SearchSwingWorkerManager searchSwingWorkerManager = mock(SearchSwingWorkerManager.class);
    private final TagTopic tagTopic = mock(TagTopic.class);

    private SearchProgressHarvest searchProgressHarvest;
    private JButton searchButton;
    private JLabel searchLabel;

    private final SearchButtonPanel searchButtonPanel = new SearchButtonPanel(activeFiltersPanel, cardsDisplayPanel, searchSwingWorkerManager, tagTopic);

    @Before
    public void setUp() {
        ArgumentCaptor<SearchProgressHarvest> argumentCaptor = ArgumentCaptor.forClass(SearchProgressHarvest.class);
        doNothing().when(searchSwingWorkerManager).searchAndDownloadCardsInBackground(anyList(), argumentCaptor.capture());
        when(activeFiltersPanel.getFilters()).thenReturn(FILTERS);

        searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        searchLabel = findComponentRecursively(searchButtonPanel, "searchLabel", JLabel.class);
        click(searchButton);

        searchProgressHarvest = argumentCaptor.getValue();
    }

    @Test
    public void updatesLabelWhenStarted() {
        searchProgressHarvest.started(5);

        assertEquals("unexpected label's text", "downloading - 0/5", searchLabel.getText());
    }

    @Test
    public void updatesLabelWhenPartDone() {
        searchProgressHarvest.started(7);
        searchProgressHarvest.partDone(3);

        assertEquals("unexpected label's text", "downloading - 3/7", searchLabel.getText());
    }

    @Test
    public void updatesLabelWhenFinished() {
        searchProgressHarvest.started(2);
        searchProgressHarvest.finished();

        assertEquals("unexpected label's text", "showing 2 cards", searchLabel.getText());
    }

    @Test
    public void enablesButtonWhenFinished() {
        searchButton.setEnabled(false);

        searchProgressHarvest.finished();

        assertTrue("searchButton should be enabled but was not", searchButton.isEnabled());
    }

    @Test
    public void loadsCardsWhenFinished() {
        ArrayList<String> foundCards = newArrayList("one", "two");
        when(searchSwingWorkerManager.getFoundCards()).thenReturn(foundCards);

        searchProgressHarvest.finished();

        verify(cardsDisplayPanel, times(1)).load(foundCards);

    }

    @Test
    public void updatesLabelWhenError() {
        searchProgressHarvest.error();

        assertEquals("unexpected label's text", "error", searchLabel.getText());
    }

    @Test
    public void enablesButtonWhenError() {
        searchButton.setEnabled(false);

        searchProgressHarvest.error();

        assertTrue("searchButton should be enabled but was not", searchButton.isEnabled());
    }

}
