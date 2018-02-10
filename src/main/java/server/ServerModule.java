package server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceFilter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.URL;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;
import static java.lang.System.getenv;

@Slf4j
public class ServerModule extends AbstractModule {


    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public Server server(@Named("server.port") String port, @Named("virtual.host") String virtualHost) {
        Server server = new Server();

        ContextHandler context = new ContextHandler();
        context.setContextPath("/");

        ResourceHandler resource_handler = new ResourceHandler();
        String baseStr = "/webapp";
        URL baseUrl = getClass().getResource(baseStr);
        String basePath = baseUrl.toExternalForm();
        resource_handler.setResourceBase(basePath);
        resource_handler.setWelcomeFiles(new String[]{"index.html"});

        context.setHandler(resource_handler);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addFilter(GuiceFilter.class, "/*", null);
        servletContextHandler.addServlet(DefaultServlet.class, "/");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context, servletContextHandler});

        server.setHandler(handlers);

        final ServerConnector connector = new ServerConnector(server);
        int portNumber;
        if (isNullOrEmpty(port)) {
            portNumber = Integer.valueOf(getenv("PORT"));
        } else {
            portNumber = Integer.valueOf(port);
        }
        connector.setPort(portNumber);
        connector.setHost(virtualHost);
        server.setConnectors(new Connector[]{connector});

        log.info(format("Jetty server is setup to start on port [%s]", port));

        return server;
    }
}
