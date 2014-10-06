package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Url;
import mtgdeckbuilder.topics.ProgressTopic;
import mtgdeckbuilder.topics.SearchTopic;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//TODO Jarek: doesn't stop when closing the MainFrame
public class SearchSwingWorker extends SwingWorker<Object, Integer> implements ProgressTopic.Subscriber {

    private final FilterToUrlConverter filterToUrlConverter;
    private final UrlDownloader urlDownloader;
    private final JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter;
    private final CardImageDownloader cardImageDownloader;
    private final SearchTopic searchTopic;
    private final ProgressTopic progressTopic;

    private final JButton searchButton;
    private final JLabel searchLabel;
    private final List<Filter> filters;

    private int totalParts;
    private Set<CardImageInfo> cardImageInfos;

    public SearchSwingWorker(FilterToUrlConverter filterToUrlConverter,
                             UrlDownloader urlDownloader,
                             JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter,
                             CardImageDownloader cardImageDownloader,
                             SearchTopic searchTopic,
                             ProgressTopic progressTopic,
                             JButton searchButton,
                             JLabel searchLabel,
                             List<Filter> filters) {
        this.filterToUrlConverter = filterToUrlConverter;
        this.urlDownloader = urlDownloader;
        this.jsonToCardsImageInfosConverter = jsonToCardsImageInfosConverter;
        this.cardImageDownloader = cardImageDownloader;
        this.searchTopic = searchTopic;
        this.progressTopic = progressTopic;

        progressTopic.addSubscriber(this);

        this.searchButton = searchButton;
        this.searchLabel = searchLabel;
        this.filters = filters;
    }

    @Override
    protected Object doInBackground() {
        String url = filterToUrlConverter.convert(filters);
        String json = urlDownloader.download(new Url(url));
        cardImageInfos = jsonToCardsImageInfosConverter.convert(json);
        cardImageDownloader.download(cardImageInfos);
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        searchLabel.setText("downloading - " + chunks.get(chunks.size() - 1) + "/" + totalParts);
    }

    @Override
    protected void done() {
        progressTopic.removeSubscriber(this);
        searchButton.setEnabled(true);
        try {
            get();
        } catch (Exception e) {
            searchLabel.setText(searchLabel.getText() + " - error");
            throw new RuntimeException(e);
        }
        searchLabel.setText("showing " + totalParts + " cards");

        List<String> cardNames = new ArrayList<>(cardImageInfos.size());
        for (CardImageInfo cardImageInfo : cardImageInfos) {
            cardNames.add(cardImageInfo.getName());
        }
        searchTopic.notifySearchFinished(cardNames);
    }

    @Override
    public void workStarted(int totalParts) {
        this.totalParts = totalParts;
        publish(0);
    }

    @Override
    public void workUpdate(int partDone) {
        publish(partDone);
    }

    @Override
    public void workFinished() {}

}
