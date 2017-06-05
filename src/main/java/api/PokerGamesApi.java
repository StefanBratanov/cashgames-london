package api;

import com.google.inject.Inject;
import database.PokerDao;
import model.PokerGameDetail;
import model.PokerVenue;
import model.PokerVenueDetail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api")
public class PokerGamesApi {

    @Inject
    private PokerDao pokerDao;

    @GET
    @Path("poker-games")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pokerGameDetails() {
        Response.ResponseBuilder builder = Response.ok(getPokerGameDetails());
        builder.cacheControl(noCacheControl());
        return builder.build();
    }

    @GET
    @Path("poker-venues")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pokerVenueDetails() {
        List<PokerVenueDetail> pokerVenueDetails = pokerDao.getPokerVenueDetails().stream()
                .peek(venueDetail -> {
                    List<PokerGameDetail> gameDetails = getPokerGameDetails();
                    List<PokerGameDetail> gameDetailsForVenue = gameDetails.stream()
                            .filter(gameDetail ->
                                    gameDetail.getPokerGame().getVenue().equals(venueDetail.getPokerVenue()))
                            .collect(Collectors.toList());
                    venueDetail.setPokerGameDetails(gameDetailsForVenue);
                    pokerDao.getLatestUpdateTimeForVenue(venueDetail.getPokerVenue())
                            .ifPresent(venueDetail::setLatestUpdateTime);
                })
                .sorted((x, y) -> x.getPokerVenue().name().compareTo(y.getPokerVenue().name()))
                .collect(Collectors.toList());

        Response.ResponseBuilder builder = Response.ok(pokerVenueDetails);
        builder.cacheControl(noCacheControl());
        return builder.build();
    }

    private List<PokerGameDetail> getPokerGameDetails() {
        List<PokerGameDetail> pokerGameDetails = pokerDao.getPokerGameDetails();

        Map<PokerVenue, LocalDateTime> pokerVenueByLatestUpdatedTime = pokerGameDetails.stream()
                .filter(pokerGameDetail -> {
                    LocalDateTime updatedAt = pokerGameDetail.getUpdatedAt();
                    return LocalDateTime.now(ZoneId.of("Europe/London"))
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

    private CacheControl noCacheControl() {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);

        return cc;
    }
}
