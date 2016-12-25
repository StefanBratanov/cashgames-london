package server;

import api.PokerGamesApi;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

public class MyJerseyServletModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {
        bind(PokerGamesApi.class);

        Map<String, String> params = new HashMap<>();
        params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        params.put("com.sun.jersey.config.property.packages", "org.codehaus.jackson.jaxrs");
        serve("/*").with(GuiceContainer.class, params);
    }
}
