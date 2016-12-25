package common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import services.ServiceManager;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class GuiceInitialiser{

    public static void createAndStartServices(Module... modules) {

        Injector injector = Guice.createInjector(modules);

        ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        Session session = injector.getInstance(Session.class);
        Connection connection = injector.getInstance(Connection.class);

        serviceManager.startAll();

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
