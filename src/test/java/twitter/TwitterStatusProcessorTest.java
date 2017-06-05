package twitter;

import database.PokerDao;
import extractors.PokerGameDetailsExtractor;
import model.PokerGameDetail;
import model.PokerVenue;
import model.PokerVenueDetail;
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
    private PokerDao pokerDao;

    @Mock
    private Status status;

    @Mock
    private User user;

    @InjectMocks
    private TwitterStatusProcessor underTest;

    @Test
    public void processesStatus() {
        Status status = new TestStatus();

        String statusText = "statusText";
        String twitterUrl = "https://twitter.com/ThePokerRoomUK/status/123";

        LocalDateTime updatedAt = LocalDateTime.of(2007, 7, 28, 13, 0, 33, 220000000);

        List<PokerGameDetail> details = Arrays.asList(new PokerGameDetail(null, 2, null, twitterUrl),
                new PokerGameDetail(null, 3, null, twitterUrl));

        PokerVenueDetail pokerVenueDetail = new PokerVenueDetail(PokerVenue.Vic,"http://stuff.com/pic1.jpg");

        when(pokerGameDetailsExtractor.extract(PokerVenue.Vic, statusText, updatedAt,twitterUrl)).thenReturn(details);

        underTest.process(status);

        verify(pokerGameDetailsExtractor, times(1)).extract(PokerVenue.Vic, statusText, updatedAt, twitterUrl);
        verify(pokerDao,times(1)).persistPokerVenueDetail(pokerVenueDetail);
        verify(pokerDao, times(1)).persistPokerGameDetail(details.get(0));
        verify(pokerDao, times(1)).persistPokerGameDetail(details.get(1));

        verifyNoMoreInteractions(pokerGameDetailsExtractor, pokerDao);
    }

    @Test
    public void doesNotProcesssesStatusIfScreenNameIsNotAPokerVenue() {

        when(status.getUser()).thenReturn(user);
        when(user.getScreenName()).thenReturn("crap");

        verifyZeroInteractions(pokerGameDetailsExtractor, pokerDao);

    }
}