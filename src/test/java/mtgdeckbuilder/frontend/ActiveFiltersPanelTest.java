package mtgdeckbuilder.frontend;

import com.shazam.shazamcrest.MatcherAssert;
import mtgdeckbuilder.data.Field;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Function;
import mtgdeckbuilder.frontend.topics.AddFilterTopic;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JLabel;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static java.util.Arrays.asList;
import static mtgdeckbuilder.util.FrontEndTestingUtils.click;
import static mtgdeckbuilder.util.FrontEndTestingUtils.containsComponentRecursively;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ActiveFiltersPanelTest {

    private AddFilterTopic addFilterTopic = mock(AddFilterTopic.class);

    private ActiveFiltersPanel activeFiltersPanel = new ActiveFiltersPanel(addFilterTopic);

    @Test
    public void subscribesToAddFilterTopic() {
        verify(addFilterTopic).addSubscriber(activeFiltersPanel);
    }

    @Test
    public void displaysNewlyAddedFilter() {
        activeFiltersPanel.filterAdded(new Filter(Field.rarity, Function.eq, "rare"));

        JLabel filterLabel = findComponentRecursively(activeFiltersPanel, "filterLabel0", JLabel.class);
        assertThat(filterLabel.getText(), is(equalTo("rarity equal to rare")));
    }

    @Test
    public void displaysTwoNewlyAddedFilters() {
        activeFiltersPanel.filterAdded(new Filter(Field.type, Function.m, "enchantment"));
        activeFiltersPanel.filterAdded(new Filter(Field.convertedmanacost, Function.gte, "4"));

        JLabel filterLabel0 = findComponentRecursively(activeFiltersPanel, "filterLabel0", JLabel.class);
        assertThat(filterLabel0.getText(), is(equalTo("type contains enchantment")));

        JLabel filterLabel1 = findComponentRecursively(activeFiltersPanel, "filterLabel1", JLabel.class);
        assertThat(filterLabel1.getText(), is(equalTo("converted mana cost greater than or equal to 4")));
    }

    @Test
    public void canDeleteFilter() {
        Filter filter0 = new Filter(Field.subtype, Function.eq, "goblin");
        Filter filter1 = new Filter(Field.manacost, Function.gt, "0");
        Filter filter2 = new Filter(Field.description, Function.m, "end of turn");
        activeFiltersPanel.filterAdded(filter0);
        activeFiltersPanel.filterAdded(filter1);
        activeFiltersPanel.filterAdded(filter2);

        JButton deleteFilterButton1 = findComponentRecursively(activeFiltersPanel, "deleteFilterButton1", JButton.class);
        click(deleteFilterButton1);

        assertThat("Expected filterLabel0 component but not found",        containsComponentRecursively(activeFiltersPanel, "filterLabel0"),        is(equalTo(true)));
        assertThat("Expected deleteFilterButton0 component but not found", containsComponentRecursively(activeFiltersPanel, "deleteFilterButton0"), is(equalTo(true)));
        assertThat("Unexpected filterLabel1 component found",              containsComponentRecursively(activeFiltersPanel, "filterLabel1"),        is(equalTo(false)));
        assertThat("Unexpected deleteFilterButton1 component found",       containsComponentRecursively(activeFiltersPanel, "deleteFilterButton1"), is(equalTo(false)));
        assertThat("Expected filterLabel2 component but not found",        containsComponentRecursively(activeFiltersPanel, "filterLabel2"),        is(equalTo(true)));
        assertThat("Expected deleteFilterButton2 component but not found", containsComponentRecursively(activeFiltersPanel, "deleteFilterButton2"), is(equalTo(true)));

        MatcherAssert.assertThat(activeFiltersPanel.getFilters(), sameBeanAs(asList(filter0, filter2)));
    }

}
