package database;

import com.google.inject.Guice;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerVenue;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

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

        PokerGame pokerGame = PokerGame.builder().venue(PokerVenue.Empire).game("NLH")
                .limit("1/2").build();

        PokerGameDetail detail1 = PokerGameDetail.builder()
                .pokerGame(pokerGame).numberOfTables(4).updatedAt(updatedAt).build();

        PokerGameDetail detail2 = PokerGameDetail.builder()
                .pokerGame(pokerGame).numberOfTables(5).updatedAt(updatedAt).build();

        pokerGameStore.persistPokerGameDetail(detail1);
        pokerGameStore.persistPokerGameDetail(detail2);

    }

}