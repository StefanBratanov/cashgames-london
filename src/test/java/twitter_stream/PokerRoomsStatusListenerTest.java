package twitter_stream;

import common.StatusProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Status;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PokerRoomsStatusListenerTest {

    @Mock
    private StatusProcessor statusProcessor;

    private PokerRoomsStatusListener underTest;

    @Before
    public void init() {
        underTest = new PokerRoomsStatusListener(statusProcessor);
    }

    @Test
    public void processesStatus() {
        Status status = Mockito.mock(Status.class);

        underTest.onStatus(status);

        verify(statusProcessor, times(1)).process(status);
    }
}