package twitter_stream;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import model.PokerVenue;
import model.TwitterId;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.stream.Stream;

public class TwitterStreamModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(StatusListener.class).to(PokerRoomsStatusListener.class);
        bind(TwitterStream.class).toInstance(new TwitterStreamFactory().getInstance());

        Multibinder<TwitterId> twitterIdsBinder = Multibinder.newSetBinder(binder(), TwitterId.class);

        Stream.of(PokerVenue.values())
                .forEach(pokerVenue -> twitterIdsBinder.addBinding().toInstance(TwitterId.builder()
                        .name(pokerVenue.getTwitterName())
                        .id(pokerVenue.getTwitterId()).build()));
    }
}
