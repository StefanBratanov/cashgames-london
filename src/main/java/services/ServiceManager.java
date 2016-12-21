package services;

import com.google.common.util.concurrent.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static java.lang.String.format;

@Slf4j
public class ServiceManager {

    private Set<Service> services;

    public ServiceManager(Set<Service> services) {
        this.services = services;
    }

    public void startAll() {
        services.forEach(service -> {
            service.startAsync();
            service.awaitRunning();
            log.info(format("[%s] has been started", service.getClass().getTypeName()));
        });
    }

    public void stopAll() {
        services.forEach(service -> {
            service.stopAsync();
            service.awaitTerminated();
            log.info(format("[%s] has been stopped", service.getClass().getCanonicalName()));
        });

    }
}
