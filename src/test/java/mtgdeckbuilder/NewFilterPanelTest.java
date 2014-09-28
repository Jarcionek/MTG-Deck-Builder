package mtgdeckbuilder;

import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static mtgdeckbuilder.FrontEndTestingUtils.click;
import static mtgdeckbuilder.FrontEndTestingUtils.findComponent;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NewFilterPanelTest {

    private AddFilterListener addFilterListener = mock(AddFilterListener.class);

    private NewFilterPanel newFilterPanel = new NewFilterPanel(addFilterListener);

    @Test
    public void callsAddFilterListener() {
        JComboBox<?> fieldComboBox = findComponent(newFilterPanel, "fieldComboBox", JComboBox.class);
        fieldComboBox.setSelectedItem(Field.convertedmanacost);

        JComboBox<?> functionComboBox = findComponent(newFilterPanel, "functionComboBox", JComboBox.class);
        functionComboBox.setSelectedItem(Function.lt);

        JTextField argumentTextFiled = findComponent(newFilterPanel, "argumentTextField", JTextField.class);
        argumentTextFiled.setText("20");

        JButton addButton = findComponent(newFilterPanel, "addButton", JButton.class);
        click(addButton);

        verify(addFilterListener, times(1)).addFilter(argThat(sameBeanAs(new Filter(Field.convertedmanacost, Function.lt, "20"))));
    }

}
