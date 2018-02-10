package database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.String.format;
import static java.lang.System.getenv;

@Slf4j
public class DatabaseModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    Connection connection(@Named("db.url") String databaseUrl,
                          @Named("db.username") String username,
                          @Named("db.pass") String password) throws SQLException {

        Properties connectionProps = new Properties();
        connectionProps.put("user", username);
        connectionProps.put("password", password);

        Connection connection = DriverManager.getConnection(
                databaseUrl, connectionProps);

        log.info(format("Successfully connected to [%s] using JDBC", databaseUrl));

        return connection;
    }

    @Provides
    @Singleton
    Session session(@Named("hibernate.config.filename") String configFile) {
        Configuration config = new Configuration();
        //TODO: find better way to find if app runs on Heroku or not
        if (!configFile.contains("local")) {
            config.setProperty("hibernate.connection.url", getenv("JDBC_DATABASE_URL"));
            config.setProperty("hibernate.connection.username", getenv("JDBC_DATABASE_USERNAME"));
            config.setProperty("hibernate.connection.password", getenv("JDBC_DATABASE_PASSWORD"));
        }
        SessionFactory sessionFactory = config.configure(configFile)
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        log.info(format("Successfully connected to database[%s] using Hibernate", sessionFactory
                .getProperties().get("hibernate.connection.url")));
        return session;
    }

}
