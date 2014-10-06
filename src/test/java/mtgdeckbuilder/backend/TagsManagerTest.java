package mtgdeckbuilder.backend;

import com.shazam.shazamcrest.matcher.CustomisableMatcher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class TagsManagerTest {

    private static final String TAG_ONE = "tagNameOne";
    private static final String TAG_TWO = "tagNameTwo";
    private static final String CARD_ONE = "cardNameOne";
    private static final String CARD_TWO = "cardNameTwo";

    private TagFilesManager tagFilesManager = mock(TagFilesManager.class);

    private TagsManager tagsManager = new TagsManager(tagFilesManager);

    @Test
    public void addsNameToTagFile() {
        when(tagFilesManager.load(TAG_ONE)).thenReturn(new ArrayList<String>());

        tagsManager.tag(TAG_ONE, CARD_ONE);

        verify(tagFilesManager).save(eq(TAG_ONE), argThat(sameIterableAs(CARD_ONE)));
    }

    @Test
    public void doesNotAddNameWhichIsAlreadyInTagFile() {
        when(tagFilesManager.load(TAG_ONE)).thenReturn(newArrayList(CARD_TWO));

        tagsManager.tag(TAG_ONE, CARD_TWO);

        verify(tagFilesManager, never()).save(any(String.class), any(Iterable.class));
    }

    @Test
    public void removesExistingNameFromTagFile() {
        when(tagFilesManager.load(TAG_TWO)).thenReturn(newArrayList(CARD_ONE, CARD_TWO));

        tagsManager.untag(TAG_TWO, CARD_ONE);

        verify(tagFilesManager).save(eq(TAG_TWO), argThat(sameIterableAs(CARD_TWO)));
    }
    
    @Test
    public void retrievesCardsFromTagFile() {
        List<String> expectedCards = newArrayList(CARD_ONE, CARD_TWO);
        when(tagFilesManager.load(TAG_TWO)).thenReturn(expectedCards);

        List<String> actualCards = tagsManager.getCards(TAG_TWO);

        assertEquals(expectedCards, actualCards);
    }

    @Test
    public void retrievesAllAvailableTags() {
        List<String> expectedTags = newArrayList(TAG_ONE, TAG_TWO);
        when(tagFilesManager.loadAvailableTags()).thenReturn(expectedTags);

        List<String> actualTags = tagsManager.getAvailableTags();

        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void delegatesEmptyListWhenCreatingNewTag() {
        tagsManager.createEmptyTag(TAG_TWO);

        verify(tagFilesManager, times(1)).save(TAG_TWO, Collections.<String>emptyList());
    }


    private static CustomisableMatcher<List<String>> sameIterableAs(String... cards) {
        return sameBeanAs(Arrays.asList(cards));
    }

}