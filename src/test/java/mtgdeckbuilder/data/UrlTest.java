package mtgdeckbuilder.data;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UrlTest {

    private static final String STRING = "http://localhost/";

    @Test
    public void unwrapsUrl() throws MalformedURLException {
        Url url = new Url(STRING);

        assertThat(url.unwrap(), is(equalTo(new URL(STRING))));
    }

}
