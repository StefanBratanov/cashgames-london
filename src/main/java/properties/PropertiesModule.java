package properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        Properties defaults = new Properties();
        try {
            Properties props = new Properties(defaults);
            InputStream propertiesFileInputStream = getClass().getClassLoader()
                    .getResourceAsStream("cashgames.properties");
            props.load(propertiesFileInputStream);
            Names.bindProperties(binder(), props);
        } catch (Exception e) {
            log.error("Could not load config: ", e);
            throw new RuntimeException(e);
        }

    }
}
