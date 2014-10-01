package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Url;

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

    public CardImageDownloader(File cardsDirectory, ImageDownloader imageDownloader) {
        this.cardsDirectory = cardsDirectory;
        this.imageDownloader = imageDownloader;
    }

    public void download(Set<CardImageInfo> cardImageInfos) {
        System.out.println("======================================="); //TODO Jarek: remove these system.out.printlns
        int count = 0;
        for (CardImageInfo cardImageInfo : cardImageInfos) {
            System.out.println(++count + "\t" + cardImageInfo.getName() + "\t" + cardImageInfo.getId());
        }
        System.out.println("=======================================");
        for (CardImageInfo cardImageInfo : cardImageInfos) {
            File lowResFile = new File(cardsDirectory, "low/" + cardImageInfo.getName() + ".jpg");
            if (!lowResFile.exists()) {
                System.out.println("downloading low: " + cardImageInfo.getName());
                imageDownloader.download(new Url(LOW_RES_URL + cardImageInfo.getId() + LOW_RES_EXT), lowResFile);
            }
            File highResFile = new File(cardsDirectory, "high/" + cardImageInfo.getName() + ".jpg");
            if (!highResFile.exists()) {
                System.out.println("downloading high: " + cardImageInfo.getName());
                imageDownloader.download(new Url(HIGH_RES_URL + cardImageInfo.getId() + HIGH_RES_EXT), highResFile);
            }
        }
        System.out.println("=======================================");
    }

}
