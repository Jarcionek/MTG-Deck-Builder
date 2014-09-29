package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {

    private final NewFilterPanel newFilterPanel;
    private final ActiveFiltersPanel activeFiltersPanel;
    private final SearchButtonPanel searchButtonPanel;

    public MainFrame() {
        this.setSize(640, 480);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        AddFilterTopic addFilterTopic = new AddFilterTopic();

        this.newFilterPanel = new NewFilterPanel(addFilterTopic);
        this.activeFiltersPanel = new ActiveFiltersPanel(addFilterTopic);
        this.searchButtonPanel = new SearchButtonPanel(
                activeFiltersPanel,
                new FilterToUrlConverter(),
                new UrlDownloader(),
                new JsonToCardsImageInfosConverter(),
                new CardImageDownloader(),
                new SearchTopic()
        );

        this.setLayout(new BorderLayout());
        this.add(newFilterPanel, BorderLayout.NORTH);
        this.add(activeFiltersPanel, BorderLayout.CENTER);
        this.add(searchButtonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

}
