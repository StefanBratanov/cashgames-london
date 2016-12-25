import common.GuiceInitialiser;
import database.DatabaseModule;
import extractors.ExtractorsModule;
import lombok.extern.slf4j.Slf4j;
import properties.PropertiesModule;
import server.MyJerseyServletModule;
import server.ServerModule;
import services.ServiceModule;
import twitter.TwitterModule;
import twitter_stream.TwitterStreamModule;

@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {

        GuiceInitialiser.createAndStartServices(
                new ServiceModule(),
                new TwitterModule(),
                new TwitterStreamModule(),
                new ExtractorsModule(),
                new DatabaseModule(),
                new PropertiesModule(),
                new MyJerseyServletModule(),
                new ServerModule()
        );
    }
}
