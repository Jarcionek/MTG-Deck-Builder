package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.Field;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Function;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewFilterPanel extends JPanel {

    private final JComboBox<Field> fieldComboBox;
    private final JComboBox<Function> functionComboBox;
    private final JTextField argumentTextField;
    private final JButton addButton;

    private AddFilterListener addFilterListener;

    public NewFilterPanel() {
        this.fieldComboBox = new JComboBox<Field>(Field.values());
        this.functionComboBox = new JComboBox<Function>(Function.values());
        this.argumentTextField = new JTextField();
        this.addButton = new JButton("+");

        setComponentsNames();
        configureComponents();
        createLayout();
    }

    public void setAddFilterListener(AddFilterListener addFilterListener) {
        this.addFilterListener = addFilterListener;
    }

    @TestCode
    private void setComponentsNames() {
        this.fieldComboBox.setName("fieldComboBox");
        this.functionComboBox.setName("functionComboBox");
        this.argumentTextField.setName("argumentTextField");
        this.addButton.setName("addButton");
    }

    private void configureComponents() {
        this.fieldComboBox.setMaximumRowCount(Field.values().length);
        this.functionComboBox.setMaximumRowCount(Function.values().length);
        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Field field = (Field) fieldComboBox.getSelectedItem();
                Function function = (Function) functionComboBox.getSelectedItem();
                String argument = argumentTextField.getText();
                addFilterListener.addedFilter(new Filter(field, function, argument));
            }
        });
    }

    private void createLayout() {
        this.setLayout(new BorderLayout());

        JPanel centralPanel = new JPanel(new GridLayout(1, 3));
        centralPanel.add(fieldComboBox);
        centralPanel.add(functionComboBox);
        centralPanel.add(argumentTextField);

        this.add(centralPanel, BorderLayout.CENTER);
        this.add(addButton, BorderLayout.EAST);
    }

}