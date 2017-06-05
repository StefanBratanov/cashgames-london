package api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import database.PokerDao;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerVenue;
import model.PokerVenueDetail;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PokerGamesApiTest {

    private PokerDao pokerDao;
    private PokerGamesApi underTest;

    public static class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(PokerGamesApi.class).toInstance(new PokerGamesApi());
            bind(PokerDao.class).toInstance(mock(PokerDao.class));
        }
    }

    @Before
    public void init() {
        Injector injector = Guice.createInjector(new TestModule());
        pokerDao = injector.getInstance(PokerDao.class);
        underTest = injector.getInstance(PokerGamesApi.class);
    }

    @Test
    public void getsPokerVenueDetails() {
        LocalDateTime updatedAt = LocalDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime latestUpdateTime = LocalDateTime.of(2017,6,1,12,30,29);
        String twitterUrl = "http://www.test.com";
        String imageUrl = "http://www.test.com/image.jpg";
        PokerGameDetail detail1 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "NLH", "1/1"), 3, updatedAt, twitterUrl);
        PokerGameDetail detail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "NLH", "1/2"), 4, updatedAt, twitterUrl);
        PokerGameDetail detail3 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "PLO", "10/25"), 1, updatedAt, twitterUrl);
        PokerGameDetail detail4 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "PLO", "5/10"), 2, updatedAt, twitterUrl);

        PokerVenueDetail venueDetail1 = new PokerVenueDetail(PokerVenue.Vic, imageUrl);
        PokerVenueDetail venueDetail2 = new PokerVenueDetail(PokerVenue.Empire, imageUrl);
        PokerVenueDetail venueDetail3 = new PokerVenueDetail(PokerVenue.Aspers, imageUrl);
        PokerVenueDetail venueDetail4 = new PokerVenueDetail(PokerVenue.Hippo, imageUrl);

        when(pokerDao.getPokerGameDetails()).thenReturn(Arrays.asList(detail1, detail2, detail3, detail4));
        when(pokerDao.getPokerVenueDetails()).thenReturn(Arrays.asList(venueDetail1, venueDetail2, venueDetail3, venueDetail4));
        when(pokerDao.getLatestUpdateTimeForVenue(any())).thenReturn(Optional.of(latestUpdateTime));
        when(pokerDao.getLatestUpdateTimeForVenue(PokerVenue.Hippo)).thenReturn(Optional.empty());


        List<PokerVenueDetail> pokerVenueDetails = (List<PokerVenueDetail>) underTest.pokerVenueDetails().getEntity();

        //setting expected details
        PokerVenueDetail updatedVenueDetail1 = SerializationUtils.clone(venueDetail1);
        PokerVenueDetail updatedVenueDetail2 = SerializationUtils.clone(venueDetail2);
        PokerVenueDetail updatedVenueDetail3 = SerializationUtils.clone(venueDetail3);
        PokerVenueDetail updatedVenueDetail4 = SerializationUtils.clone(venueDetail4);
        updatedVenueDetail1.setPokerGameDetails(Arrays.asList(detail1));
        updatedVenueDetail1.setLatestUpdateTime(latestUpdateTime);
        updatedVenueDetail2.setPokerGameDetails(Arrays.asList(detail3, detail4));
        updatedVenueDetail2.setLatestUpdateTime(latestUpdateTime);
        updatedVenueDetail3.setPokerGameDetails(Arrays.asList(detail2));
        updatedVenueDetail3.setLatestUpdateTime(latestUpdateTime);
        updatedVenueDetail4.setPokerGameDetails(Collections.emptyList());

        assertThat(pokerVenueDetails).hasSize(4);
        assertThat(pokerVenueDetails).containsExactly(updatedVenueDetail3,updatedVenueDetail2,
                updatedVenueDetail4,updatedVenueDetail1);
    }

}