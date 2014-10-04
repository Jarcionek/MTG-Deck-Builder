package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.CardImageInfo;
import org.junit.Test;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;

public class JsonToCardsImageInfosConverterTest {

    private JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter = new JsonToCardsImageInfosConverter();

    @Test
    public void convertsJsonWithSingleCard() {
        Set<CardImageInfo> cardImageInfos = jsonToCardsImageInfosConverter.convert(
                "[{" +
                        "\"id\":4253," +
                        "\"relatedCardId\":0," +
                        "\"setNumber\":32," +
                        "\"name\":\"Cloud Pirates\"," +
                        "\"searchName\":\"cloudpirates\"," +
                        "\"description\":\"Flying\\nCloud Pirates can block only creatures with flying.\"," +
                        "\"flavor\":\"\"," +
                        "\"colors\":[\"blue\"]," +
                        "\"manaCost\":\"U\"," +
                        "\"convertedManaCost\":1," +
                        "\"cardSetName\":\"Portal\"," +
                        "\"type\":\"Creature\"," +
                        "\"subType\":\"Human Pirate\"," +
                        "\"power\":1," +
                        "\"toughness\":1," +
                        "\"loyalty\":0," +
                        "\"rarity\":\"Common\"," +
                        "\"artist\":\"Phil Foglio\"," +
                        "\"cardSetId\":\"POR\"," +
                        "\"token\":false," +
                        "\"promo\":false," +
                        "\"rulings\":[]," +
                        "\"formats\":[{\"name\":\"Legacy\",\"legality\":\"Legal\"},{\"name\":\"Vintage\",\"legality\":\"Legal\"},{\"name\":\"Commander\",\"legality\":\"Legal\"}]," +
                        "\"releasedAt\":\"1997-06-01\"" +
                "}]"
        );

        assertThat(cardImageInfos, is(sameBeanAs(set(new CardImageInfo(4253, "Cloud Pirates")))));
    }

    @Test
    public void convertsJsonWithMultipleCards() {
        Set<CardImageInfo> cardImageInfos = jsonToCardsImageInfosConverter.convert(
                "[" +
                        "{" +
                                "\"id\":5," +
                                "\"name\":\"Name One\"," +
                                "\"releasedAt\":\"2007-03-02\"" +
                        "}," +
                        "{" +
                                "\"id\":3456," +
                                "\"name\":\"Name Three\"," +
                                "\"releasedAt\":\"2005-12-13\"" +
                        "}," +
                        "{" +
                                "\"id\":17945," +
                                "\"name\":\"Name Two\"," +
                                "\"releasedAt\":\"1994-08-25\"" +
                        "}" +
                "]"
        );

        assertThat(cardImageInfos, is(sameBeanAs(set(new CardImageInfo(5, "Name One"), new CardImageInfo(3456, "Name Three"), new CardImageInfo(17945, "Name Two")))));
    }

    @Test
    public void choosesMostRecentIdWhenMultipleCardsHaveSameName() {
        Set<CardImageInfo> cardImageInfos = jsonToCardsImageInfosConverter.convert(
                "[" +
                        "{" +
                            "\"id\":295," +
                            "\"name\":\"Plains\"," +
                            "\"releasedAt\":\"1993-08-05\"" +
                        "}," +
                        "{" +
                            "\"id\":1397," +
                            "\"name\":\"Plains\"," +
                            "\"releasedAt\":\"2012-04-01\"" +
                        "}," +
                        "{" +
                            "\"id\":597," +
                            "\"name\":\"Plains\"," +
                            "\"releasedAt\":\"1993-10-01\"" +
                        "}," +
                        "{" +
                            "\"id\":2386," +
                            "\"name\":\"Plains\"," +
                            "\"releasedAt\":\"2012-04-02\"" +
                        "}," +
                        "{" +
                            "\"id\":899," +
                            "\"name\":\"Plains\"," +
                            "\"releasedAt\":\"1993-12-01\"" +
                        "}" +
                "]"
        );

        assertThat(cardImageInfos, is(sameBeanAs(set(new CardImageInfo(2386, "Plains")))));
    }

    @Test
    public void ignoresEntriesWithNoReleasedAtWhenThereAreTwoCardsWithTheSameName() {
        Set<CardImageInfo> cardImageInfos = jsonToCardsImageInfosConverter.convert(
                "[" +
                        "{" +
                                "\"id\":295," +
                                "\"name\":\"Plains\"," +
                                "\"releasedAt\":\"1993-08-05\"" +
                        "}," +
                        "{" +
                                "\"id\":1397," +
                                "\"name\":\"Plains\"," +
                                "\"releasedAt\":null" +
                        "}" +
                "]"
        );

        assertThat(cardImageInfos, is(sameBeanAs(set(new CardImageInfo(295, "Plains")))));
    }


    private static Set<CardImageInfo> set(CardImageInfo... cardImageInfos) {
        return newHashSet(cardImageInfos);
    }

}
