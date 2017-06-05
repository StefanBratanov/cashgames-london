package common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.Session;
import services.ServiceManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GuiceInitialiser{

    public static void createAndStartServices(Module... modules) {

        Injector injector = Guice.createInjector(modules);

        ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        Session session = injector.getInstance(Session.class);
        Connection connection = injector.getInstance(Connection.class);

        serviceManager.startAll();
		
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder().setDaemon(true).build());

        executorService.scheduleAtFixedRate(() -> {
            String url = "http://pokerinlondon.co.uk/";
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            try {
                HttpResponse response = client.execute(request);
                log.info(String.format("Successfully tried to access webpage, Status Code [%s]",
                        response.getStatusLine().getStatusCode()));
            } catch (IOException e) {
                log.error("IOException while trying to access webpage: " + e.getMessage());
            }

        }, 0, 10, TimeUnit.MINUTES);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            serviceManager.stopAll();
            session.close();
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }));

    }
}
