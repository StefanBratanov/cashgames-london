package twitter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import common.StatusProcessor;
import database.PokerDao;
import extractors.PokerGameDetailsExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PokerGameDetail;
import model.PokerVenue;
import model.PokerVenueDetail;
import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Inject))
class TwitterStatusProcessor implements StatusProcessor {

    private final PokerGameDetailsExtractor pokerGameDetailsExtractor;
    private final PokerDao pokerDao;

    @Override
    public synchronized void process(Status status) {
        String screenName = status.getUser().getScreenName();
        if (!PokerVenue.isExistingTwitterName(screenName)) {
            log.info(format("Ignoring new status from [%s]", screenName));
            return;
        }

        String twitterUrl = "https://twitter.com/" + status.getUser().getScreenName()
                + "/status/" + status.getId();

        String statusText = status.getText();
        LocalDateTime updatedAt = LocalDateTime.ofInstant(status.getCreatedAt().toInstant(), ZoneId.of("Europe/London"));

        log.info(format("New status received [%s] from [%s] at [%s]", statusText, screenName, updatedAt.toString()));

        PokerVenue pokerVenue = PokerVenue.fromTwitterName(screenName);

        PokerVenueDetail pokerVenueDetail = new PokerVenueDetail(pokerVenue, status.getUser().getOriginalProfileImageURL());
        pokerDao.persistPokerVenueDetail(pokerVenueDetail);
        log.info("Poker Venue Detail has been persisted to the database [{}]",pokerVenueDetail.toString());

        List<PokerGameDetail> details = pokerGameDetailsExtractor.extract(pokerVenue,
                statusText, updatedAt, twitterUrl);

        if (details.isEmpty()) {
            log.info("The statusText does not have cash game information");
        } else {
            log.info("Extracted Details: " + details.stream().map(PokerGameDetail::toString).collect(Collectors.joining(",")));
            details.forEach(detail -> {
                pokerDao.persistPokerGameDetail(detail);
                log.info(format("Detail[%s] has been persisted to the database", detail.toString()));
            });
        }
    }
}
