package mtgdeckbuilder.frontend;

import mtgdeckbuilder.data.Field;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Function;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static mtgdeckbuilder.util.FrontEndTestingUtils.click;
import static mtgdeckbuilder.util.FrontEndTestingUtils.findComponentRecursively;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NewFilterPanelTest {

    private AddFilterListener addFilterListener = mock(AddFilterListener.class);

    private NewFilterPanel newFilterPanel = new NewFilterPanel();

    @Before
    public void setUp() {
        newFilterPanel.setAddFilterListener(addFilterListener);
    }

    @Test
    public void callsAddFilterListener() {
        JComboBox<?> fieldComboBox = findComponentRecursively(newFilterPanel, "fieldComboBox", JComboBox.class);
        fieldComboBox.setSelectedItem(Field.convertedmanacost);

        JComboBox<?> functionComboBox = findComponentRecursively(newFilterPanel, "functionComboBox", JComboBox.class);
        functionComboBox.setSelectedItem(Function.lt);

        JTextField argumentTextFiled = findComponentRecursively(newFilterPanel, "argumentTextField", JTextField.class);
        argumentTextFiled.setText("20");

        JButton addButton = findComponentRecursively(newFilterPanel, "addButton", JButton.class);
        click(addButton);

        verify(addFilterListener, times(1)).addedFilter(argThat(sameBeanAs(new Filter(Field.convertedmanacost, Function.lt, "20"))));
    }

}
