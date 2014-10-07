package mtgdeckbuilder.frontend;

import mtgdeckbuilder.Config;
import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.ImageDownloader;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.TagFilesManager;
import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.topics.AddFilterTopic;
import mtgdeckbuilder.topics.ProgressTopic;
import mtgdeckbuilder.topics.SearchTopic;
import mtgdeckbuilder.topics.TagTopic;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

//TODO Jarek: untested!
@SuppressWarnings("FieldCanBeLocal")
public class MainFrame extends JFrame {

    private final AddFilterTopic addFilterTopic = new AddFilterTopic();
    private final ProgressTopic progressTopic = new ProgressTopic();
    private final SearchTopic searchTopic = new SearchTopic();
    private final TagTopic tagTopic = new TagTopic();

    private final NewFilterPanel newFilterPanel;
    private final ActiveFiltersPanel activeFiltersPanel;
    private final SearchButtonPanel searchButtonPanel;
    private final CardsDisplayPanel cardsDisplayPanel;
    private final TagViewer tagViewer;
    private final TagAddPanel tagAddPanel;

    public MainFrame() {
        super("MTG Deck Builder");

        // backend
        SearchSwingWorkerFactory searchSwingWorkerFactory = new SearchSwingWorkerFactory(
                new FilterToUrlConverter(),
                new UrlDownloader(),
                new JsonToCardsImageInfosConverter(),
                new CardImageDownloader(
                        Config.cardsDirectory(),
                        new ImageDownloader(),
                        progressTopic
                ),
                searchTopic
        );
        CardImageLoader cardImageLoader = new CardImageLoader(Config.cardsDirectory());
        TagsManager tagsManager = new TagsManager(new TagFilesManager(Config.tagsDirectory()));

        // frontend
        this.newFilterPanel = new NewFilterPanel(addFilterTopic);
        this.activeFiltersPanel = new ActiveFiltersPanel(addFilterTopic);
        this.searchButtonPanel = new SearchButtonPanel(activeFiltersPanel, searchSwingWorkerFactory);
        this.cardsDisplayPanel = new SearchedCardsDisplayPanel(cardImageLoader, searchTopic);
        this.tagViewer = new TagViewer(tagsManager, tagTopic);
        this.tagAddPanel = new TagAddPanel(tagsManager, tagTopic);

        createLayout();
        configureFrame();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
    }

    private void createLayout() {
        JPanel filteringPanel = new JPanel(new BorderLayout());
        filteringPanel.add(newFilterPanel, BorderLayout.NORTH);
        filteringPanel.add(activeFiltersPanel, BorderLayout.CENTER);
        filteringPanel.add(searchButtonPanel, BorderLayout.SOUTH);
        activeFiltersPanel.setPreferredSize(new Dimension(0, 125));
        activeFiltersPanel.setMinimumSize(new Dimension(0, 50));

        JPanel searchedCardsDisplayPanel = new JPanel(new BorderLayout());
        searchedCardsDisplayPanel.add(cardsDisplayPanel, BorderLayout.CENTER);
        searchedCardsDisplayPanel.add(tagAddPanel, BorderLayout.SOUTH);

        JSplitPane topAndBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        topAndBottomSplitPane.setTopComponent(filteringPanel);
        topAndBottomSplitPane.setBottomComponent(searchedCardsDisplayPanel);

        JSplitPane leftAndRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftAndRightSplitPane.setLeftComponent(tagViewer);
        leftAndRightSplitPane.setRightComponent(topAndBottomSplitPane);

        this.setContentPane(leftAndRightSplitPane);
    }

    private void configureFrame() {
        this.setSize(640, 480);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

}
