package database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PokerGame;
import model.PokerGameDetail;
import org.hibernate.Session;

@Singleton
@RequiredArgsConstructor(onConstructor = @_(@Inject))
@Slf4j
public class PokerGameStore {

    private final Session session;

    public synchronized void persistPokerGameDetail(PokerGameDetail detail) {
        PokerGame pokerGame = detail.getPokerGame();

        session.beginTransaction();
        session.flush();
        session.saveOrUpdate(pokerGame);
        session.saveOrUpdate(detail);
        session.flush();
        session.getTransaction().commit();

        session.evict(pokerGame);
        session.evict(detail);

    }
}
