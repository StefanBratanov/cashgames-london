import common.GuiceInitialiser;
import database.DatabaseModule;
import extractors.ExtractorsModule;
import services.ServiceModule;
import lombok.extern.slf4j.Slf4j;
import twitter.TwitterModule;
import twitter_stream.TwitterStreamModule;

@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {

        GuiceInitialiser.createAndStartServices(new ServiceModule(),
                new TwitterModule(),
                new TwitterStreamModule(),
                new ExtractorsModule(),
                new DatabaseModule());
    }
}
