package twitter_stream;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import model.TwitterId;
import lombok.RequiredArgsConstructor;
import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Inject))
public class TwitterStreamService extends AbstractIdleService {

    private final StatusListener statusListener;
    private final TwitterStream twitterStream;
    private final Set<TwitterId> twitterIds;

    @Override
    protected void startUp() {
        long[] twitterIdsArr = twitterIds.stream().mapToLong(TwitterId::getId).toArray();
        twitterStream.addListener(statusListener);
        twitterStream.filter(new FilterQuery(0, twitterIdsArr, new String[0]));
        log.info(format("Will start listen to [%s] for updates", twitterIds.stream()
                .map(TwitterId::getName).collect(Collectors.joining(","))));
    }

    @Override
    protected void shutDown() {
        twitterStream.cleanUp();
        twitterStream.clearListeners();
        twitterStream.shutdown();
    }
}
