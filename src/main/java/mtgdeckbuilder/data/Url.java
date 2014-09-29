package mtgdeckbuilder.data;

import java.net.MalformedURLException;
import java.net.URL;

public class Url {

    private final URL url;

    public Url(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public URL unwrap() {
        return url;
    }

}
