package api;

import database.PokerGameStore;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerVenue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PokerGamesApiTest {

    @Mock
    private PokerGameStore pokerGameStore;

    @InjectMocks
    private PokerGamesApi underTest;

    @Test
    public void getsPokerGameDetails() {
        String twitterUrl = "www.lol.com";
        LocalDateTime time1 = LocalDateTime.now(ZoneId.of("UTC")).minus(1, ChronoUnit.HOURS);
        LocalDateTime time2 = LocalDateTime.now(ZoneId.of("UTC")).minus(2, ChronoUnit.HOURS);
        LocalDateTime time3 = LocalDateTime.now(ZoneId.of("UTC")).minus(3, ChronoUnit.HOURS);
        LocalDateTime time4 = LocalDateTime.now(ZoneId.of("UTC")).minus(7, ChronoUnit.HOURS);
        PokerGameDetail pokerGameDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 3,time1,twitterUrl);
        PokerGameDetail pokerGameDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/1"), 4,time1,twitterUrl);
        PokerGameDetail pokerGameDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 5,time2,twitterUrl);
        PokerGameDetail pokerGameDetail4 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "NLH", "1/2"), 5,time3,twitterUrl);
        PokerGameDetail pokerGameDetail5 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "NLH", "1/2"), 5,time4,twitterUrl);
        when(pokerGameStore.getPokerGameDetails()).thenReturn(Arrays.asList(pokerGameDetail1,pokerGameDetail2,
                pokerGameDetail3,pokerGameDetail4,pokerGameDetail5));

        List<PokerGameDetail> details = underTest.pokerGameDetails();

        assertThat(details).hasSize(3);
        assertThat(details).containsExactlyInAnyOrder(pokerGameDetail1,pokerGameDetail2,pokerGameDetail4);

    }

}