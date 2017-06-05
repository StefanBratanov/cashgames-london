package properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class PropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        Properties defaults = new Properties();
        try {
            Properties props = new Properties(defaults);
            String propertiesFilename;
            String hibernateConfigFilename;
            if (Optional.ofNullable(System.getenv("OPENSHIFT_APP_NAME")).isPresent()) {
                propertiesFilename = "cashgames.properties";
                hibernateConfigFilename = "hibernate.cfg.xml";
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
