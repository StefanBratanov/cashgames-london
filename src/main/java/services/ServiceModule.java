package services;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import twitter_stream.TwitterStreamService;

import java.util.Set;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {

        Multibinder<Service> serviceMultibinder = Multibinder.newSetBinder(binder(), Service.class);

        serviceMultibinder.addBinding().to(TwitterStreamService.class);

    }

    @Provides
    @Singleton
    ServiceManager serviceManager(Set<Service> services) {
        return new ServiceManager(services);
    }
}
