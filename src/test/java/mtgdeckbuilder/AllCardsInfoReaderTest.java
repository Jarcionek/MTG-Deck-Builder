package mtgdeckbuilder;

import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllCardsInfoReaderTest {

    private final long DAY_DURATION_IN_MILLIS = 1000L * 24 * 60 * 60;

    private final Url url = mock(Url.class);
    private final File file = mock(File.class);
    private final CardsInfoDownloader cardsInfoDownloader = mock(CardsInfoDownloader.class);
    private final Clock clock = mock(Clock.class);

    private AllCardsInfoReader allCardsInfoReader = new AllCardsInfoReader(url, file, cardsInfoDownloader, clock);

    @Test
    public void setsUrlAndFileOnCardsInfoDownloader() {
        verify(cardsInfoDownloader, times(1)).set(url, file);
    }

    @Test
    public void downloadsIfFileDoesNotExist() {
        when(file.exists()).thenReturn(false);

        allCardsInfoReader.read();

        verify(cardsInfoDownloader, times(1)).download();
    }

    @Test
    public void redownloadsFileIfItIsMoreThanOneDayOld() {
        when(file.exists()).thenReturn(true);
        when(file.lastModified()).thenReturn(25L);
        when(clock.currentTime()).thenReturn(DAY_DURATION_IN_MILLIS + 100L);

        allCardsInfoReader.read();

        verify(file, times(1)).delete();
        verify(cardsInfoDownloader, times(1)).download();
    }

}
