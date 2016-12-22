package twitter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import common.StatusProcessor;
import database.PokerGameStore;
import extractors.PokerGameDetailsExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PokerGameDetail;
import model.PokerVenue;
import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Inject))
class TwitterStatusProcessor implements StatusProcessor {

    private final PokerGameDetailsExtractor pokerGameDetailsExtractor;
    private final PokerGameStore pokerGameStore;

    @Override
    public synchronized void process(Status status) {
        String screenName = status.getUser().getScreenName();
        if (!PokerVenue.isExistingTwitterName(screenName)) {
            log.info(format("Ignoring new status from [%s]", screenName));
            return;
        }

        String statusText = status.getText();
        LocalDateTime updatedAt = LocalDateTime.ofInstant(status.getCreatedAt().toInstant(), ZoneId.of("UTC"));

        log.info(format("New status received [%s] from [%s] at [%s]", statusText, screenName, updatedAt.toString()));

        List<PokerGameDetail> details = pokerGameDetailsExtractor.extract(screenName, statusText, updatedAt);

        if (details.isEmpty()) {
            log.info("The statusText does not have cash game information");
        } else {
            log.info("Extracted Details: " + details.stream().map(PokerGameDetail::toString).collect(Collectors.joining(",")));
            details.forEach(detail -> {
                pokerGameStore.persistPokerGameDetail(detail);
                log.info(format("Detail[%s] has been persisted to the database", detail.toString()));
            });
        }
    }
}
