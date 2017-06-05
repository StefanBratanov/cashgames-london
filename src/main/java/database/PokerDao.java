package database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerVenue;
import model.PokerVenueDetail;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor = @_(@Inject))
@Slf4j
public class PokerDao {

    private final Session session;
    private final Connection connection;

    public void persistPokerGameDetail(PokerGameDetail detail) {
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

    public void persistPokerVenueDetail(PokerVenueDetail detail) {
        session.beginTransaction();
        session.flush();
        session.merge(detail);
        session.flush();
        session.getTransaction().commit();
        session.evict(detail);

    }

    public List<PokerGameDetail> getPokerGameDetails() {
        Statement statement = null;
        String query = "select * from pokergamedetail";

        List<PokerGameDetail> details = new ArrayList<>();

        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String venue = rs.getString("pokergame_venue");
                String game = rs.getString("pokergame_game");
                String limit = rs.getString("pokergame_stakes");
                Integer numberOfTables = rs.getInt("numberoftables");
                Timestamp updatedAtTimestamp = rs.getTimestamp("updatedat");
                LocalDateTime updatedAt = updatedAtTimestamp.toLocalDateTime();
                String twitterUrl = rs.getString("twitterurl");
                PokerGame pokerGame = new PokerGame(PokerVenue.valueOf(venue), game, limit);
                PokerGameDetail pokerGameDetail = new PokerGameDetail(pokerGame,
                        numberOfTables, updatedAt, twitterUrl);
                details.add(pokerGameDetail);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        return details;
    }

    public List<PokerVenueDetail> getPokerVenueDetails() {
        Query<PokerVenueDetail> query = session.createQuery("from PokerVenueDetail");
        return query.list();
    }

    public Optional<LocalDateTime> getLatestUpdateTimeForVenue(PokerVenue pokerVenue) {
        return getPokerGameDetails().stream()
                .filter(detail -> detail.getPokerGame().getVenue().equals(pokerVenue))
                .map(PokerGameDetail::getUpdatedAt)
                .reduce((x,y) -> x.isAfter(y) ? x : y);
    }
}
