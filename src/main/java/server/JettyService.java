package server;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;

@Slf4j
public class JettyService extends AbstractIdleService {

    @Inject
    private Server server;

    @Override
    protected void startUp() throws Exception {
        server.start();
    }

    @Override
    protected void shutDown() throws Exception {
        server.destroy();
    }
}
