package twitter;

import database.PokerGameStore;
import extractors.PokerGameDetailsExtractor;
import model.PokerGameDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Status;
import twitter4j.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterStatusProcessorTest {

    @Mock
    private PokerGameDetailsExtractor pokerGameDetailsExtractor;

    @Mock
    private PokerGameStore pokerGameStore;

    @Mock
    private Status status;

    @Mock
    private User user;

    @InjectMocks
    private TwitterStatusProcessor underTest;


    @Test
    public void processesStatus() {
        Status status = new TestStatus();

        String testScreenName = "ThePokerRoomUK";
        String statusText = "statusText";
        LocalDateTime updatedAt = LocalDateTime.of(2007, 07, 28, 12, 00, 33, 220000000);

        List<PokerGameDetail> details = Arrays.asList(new PokerGameDetail(null,2,null),
                new PokerGameDetail(null,3,null));

        when(pokerGameDetailsExtractor.extract(testScreenName, statusText, updatedAt)).thenReturn(details);

        underTest.process(status);

        verify(pokerGameDetailsExtractor, times(1)).extract(testScreenName, statusText, updatedAt);
        verify(pokerGameStore, times(1)).persistPokerGameDetail(details.get(0));
        verify(pokerGameStore, times(1)).persistPokerGameDetail(details.get(1));

        verifyNoMoreInteractions(pokerGameDetailsExtractor, pokerGameStore);
    }

    @Test
    public void doesNotProcesssesStatusIfScreenNameIsNotAPokerVenue() {

        when(status.getUser()).thenReturn(user);
        when(user.getScreenName()).thenReturn("crap");

        verifyZeroInteractions(pokerGameDetailsExtractor, pokerGameStore);

    }
}