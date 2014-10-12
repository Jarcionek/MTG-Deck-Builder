package mtgdeckbuilder.frontend;

import mtgdeckbuilder.Config;
import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.ImageDownloader;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.TagFilesManager;
import mtgdeckbuilder.backend.TagsManager;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.frontend.swingworkers.SearchSwingWorkerManager;
import mtgdeckbuilder.frontend.topics.AddFilterTopic;
import mtgdeckbuilder.frontend.topics.SearchTopic;
import mtgdeckbuilder.frontend.topics.TagTopic;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("FieldCanBeLocal")
public class MainFrame extends JFrame {

    private final AddFilterTopic addFilterTopic = new AddFilterTopic();
    private final TagTopic tagTopic = new TagTopic();
    private final SearchTopic searchTopic = new SearchTopic();

    private final NewFilterPanel newFilterPanel;
    private final ActiveFiltersPanel activeFiltersPanel;
    private final SearchButtonPanel searchButtonPanel;
    private final CardsDisplayPanel cardsDisplayPanel;
    private final TagViewer tagViewer;
    private final TagAddPanel tagAddPanel;
    private final CardTaggingPanel cardTaggingPanel;

    public MainFrame() {
        super("MTG Deck Builder");

        // backend
        final SearchSwingWorkerManager searchSwingWorkerManager = new SearchSwingWorkerManager(
                new FilterToUrlConverter(),
                new UrlDownloader(),
                new JsonToCardsImageInfosConverter(),
                new CardImageDownloader(
                        Config.cardsDirectory(),
                        new ImageDownloader()
                )
        );
        CardImageLoader cardImageLoader = new CardImageLoader(Config.cardsDirectory());
        TagsManager tagsManager = new TagsManager(new TagFilesManager(Config.tagsDirectory()));

        // frontend
        this.newFilterPanel = new NewFilterPanel(addFilterTopic);
        this.activeFiltersPanel = new ActiveFiltersPanel(addFilterTopic);
        this.cardsDisplayPanel = new CardsDisplayPanel(cardImageLoader);
        this.searchButtonPanel = new SearchButtonPanel(activeFiltersPanel, cardsDisplayPanel, searchSwingWorkerManager, searchTopic, tagTopic);
        this.tagViewer = new TagViewer(tagsManager, cardsDisplayPanel, searchTopic, tagTopic);
        this.tagAddPanel = new TagAddPanel(tagsManager, tagTopic);
        this.cardTaggingPanel = new CardTaggingPanel(cardsDisplayPanel, tagsManager, tagTopic);

        createLayout();
        configureFrame();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                searchSwingWorkerManager.cancel();
            }
        });
    }

    private void createLayout() {
        activeFiltersPanel.setPreferredSize(new Dimension(0, 125));
        activeFiltersPanel.setMinimumSize(new Dimension(0, 50));

        JPanel filteringPanel = new JPanel(new BorderLayout());
        filteringPanel.add(newFilterPanel, BorderLayout.NORTH);
        filteringPanel.add(activeFiltersPanel, BorderLayout.CENTER);
        filteringPanel.add(searchButtonPanel, BorderLayout.SOUTH);

        JPanel searchedCardsDisplayContainer = new JPanel(new BorderLayout());
        searchedCardsDisplayContainer.add(cardsDisplayPanel, BorderLayout.CENTER);
        searchedCardsDisplayContainer.add(cardTaggingPanel, BorderLayout.SOUTH);

        JSplitPane topAndBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        topAndBottomSplitPane.setTopComponent(filteringPanel);
        topAndBottomSplitPane.setBottomComponent(searchedCardsDisplayContainer);

        JPanel leftPartOfSplitPane = new JPanel(new BorderLayout());
        leftPartOfSplitPane.add(tagViewer, BorderLayout.CENTER);
        leftPartOfSplitPane.add(tagAddPanel, BorderLayout.NORTH);

        JSplitPane leftAndRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftAndRightSplitPane.setLeftComponent(leftPartOfSplitPane);
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
