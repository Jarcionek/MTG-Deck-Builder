package mtgdeckbuilder.frontend;

import mtgdeckbuilder.TestCode;
import mtgdeckbuilder.data.Field;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Function;
import mtgdeckbuilder.frontend.topics.AddFilterTopic;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//TODO Jarek: card name: Ajani's Pridemate - apostrophe is illegal character
//TODO Jarek: card name: Æther Adept - Æ is illegal character (use AE instead)
//TODO Jarek: card name: Error (Trial/Error) - cannot be used as file name due to slash
public class NewFilterPanel extends JPanel {

    private final AddFilterTopic addFilterTopic;

    private final JComboBox<Field> fieldComboBox;
    private final JComboBox<Function> functionComboBox;
    private final JTextField argumentTextField;
    private final JButton addButton;

    public NewFilterPanel(AddFilterTopic addFilterTopic) {
        this.addFilterTopic = addFilterTopic;

        this.fieldComboBox = new JComboBox<>(Field.values());
        this.functionComboBox = new JComboBox<>(Function.values());
        this.argumentTextField = new JTextField();
        this.addButton = new JButton("+");

        setComponentsNames();
        configureComponents();
        createLayout();
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

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Field field = (Field) fieldComboBox.getSelectedItem();
                Function function = (Function) functionComboBox.getSelectedItem();
                String argument = argumentTextField.getText();
                addFilterTopic.post(new Filter(field, function, argument));
            }
        };
        this.argumentTextField.addActionListener(actionListener);
        this.addButton.addActionListener(actionListener);
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
