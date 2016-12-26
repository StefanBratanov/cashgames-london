package database;

import com.google.inject.Guice;
import com.google.inject.Injector;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerVenue;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import properties.PropertiesModule;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PokerGameStoreTest {

    private PokerGameStore underTest;

    @Before
    public void init() {

        Injector injector = Guice.createInjector(new DatabaseModule()
                , new PropertiesModule());

        Session injectedSession = injector.getInstance(Session.class);
        Connection injectedConnection = injector.getInstance(Connection.class);

        injectedSession.beginTransaction();
        injectedSession.createQuery("delete from PokerGameDetail").executeUpdate();
        injectedSession.createQuery("delete from PokerGame").executeUpdate();
        injectedSession.getTransaction().commit();

        underTest = new PokerGameStore(injectedSession, injectedConnection);
    }

    @Test
    @Ignore
    public void persistsFourPokerGameDetailsOneWithSamePokerGameAndGetsThem() {
        LocalDateTime updatedAt = LocalDateTime.of(2016, 07, 28, 12, 23, 5);
        String twitterUrl = "http://www.test.com";

        PokerGame pokerGame = new PokerGame(PokerVenue.Empire, "NLH", "1/2");
        PokerGame pokerGame1 = new PokerGame(PokerVenue.Vic, "NLH", "1/2");
        PokerGame pokerGame2 = new PokerGame(PokerVenue.Aspers, "NLH", "1/2");



        PokerGameDetail detail1 = new PokerGameDetail(pokerGame, 4, updatedAt, twitterUrl);
        PokerGameDetail detail2 = new PokerGameDetail(pokerGame, 5, updatedAt, twitterUrl);
        PokerGameDetail detail3 = new PokerGameDetail(pokerGame1, 3, updatedAt, twitterUrl);
        PokerGameDetail detail4 = new PokerGameDetail(pokerGame2, 3, updatedAt, twitterUrl);

        underTest.persistPokerGameDetail(detail1);
        underTest.persistPokerGameDetail(detail2);
        underTest.persistPokerGameDetail(detail3);
        underTest.persistPokerGameDetail(detail4);

        List<PokerGameDetail> actualDetails = underTest.getPokerGameDetails();

        assertThat(actualDetails).hasSize(3);
        assertThat(actualDetails).contains(detail2, detail3,detail4);

    }
}