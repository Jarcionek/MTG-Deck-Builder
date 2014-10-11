package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.Url;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * http://api.mtgdb.info/
 */
public class UrlDownloaderTest {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private UrlDownloader urlDownloader = new UrlDownloader();

    @Test
    public void downloadsAllCardsInfoFromUrl() {
        String response = urlDownloader.download(new Url("http://api.mtgdb.info/cards/?fields=id,name&limit=2"));

        assertThat(response, is(equalTo("[{\"id\":1,\"name\":\"Ankh of Mishra\"},{\"id\":2,\"name\":\"Basalt Monolith\"}]")));
    }

    @Test
    public void downloadsSearchedCardsFromUrl() {
        String response = urlDownloader.download(new Url("http://api.mtgdb.info/search/?q=color eq blue and color eq black and color eq red and color eq green and color not white"));

        assertThat(response, is(equalTo("[{" +
                "\"id\":107094," +
                "\"relatedCardId\":0," +
                "\"setNumber\":115," +
                "\"name\":\"Glint-Eye Nephilim\"," +
                "\"searchName\":\"glinteyenephilim\"," +
                "\"description\":\"Whenever Glint-Eye Nephilim deals combat damage to a player, draw that many cards.\\n{1}, Discard a card: Glint-Eye Nephilim gets +1/+1 until end of turn.\"," +
                "\"flavor\":\" When it awoke, it shook the plane with the thunder of its craving.\"," +
                "\"colors\":[\"blue\",\"red\",\"green\",\"black\"]," +
                "\"manaCost\":\"UBRG\"," +
                "\"convertedManaCost\":4," +
                "\"cardSetName\":\"Guildpact\"," +
                "\"type\":\"Creature\"," +
                "\"subType\":\"Nephilim\"," +
                "\"power\":2," +
                "\"toughness\":2," +
                "\"loyalty\":0," +
                "\"rarity\":\"Rare\"," +
                "\"artist\":\"Mark Zug\"," +
                "\"cardSetId\":\"GPT\"," +
                "\"token\":false," +
                "\"promo\":false," +
                "\"rulings\":[]," +
                "\"formats\":" +
                    "[" +
                        "{\"name\":\"Modern\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Ravnica Block\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Legacy\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Vintage\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Freeform\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Prismatic\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Tribal Wars Legacy\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Classic\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Singleton 100\",\"legality\":\"Legal\"}," +
                        "{\"name\":\"Commander\",\"legality\":\"Legal\"}" +
                    "]," +
                "\"releasedAt\":\"2006-02-03\"}]")));
    }

    @Test
    public void downloadsSearchedCardsWithWhiteSpacesFromUrl() {
        String response = urlDownloader.download(new Url("http://api.mtgdb.info/search/?q=name m 'Child of Alara' and color eq 'black'"));

        assertThat(response, containsString("\"name\":\"Child of Alara\""));
    }

    @Test
    public void returnsAStringWithLineSeparatorWhenTargetResourceReturnsMultipleLines() throws MalformedURLException {
        String actual = urlDownloader.download(new Url(this.getClass().getResource("urlDownloaderTestFile.txt").toString()));

        assertThat(actual, is(equalTo("firstline" + NEW_LINE + "secondline" + NEW_LINE + "thirdline")));
    }

}
