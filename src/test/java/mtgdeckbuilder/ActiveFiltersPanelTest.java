package mtgdeckbuilder;

import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JLabel;

import static mtgdeckbuilder.util.FrontEndTestingUtils.click;
import static mtgdeckbuilder.util.FrontEndTestingUtils.containsComponentRecursively;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ActiveFiltersPanelTest {

    private ActiveFiltersPanel activeFiltersPanel = new ActiveFiltersPanel();

    @Test
    public void displaysNewlyAddedFilter() {
        activeFiltersPanel.addFilter(new Filter(Field.rarity, Function.eq, "rare"));

        JLabel filterLabel = findComponentRecursively(activeFiltersPanel, "filterLabel0", JLabel.class);
        assertThat(filterLabel.getText(), is(equalTo("rarity equal to rare")));
    }

    @Test
    public void displaysTwoNewlyAddedFilters() {
        activeFiltersPanel.addFilter(new Filter(Field.type, Function.m, "enchantment"));
        activeFiltersPanel.addFilter(new Filter(Field.convertedmanacost, Function.gte, "4"));

        JLabel filterLabel0 = findComponentRecursively(activeFiltersPanel, "filterLabel0", JLabel.class);
        assertThat(filterLabel0.getText(), is(equalTo("type contains enchantment")));

        JLabel filterLabel1 = findComponentRecursively(activeFiltersPanel, "filterLabel1", JLabel.class);
        assertThat(filterLabel1.getText(), is(equalTo("converted mana cost greater than or equal to 4")));
    }

    @Test
    public void canDeleteFilter() {
        activeFiltersPanel.addFilter(new Filter(Field.subtype, Function.eq, "goblin"));
        activeFiltersPanel.addFilter(new Filter(Field.manacost, Function.gt, "0"));
        activeFiltersPanel.addFilter(new Filter(Field.description, Function.m, "end of turn"));

        JButton deleteFilterButton1 = findComponentRecursively(activeFiltersPanel, "deleteFilterButton1", JButton.class);
        click(deleteFilterButton1);

        assertThat("Expected filterLabel0 component but not found",        containsComponentRecursively(activeFiltersPanel, "filterLabel0"),        is(equalTo(true)));
        assertThat("Expected deleteFilterButton0 component but not found", containsComponentRecursively(activeFiltersPanel, "deleteFilterButton0"), is(equalTo(true)));
        assertThat("Unexpected filterLabel1 component found",              containsComponentRecursively(activeFiltersPanel, "filterLabel1"),        is(equalTo(false)));
        assertThat("Unexpected deleteFilterButton1 component found",       containsComponentRecursively(activeFiltersPanel, "deleteFilterButton1"), is(equalTo(false)));
        assertThat("Expected filterLabel2 component but not found",        containsComponentRecursively(activeFiltersPanel, "filterLabel2"),        is(equalTo(true)));
        assertThat("Expected deleteFilterButton2 component but not found", containsComponentRecursively(activeFiltersPanel, "deleteFilterButton2"), is(equalTo(true)));

    }

}
