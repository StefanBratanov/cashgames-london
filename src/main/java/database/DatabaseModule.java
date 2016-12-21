package database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static java.lang.String.format;

@Slf4j
public class DatabaseModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    Session session() {
        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        Session session =  sessionFactory.openSession();
        log.info(format("Successfully connected to database[%s] using Hibernate",sessionFactory
                .getProperties().get("hibernate.connection.url")));
        return session;
    }

}
