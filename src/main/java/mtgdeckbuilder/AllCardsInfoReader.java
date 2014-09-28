package mtgdeckbuilder;

import org.json.JSONArray;

import java.io.File;

public class AllCardsInfoReader {

    private final Url url;
    private final File file;
    private final CardsInfoDownloader cardsInfoDownloader;
    private final Clock clock;

    public AllCardsInfoReader(Url url, File file, CardsInfoDownloader cardsInfoDownloader, Clock clock) {
        this.url = url;
        this.file = file;
        this.cardsInfoDownloader = cardsInfoDownloader;
        this.cardsInfoDownloader.set(url, file);
        this.clock = clock;
    }

    public JSONArray read() {
        if (!file.exists()) {
            cardsInfoDownloader.download();
        } else {
            file.delete();
            cardsInfoDownloader.download();
        }
//        if (clock.currentTime() - file.lastModified() > 1000L * 24 * 60 * 60) {
//        }

        return null;
    }

}
