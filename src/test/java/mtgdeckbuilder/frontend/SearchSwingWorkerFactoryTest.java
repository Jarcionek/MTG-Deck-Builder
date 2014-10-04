package mtgdeckbuilder.frontend;

import mtgdeckbuilder.backend.CardImageDownloader;
import mtgdeckbuilder.topics.ProgressTopic;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SearchSwingWorkerFactoryTest {

    private ProgressTopic progressTopic = mock(ProgressTopic.class);

    private SearchSwingWorkerFactory searchSwingWorkerFactory = new SearchSwingWorkerFactory(null, null, null, new CardImageDownloader(null, null, progressTopic), null);

    @Test
    public void createsSearchSwingWorkerWithProgressTopicOfCardImageDownloader() {
        SearchSwingWorker searchSwingWorker = searchSwingWorkerFactory.newSearchSwingWorker(null, null, null);

        verify(progressTopic).addSubscriber(searchSwingWorker);
    }

    @Test
    public void createsNewInstanceEachTime() {
        SearchSwingWorker searchSwingWorker1 = searchSwingWorkerFactory.newSearchSwingWorker(null, null, null);
        SearchSwingWorker searchSwingWorker2 = searchSwingWorkerFactory.newSearchSwingWorker(null, null, null);

        assertNotSame("factory returned the same instance,", searchSwingWorker1, searchSwingWorker2);
    }

}