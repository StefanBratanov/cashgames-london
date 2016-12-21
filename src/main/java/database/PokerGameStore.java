package database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PokerGameDetail;
import org.hibernate.Session;

@Singleton
@RequiredArgsConstructor(onConstructor = @_(@Inject))
@Slf4j
public class PokerGameStore {

    private final Session session;

    public synchronized void persistPokerGameDetail(PokerGameDetail detail) {

        session.beginTransaction();

        session.flush();
        session.saveOrUpdate(detail.getPokerGame());
        session.saveOrUpdate(detail);
        session.flush();

        session.getTransaction().commit();

    }
}
