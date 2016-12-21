package twitter_stream;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import common.StatusProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Inject))
class PokerRoomsStatusListener implements StatusListener {

    private final StatusProcessor statusProcessor;

    @Override
    public void onStatus(Status status) {
        statusProcessor.process(status);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int i) {

    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {
        log.error(e.getMessage());
    }
}
