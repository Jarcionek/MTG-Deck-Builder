package mtgdeckbuilder.frontend.swingworkers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import mtgdeckbuilder.backend.CardImageDownloadProgressHarvest;
import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.backend.FilterToUrlConverter;
import mtgdeckbuilder.backend.JsonToCardsImageInfosConverter;
import mtgdeckbuilder.backend.UrlDownloader;
import mtgdeckbuilder.data.CardImageInfo;
import mtgdeckbuilder.data.Filter;
import mtgdeckbuilder.data.Url;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import javax.swing.SwingUtilities;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static mtgdeckbuilder.data.Field.color;
import static mtgdeckbuilder.data.Field.manacost;
import static mtgdeckbuilder.data.Function.lt;
import static mtgdeckbuilder.data.Function.m;
import static org.apache.log4j.Logger.getRootLogger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class SearchSwingWorkerManagerTest {

    private static final Appender NO_OP_APPENDER = new AppenderSkeleton() {
        @Override
        protected void append(LoggingEvent loggingEvent) {}

        @Override
        public void close() {}

        @Override
        public boolean requiresLayout() {
            return false;
        }
    };

    private static final List<Filter> FILTERS = ImmutableList.of(new Filter(color, m, "blue"), new Filter(manacost, lt, "3"));
    private static final String URL = "blah.com";
    private static final String JSON = "this is cards data in json format";
    private static final ImmutableSet<CardImageInfo> CARD_IMAGE_INFOS = ImmutableSet.of(new CardImageInfo(1, "name1"), new CardImageInfo(2, "name2"));

    @Rule public final Timeout timeout = new Timeout(1000);

    private final Object lock = new Object();

    private FilterToUrlConverter filterToUrlConverter = mock(FilterToUrlConverter.class);
    private UrlDownloader urlDownloader = mock(UrlDownloader.class);
    private JsonToCardsImageInfosConverter jsonToCardsImageInfosConverter = mock(JsonToCardsImageInfosConverter.class);
    private CardImageDownloader cardImageDownloader = mock(CardImageDownloader.class);
    private SearchProgressHarvest searchProgressHarvest = mock(SearchProgressHarvest.class);

    private SearchSwingWorkerManager searchSwingWorkerManager = new SearchSwingWorkerManager(filterToUrlConverter, urlDownloader, jsonToCardsImageInfosConverter, cardImageDownloader);

    @Before
    public void setUpMocks() {
        when(filterToUrlConverter.convert(FILTERS)).thenReturn(URL);
        when(urlDownloader.download(argThat(sameBeanAs(new Url(URL))))).thenReturn(JSON);
        when(jsonToCardsImageInfosConverter.convert(JSON)).thenReturn(CARD_IMAGE_INFOS);
    }

    @Before
    public void setNoOpLogger() {
        getRootLogger().removeAllAppenders();
        getRootLogger().addAppender(NO_OP_APPENDER);
    }

    @Test
    public void callsCardImageDownloader() {
        doNotifyLock().when(cardImageDownloader).download(anySet(), any(CardImageDownloadProgressHarvest.class));

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvest);

        waitUntilLockNotified();
        verify(cardImageDownloader, times(1)).download(eq(CARD_IMAGE_INFOS), any(CardImageDownloadProgressHarvest.class));
    }

    @Test
    public void notifiesSearchProgressHarvestWhenFinished() {
        doNotifyLock().when(searchProgressHarvest).finished();

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvest);

        waitUntilLockNotified();
    }

    @Test
    public void providesFoundCardsWhenFinished() {
        doNotifyLock().when(searchProgressHarvest).finished();

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvest);

        waitUntilLockNotified();
        assertEquals(newArrayList("name1", "name2"), searchSwingWorkerManager.getFoundCards());
    }

    @Test
    public void notifiesAboutProgressWhenCardImageDownloaderNotifiesAboutDownloadedCards() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CardImageDownloadProgressHarvest cardImageDownloadProgressHarvest = (CardImageDownloadProgressHarvest) invocation.getArguments()[1];
                cardImageDownloadProgressHarvest.downloaded(1);
                cardImageDownloadProgressHarvest.downloaded(2);
                return null;
            }
        }).when(cardImageDownloader).download(anySet(), any(CardImageDownloadProgressHarvest.class));
        doNotifyLock().when(searchProgressHarvest).finished();

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvest);

        waitUntilLockNotified();
        InOrder inOrder = inOrder(searchProgressHarvest);
        inOrder.verify(searchProgressHarvest).started(2);
        inOrder.verify(searchProgressHarvest).partDone(2);
        inOrder.verify(searchProgressHarvest).finished();
    }

    @Test
    public void notifiesAboutProgressOnEventDispatchThread() {
        SearchProgressHarvestStub searchProgressHarvestStub = new SearchProgressHarvestStub();

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvestStub);

        waitUntilLockNotified();
        searchProgressHarvestStub.verifyAllCallsWereOnEventDispatchThread();
    }

    @Test
    public void notifiesAboutError() {
        doThrow(new RuntimeException("this is expected exception")).when(cardImageDownloader).download(anySet(), any(CardImageDownloadProgressHarvest.class));
        notifyLockWhenLogging(Level.ERROR);

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvest);

        waitUntilLockNotified();
        verify(searchProgressHarvest, times(1)).error();
        verify(searchProgressHarvest, never()).finished();
    }

    @Test
    public void notifiesAboutErrorOnEventDispatchThread() {
        SearchProgressHarvestStub searchProgressHarvestStub = new SearchProgressHarvestStub();
        doThrow(new RuntimeException()).when(cardImageDownloader).download(anySet(), any(CardImageDownloadProgressHarvest.class));

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvestStub);

        waitUntilLockNotified();
        searchProgressHarvestStub.verifyAllCallsWereOnEventDispatchThread();
    }

    @Test
    public void interruptsRunningThreadWhenCancelled() {
        doNotifyLockAndGoToSleep().when(cardImageDownloader).download(anySet(), any(CardImageDownloadProgressHarvest.class));
        notifyLockWhenLogging(Level.INFO);
        SearchProgressHarvestStub searchProgressHarvestStub = spy(new SearchProgressHarvestStub());

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvestStub);

        waitUntilLockNotified(); // wait until thread starts and goes to sleep

        searchSwingWorkerManager.cancel();

        waitUntilLockNotified(); // wait until thread is cancelled and logger is informed
        verify(searchProgressHarvestStub, never()).finished();
        verify(searchProgressHarvestStub, never()).error();
        searchProgressHarvestStub.verifyAllCallsWereOnEventDispatchThread();
    }

    @Test
    public void interruptsAlreadyRunningThreadWhenStartingNewThread() {
        doNotifyLockAndGoToSleep().when(cardImageDownloader).download(anySet(), any(CardImageDownloadProgressHarvest.class));

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvest);
        waitUntilLockNotified(); // wait until thread starts and goes to sleep

        doNothing().when(cardImageDownloader).download(anySet(), any(CardImageDownloadProgressHarvest.class));
        notifyLockWhenLogging(Level.INFO);

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvest);
        waitUntilLockNotified(); // wait until info log about cancelled thread
    }

    @Test
    public void doesNotCrashWhenCancellingIfThreadNeverRun() {
        searchSwingWorkerManager.cancel();
    }

    @Test
    public void doesNotNotifySearchProgressHarvestAfterCancelling() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                searchSwingWorkerManager.cancel();
                CardImageDownloadProgressHarvest cardImageDownloadProgressHarvest = (CardImageDownloadProgressHarvest) invocation.getArguments()[1];
                cardImageDownloadProgressHarvest.downloaded(1);
                cardImageDownloadProgressHarvest.downloaded(2);
                return null;
            }
        }).when(cardImageDownloader).download(anySet(), any(CardImageDownloadProgressHarvest.class));
        notifyLockWhenLogging(Level.INFO);

        searchSwingWorkerManager.searchAndDownloadCardsInBackground(FILTERS, searchProgressHarvest);

        waitUntilLockNotified();
        verify(searchProgressHarvest, never()).partDone(anyInt());
    }


    private class SearchProgressHarvestStub implements SearchProgressHarvest {

        private boolean startedWasOnEventDispatchThread = true;
        private boolean partDoneWasOnEventDispatchThread = true;
        private boolean finishedWasOnEventDispatchThread = true;
        private boolean errorWasOnEventDispatchThread = true;

        @Override
        public void started(int numberOfParts) {
            startedWasOnEventDispatchThread = SwingUtilities.isEventDispatchThread();
        }

        @Override
        public void partDone(int partNumber) {
            partDoneWasOnEventDispatchThread = partDoneWasOnEventDispatchThread && SwingUtilities.isEventDispatchThread();
        }

        @Override
        public void finished() {
            finishedWasOnEventDispatchThread = SwingUtilities.isEventDispatchThread();
            notifyLock();
        }

        @Override
        public void error() {
            errorWasOnEventDispatchThread = SwingUtilities.isEventDispatchThread();
            notifyLock();
        }

        public void verifyAllCallsWereOnEventDispatchThread() {
            assertTrue("SearchProgressHarvest.started() should be called on event dispatch thread, but was not", startedWasOnEventDispatchThread);
            assertTrue("SearchProgressHarvest.partDone() should be called on event dispatch thread, but was not", partDoneWasOnEventDispatchThread);
            assertTrue("SearchProgressHarvest.finished() should be called on event dispatch thread, but was not", finishedWasOnEventDispatchThread);
            assertTrue("SearchProgressHarvest.error() should be called on event dispatch thread, but was not", errorWasOnEventDispatchThread);
        }

    }

    private void notifyLockWhenLogging(final Level level) {
        Logger.getRootLogger().addAppender(new AppenderSkeleton() {
            @Override
            protected void append(LoggingEvent loggingEvent) {
                if (loggingEvent.getLevel().equals(level)) {
                    notifyLock();
                }
            }

            @Override
            public void close() {}

            @Override
            public boolean requiresLayout() {
                return false;
            }
        });
    }

    private void waitUntilLockNotified() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ignored) {}
        }
    }

    private Stubber doNotifyLock() {
        return doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                notifyLock();
                return null;
            }
        });
    }

    private Stubber doNotifyLockAndGoToSleep() {
        return doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                notifyLock();
                Thread.sleep(10000);
                return null;
            }
        });
    }

    private void notifyLock() {
        synchronized (lock) {
            lock.notify();
        }
    }

}