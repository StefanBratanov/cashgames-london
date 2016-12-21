package extractors;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ExtractorsModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    PokerGameDetailsExtractor pokerGameDetailsExtractor(LimitAndTablesExtractor limitAndTablesExtractor) {
        return new PokerGameDetailsExtractor(limitAndTablesExtractor);
    }
}
