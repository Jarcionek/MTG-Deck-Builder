package mtgdeckbuilder.frontend;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {

    private final NewFilterPanel newFilterPanel;
    private final ActiveFiltersPanel activeFiltersPanel;

    public MainFrame() {
        this.setSize(640, 480);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        AddFilterTopic addFilterTopic = new AddFilterTopic();

        this.newFilterPanel = new NewFilterPanel(addFilterTopic);
        this.activeFiltersPanel = new ActiveFiltersPanel(addFilterTopic);

        this.setLayout(new BorderLayout());
        this.add(newFilterPanel, BorderLayout.NORTH);
        this.add(activeFiltersPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

}
