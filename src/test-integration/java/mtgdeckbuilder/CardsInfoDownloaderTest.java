package mtgdeckbuilder;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CardsInfoDownloaderTest {

    private CardsInfoDownloader cardsInfoDownloader = new CardsInfoDownloader();

    @Test
    public void downloadsFromUrl() throws FileNotFoundException {
        String response = cardsInfoDownloader.download(new Url("http://api.mtgdb.info/cards/?fields=id,name&limit=2"));

        assertThat(response, is(equalTo("[{\"id\":1,\"name\":\"Ankh of Mishra\"},{\"id\":2,\"name\":\"Basalt Monolith\"}]")));
    }

}
