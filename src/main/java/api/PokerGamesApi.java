package api;

import com.google.inject.Inject;
import database.PokerGameStore;
import model.PokerGameDetail;
import model.PokerVenue;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api")
public class PokerGamesApi {

    @Inject
    private PokerGameStore pokerGameStore;

    @GET
    @Path("poker-games")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PokerGameDetail> pokerGameDetails() {

        List<PokerGameDetail> pokerGameDetails = pokerGameStore.getPokerGameDetails();

        Map<PokerVenue, LocalDateTime> pokerVenueByLatestUpdatedTime = pokerGameDetails.stream()
                .filter(pokerGameDetail -> {
                    LocalDateTime updatedAt = pokerGameDetail.getUpdatedAt();
                    return LocalDateTime.now(ZoneId.of("UTC"))
                            .minusHours(6).isBefore(updatedAt);
                })
                .collect(Collectors.groupingBy((pokerGameDetail) -> pokerGameDetail.getPokerGame().getVenue()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry ->
                        entry.getValue().stream().reduce((d1, d2) -> d1.getUpdatedAt().isAfter(d2.getUpdatedAt()) ? d1 : d2)
                                .get().getUpdatedAt()));

        return pokerGameDetails.stream()
                .filter(pokerGameDetail -> {
                    Optional<LocalDateTime> latestTime = Optional.ofNullable(pokerVenueByLatestUpdatedTime.get
                            (pokerGameDetail.getPokerGame().getVenue()));
                    if (latestTime.isPresent()) {
                        return pokerGameDetail.getUpdatedAt().equals(latestTime.get());
                    } else {
                        return false;
                    }
                }).sorted((x, y) -> x.getPokerGame().getVenue().name()
                        .compareTo(y.getPokerGame().getVenue().name()))
                .collect(Collectors.toList());
    }
}
