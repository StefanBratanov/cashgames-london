package properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

import static java.lang.System.getenv;
import static java.util.Objects.nonNull;

@Slf4j
public class PropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        Properties defaults = new Properties();
        try {
            Properties props = new Properties(defaults);
            String propertiesFilename;
            String hibernateConfigFilename;
            //TODO: find better way to determine what properties to use for heroku
            if (nonNull(getenv("JDBC_DATABASE_URL"))) {
                propertiesFilename = "cashgames.properties";
                hibernateConfigFilename = "hibernate.cfg.xml";
                props.put("db.url", getenv("JDBC_DATABASE_URL"));
                props.put("db.username", getenv("JDBC_DATABASE_USERNAME"));
                props.put("db.pass", getenv("JDBC_DATABASE_PASSWORD"));
            } else {
                propertiesFilename = "cashgames-local.properties";
                hibernateConfigFilename = "hibernate-local.cfg.xml";
            }
            InputStream propertiesFileInputStream = getClass().getClassLoader()
                    .getResourceAsStream(propertiesFilename);
            props.load(propertiesFileInputStream);
            props.setProperty("hibernate.config.filename", hibernateConfigFilename);
            log.info("Loading properties from {} and {}", propertiesFilename, hibernateConfigFilename);
            Names.bindProperties(binder(), props);
        } catch (Exception e) {
            log.error("Could not load config: ", e);
            throw new RuntimeException(e);
        }

    }
}
