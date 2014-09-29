package mtgdeckbuilder.data;

import java.net.MalformedURLException;
import java.net.URL;

public class Url {

    private final String url;

    public Url(String url) {
        this.url = url;
    }

    public URL unwrap() {
        try {
            return new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }

}
