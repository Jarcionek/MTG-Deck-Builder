package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Url;
import mtgdeckbuilder.topics.ProgressTopic;

import java.io.File;
import java.util.Set;

public class CardImageDownloader {

    //TODO Jarek: cards have different sizes!
    private static final String LOW_RES_URL = "http://api.mtgdb.info/content/card_images/";
    private static final String HIGH_RES_URL = "http://api.mtgdb.info/content/hi_res_card_images/";
    private static final String LOW_RES_EXT = ".jpeg";
    private static final String HIGH_RES_EXT = ".jpg";

    private final File cardsDirectory;
    private final ImageDownloader imageDownloader;
    private final ProgressTopic progressTopic;

    public CardImageDownloader(File cardsDirectory, ImageDownloader imageDownloader, ProgressTopic progressTopic) {
        this.cardsDirectory = cardsDirectory;
        this.imageDownloader = imageDownloader;
        this.progressTopic = progressTopic;
    }

    public void download(Set<CardImageInfo> cardImageInfos) {
        progressTopic.notifyWorkStarted(cardImageInfos.size());

        int count = 0;
        for (CardImageInfo cardImageInfo : cardImageInfos) {

            File lowResFile = new File(cardsDirectory, "low/" + cardImageInfo.getName() + ".jpg");
            if (!lowResFile.exists()) {
                imageDownloader.download(new Url(LOW_RES_URL + cardImageInfo.getId() + LOW_RES_EXT), lowResFile);
            }

            File highResFile = new File(cardsDirectory, "high/" + cardImageInfo.getName() + ".jpg");
            if (!highResFile.exists()) {
                imageDownloader.download(new Url(HIGH_RES_URL + cardImageInfo.getId() + HIGH_RES_EXT), highResFile);
            }

            progressTopic.notifyWorkUpdate(++count);
        }

        progressTopic.notifyWorkFinished();
    }

}
