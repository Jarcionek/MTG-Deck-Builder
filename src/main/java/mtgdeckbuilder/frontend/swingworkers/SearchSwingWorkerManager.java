package mtgdeckbuilder.frontend.swingworkers;

import com.google.common.collect.ImmutableList;
import mtgdeckbuilder.backend.CardImageDownloadProgressHarvest;
import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;

public class SearchSwingWorkerManager {

    private final Logger log = LoggerFactory.getLogger(SearchSwingWorkerManager.class);

    private final FilterToUrlConverter filterToUrlConverter;
    private final UrlDownloader urlDownloader;
    private final JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter;
    private final CardImageDownloader cardImageDownloader;

    private SearchSwingWorker swingWorker;

    public SearchSwingWorkerManager(FilterToUrlConverter filterToUrlConverter,
                                    UrlDownloader urlDownloader,
                                    JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter,
                                    CardImageDownloader cardImageDownloader) {
        this.filterToUrlConverter = filterToUrlConverter;
        this.urlDownloader = urlDownloader;
        this.jsonToCardsImageInfosConverter = jsonToCardsImageInfosConverter;
        this.cardImageDownloader = cardImageDownloader;
    }

    public void searchAndDownloadCardsInBackground(List<Filter> filters, SearchProgressHarvest progressHarvest) {
        cancel();
        swingWorker = new SearchSwingWorker(filters, progressHarvest);
        swingWorker.execute();
    }

    public void cancel() {
        if (swingWorker != null) {
            swingWorker.cancel(true);
        }
    }

    public Iterable<String> getFoundCards() {
        return swingWorker.foundCards;
    }

    private class SearchSwingWorker extends SwingWorker<List<String>, ProgressUpdate> {

        private final List<Filter> filters;
        private final SearchProgressHarvest searchProgressHarvest;
        private final CardImageDownloadProgressHarvest cardImageDownloadProgressHarvest;

        private List<String> foundCards;

        private SearchSwingWorker(List<Filter> filters, SearchProgressHarvest searchProgressHarvest) {
            this.filters = filters;
            this.searchProgressHarvest = searchProgressHarvest;
            this.cardImageDownloadProgressHarvest = new CardImageDownloadProgressHarvest() {
                @Override
                public void downloaded(int partNumber) {
                    publish(new PartDoneUpdate(partNumber));
                }
            };
        }

        @Override
        protected List<String> doInBackground() {
            String url = filterToUrlConverter.convert(filters);
            String json = urlDownloader.download(new Url(url));
            Set<CardImageInfo> cardImageInfos = jsonToCardsImageInfosConverter.convert(json);
            publish(new StartedUpdate(cardImageInfos.size()));

            cardImageDownloader.download(cardImageInfos, cardImageDownloadProgressHarvest);

            ImmutableList.Builder<String> builder = ImmutableList.builder();
            for (CardImageInfo cardImageInfo : cardImageInfos) {
                builder.add(cardImageInfo.getName());
            }
            return builder.build();
        }

        @Override
        protected void process(List<ProgressUpdate> chunks) {
            if (isCancelled()) {
                return;
            }
            if (chunks.get(0) instanceof StartedUpdate) {
                searchProgressHarvest.started(chunks.get(0).getNumber());
            }
            if (chunks.get(chunks.size() - 1) instanceof PartDoneUpdate) {
                searchProgressHarvest.partDone(chunks.get(chunks.size() - 1).getNumber());
            }
        }

        @Override
        protected void done() {
            try {
                foundCards = get();
                searchProgressHarvest.finished();
            } catch (CancellationException e) {
                log.info("background thread cancelled");
            } catch (Exception e) {
                log.error("error when downloading cards in background thread", e);
                searchProgressHarvest.error();
            }
        }

    }

}
