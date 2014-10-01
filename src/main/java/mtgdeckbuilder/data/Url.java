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

    @Override
    public boolean equals(Object that) {
        return that != null && this.getClass().equals(that.getClass()) && this.url.equals(((Url) that).url);
    }

    @Override
    public int hashCode() {
        return url == null ? 0 : url.hashCode();
    }

    @Override
    public String toString() {
        return url;
    }

}
