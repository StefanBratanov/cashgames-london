import common.GuiceInitialiser;
import database.DatabaseModule;
import extractors.ExtractorsModule;
import properties.PropertiesModule;
import server.MyJerseyServletModule;
import server.ServerModule;
import services.ServiceModule;
import twitter.TwitterModule;
import twitter_stream.TwitterStreamModule;

public class Main {

    public static void main(String[] args) {

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
