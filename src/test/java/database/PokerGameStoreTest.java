package database;

import com.google.inject.Guice;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerVenue;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;

public class PokerGameStoreTest {

    private PokerGameStore pokerGameStore;

    private Session session;

    @Before
    public void init() {

        Session injectedSession = Guice.createInjector(new DatabaseModule()).getInstance(Session.class);

        injectedSession.beginTransaction();
        injectedSession.createQuery("delete from PokerGameDetail").executeUpdate();
        injectedSession.createQuery("delete from PokerGame").executeUpdate();
        injectedSession.getTransaction().commit();

        pokerGameStore = new PokerGameStore(injectedSession);
        session = pokerGameStore.getSession();
    }

    @Test
    @Ignore
    public void persistsPokerGameDetailTwiceWithSamePokerGame() {
        LocalDateTime updatedAt = LocalDateTime.of(2016, 07, 28, 12, 23, 5);
        String twitterUrl = "http://www.test.com";

        PokerGame pokerGame = new PokerGame(PokerVenue.Empire, "NLH", "1/2");

        PokerGameDetail detail1 = new PokerGameDetail(pokerGame, 4, updatedAt, twitterUrl);
        PokerGameDetail detail2 = new PokerGameDetail(pokerGame, 5, updatedAt, twitterUrl);

        pokerGameStore.persistPokerGameDetail(detail1);
        pokerGameStore.persistPokerGameDetail(detail2);

    }

}