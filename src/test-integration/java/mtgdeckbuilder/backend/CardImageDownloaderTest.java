package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Url;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.File;
import java.io.IOException;
import java.util.AbstractSet;
import java.util.Iterator;

import static com.google.common.collect.Sets.newHashSet;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CardImageDownloaderTest {

    private static final String IMAGE_NAME_ONE = "one";
    private static final String IMAGE_NAME_TWO = "two";
    private static final String IMAGE_NAME_THREE = "three";
    private static final File CARDS_DIRECTORY = new File(CardImageDownloaderTest.class + "-dir");
    private static final File LOW_RES_IMAGES_DIRECTORY = new File(CARDS_DIRECTORY, "low");
    private static final File HIGH_RES_IMAGES_DIRECTORY = new File(CARDS_DIRECTORY, "high");
    private static final File LOW_RES_FILE_ONE = new File(LOW_RES_IMAGES_DIRECTORY, IMAGE_NAME_ONE + ".jpg");
    private static final File LOW_RES_FILE_TWO = new File(LOW_RES_IMAGES_DIRECTORY, IMAGE_NAME_TWO + ".jpg");
    private static final File LOW_RES_FILE_THREE = new File(LOW_RES_IMAGES_DIRECTORY, IMAGE_NAME_THREE + ".jpg");
    private static final File HIGH_RES_FILE_ONE = new File(HIGH_RES_IMAGES_DIRECTORY, IMAGE_NAME_ONE + ".jpg");
    private static final File HIGH_RES_FILE_TWO = new File(HIGH_RES_IMAGES_DIRECTORY, IMAGE_NAME_TWO + ".jpg");
    private static final File HIGH_RES_FILE_THREE = new File(HIGH_RES_IMAGES_DIRECTORY, IMAGE_NAME_THREE + ".jpg");

    private ImageDownloader imageDownloader = mock(ImageDownloader.class);
    private CardImageDownloadProgressHarvest progressHarvest = mock(CardImageDownloadProgressHarvest.class);

    private CardImageDownloader cardImageDownloader = new CardImageDownloader(CARDS_DIRECTORY, imageDownloader);

    @Before
    @After
    public void deleteTestFiles() {
        LOW_RES_FILE_ONE.delete();
        LOW_RES_FILE_TWO.delete();
        LOW_RES_FILE_THREE.delete();
        HIGH_RES_FILE_ONE.delete();
        HIGH_RES_FILE_TWO.delete();
        HIGH_RES_FILE_THREE.delete();
        LOW_RES_IMAGES_DIRECTORY.delete();
        HIGH_RES_IMAGES_DIRECTORY.delete();
        CARDS_DIRECTORY.delete();
    }
    
    @Test
    public void downloadsMissingImages() {
        cardImageDownloader.download(newHashSet(new CardImageInfo(35, "one")), progressHarvest);

        verify(imageDownloader, times(1)).download(new Url("http://api.mtgdb.info/content/card_images/35.jpeg"), LOW_RES_FILE_ONE);
        verify(imageDownloader, times(1)).download(new Url("http://api.mtgdb.info/content/hi_res_card_images/35.jpg"), HIGH_RES_FILE_ONE);
    }

    @Test
    public void doesNotDownloadHighResCardsIfTheyAlreadyExist() throws IOException {
        HIGH_RES_IMAGES_DIRECTORY.mkdirs();
        HIGH_RES_FILE_ONE.createNewFile();
        HIGH_RES_FILE_TWO.createNewFile();

        cardImageDownloader.download(newHashSet(new CardImageInfo(12, "one"), new CardImageInfo(17, "two")), progressHarvest);

        verify(imageDownloader, times(1)).download(new Url("http://api.mtgdb.info/content/card_images/12.jpeg"), LOW_RES_FILE_ONE);
        verify(imageDownloader, times(1)).download(new Url("http://api.mtgdb.info/content/card_images/17.jpeg"), LOW_RES_FILE_TWO);
        verifyNoMoreInteractions(imageDownloader);
    }

    @Test
    public void doesNotDownloadLowResCardsIfTheyAlreadyExist() throws IOException {
        LOW_RES_IMAGES_DIRECTORY.mkdirs();
        LOW_RES_FILE_ONE.createNewFile();
        LOW_RES_FILE_THREE.createNewFile();

        cardImageDownloader.download(newHashSet(new CardImageInfo(1, "three"), new CardImageInfo(77777, "one")), progressHarvest);

        verify(imageDownloader, times(1)).download(new Url("http://api.mtgdb.info/content/hi_res_card_images/1.jpg"), HIGH_RES_FILE_THREE);
        verify(imageDownloader, times(1)).download(new Url("http://api.mtgdb.info/content/hi_res_card_images/77777.jpg"), HIGH_RES_FILE_ONE);
        verifyNoMoreInteractions(imageDownloader);
    }

    @Test
    public void notifiesAboutProgress() {
        cardImageDownloader.download(newHashSet(new CardImageInfo(1, "1"), new CardImageInfo(2, "2"), new CardImageInfo(3, "3")), progressHarvest);

        InOrder inOrder = inOrder(progressHarvest);
        inOrder.verify(progressHarvest).downloaded(1);
        inOrder.verify(progressHarvest).downloaded(2);
        inOrder.verify(progressHarvest).downloaded(3);
        inOrder.verifyNoMoreInteractions();
    }

    @Test(timeout = 100)
    public void stopsExecutionWhenThreadIsInterrupted() throws InterruptedException {
        // given
        Thread thread = new Thread() {
            @Override
            public void run() {
                cardImageDownloader.download(new InfiniteSet(), progressHarvest);
            }
        };

        // when
        thread.start();
        thread.interrupt();

        // then
        thread.join(); // should not timeout
    }


    private static class InfiniteSet extends AbstractSet<CardImageInfo> {

        private final CardImageInfo element = new CardImageInfo(0, "element");

        @Override
        public Iterator<CardImageInfo> iterator() {
            return new Iterator<CardImageInfo>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public CardImageInfo next() {
                    return element;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public int size() {
            return Integer.MAX_VALUE;
        }

    }

}
