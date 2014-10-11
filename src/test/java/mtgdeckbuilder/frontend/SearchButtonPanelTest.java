package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.Filter;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class SearchButtonPanelTest {

    private static final List<Filter> FILTERS = asList(new Filter(color, eq, "blue"), new Filter(manacost, lt, "3"));
    private static final List<Filter> NO_FILTERS = emptyList();

    private ActiveFiltersPanel activeFiltersPanel = mock(ActiveFiltersPanel.class);
    private SearchSwingWorkerFactory searchSwingWorkerFactory = mock(SearchSwingWorkerFactory.class);

    private SearchButtonPanel searchButtonPanel = new SearchButtonPanel(activeFiltersPanel, searchSwingWorkerFactory);

    @Test
    public void getsSearchSwingWorkerFromTheFactoryAndExecutes() {
        when(activeFiltersPanel.getFilters()).thenReturn(FILTERS);
        
        JButton searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        JLabel searchLabel = findComponentRecursively(searchButtonPanel, "searchLabel", JLabel.class);

        try {
            click(searchButton);
        } catch (NullPointerException exception) {
            assertFalse("searchButton should be disabled but was not", searchButton.isEnabled());
            assertEquals("unexpected searchLabel's text", "fetching data", searchLabel.getText());
            verify(searchSwingWorkerFactory).newSearchSwingWorker(searchButton, searchLabel, FILTERS);
            return; // should call final method execute() on SearchSwingWorker (factory returns null)
        }
        fail("no interactions with SearchSwingWorker, but execute() call expected");
    }

    @Test
    public void doesNothingWhenThereAreNoFilters() {
        when(activeFiltersPanel.getFilters()).thenReturn(NO_FILTERS);

        JButton searchButton = findComponentRecursively(searchButtonPanel, "searchButton", JButton.class);
        JLabel searchLabel = findComponentRecursively(searchButtonPanel, "searchLabel", JLabel.class);

        click(searchButton);

        assertEquals("unexpected searchLabel's text", "you need at least one filter to search", searchLabel.getText());
        verifyZeroInteractions(searchSwingWorkerFactory);
    }

}
