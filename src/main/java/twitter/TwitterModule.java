package twitter;

import com.google.inject.AbstractModule;
import common.StatusProcessor;

public class TwitterModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(StatusProcessor.class).to(TwitterStatusProcessor.class);

    }
}
