package extractors;

import model.PokerGame;
import model.PokerGameDetail;
import model.PokerGame;
import model.PokerVenue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

public class PokerGameDetailsExtractorTest {

    private PokerGameDetailsExtractor underTest;

    private LocalDateTime updatedAt;

    private String twitterUrl;

    @Before
    public void init() {
        updatedAt = LocalDateTime.of(2016, 07, 28, 12, 23, 5);
        twitterUrl = "http://www.test.com";
        LimitAndTablesExtractor limitAndTablesExtractor = new LimitAndTablesExtractor();
        underTest = new PokerGameDetailsExtractor(limitAndTablesExtractor);
    }

    @Test
    public void extractsDetailsFromThePokerRoomUK() {
        String testStatus = "Cash game update\n" +
                "NlH\n" +
                "1-1 (3)\n" +
                "1-2 (4)\n" +
                "\n" +
                "PLO\n" +
                "10-25 (1)\n" +
                "\n" +
                "#Gcpoker";

        String testUserName = "ThePokerRoomUK";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "NLH", "1/1"), 3, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "NLH", "1/2"), 4, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "PLO", "10/25"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, testStatus, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);
    }

    @Test
    public void extractDetailsFromHippo1() {
        String testStatus = "Cash Game Update:\n" +
                "\n" +
                "1 x £1/£2 PLO (4/5/6)\n" +
                "\n" +
                "1 x £2/£5 NLH \n" +
                "\n" +
                "6 x £1/£2 ROE\n" +
                "\n" +
                "We Are Poker\n" +
                "\n" +
                "#PSLive";

        String testUserName = "PSLive_Hippo";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "ROE", "1/2"), 6, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "2/5"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "PLO", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, testStatus, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);
    }

    @Test
    public void extractDetailsFromHippo2() {
        String testStatus = "Rake Free Draw 14:00\n" +
                "\n" +
                "1 x £1/£2 NLH\n" +
                "\n" +
                "We Are Poker,\n" +
                "Any Game, Any Time\n" +
                "\n" +
                "#PSLive\n" +
                "#PokerStarsFestival";

        String testUserName = "PSLive_Hippo";

        PokerGameDetail expectedDetail = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, testStatus, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail);
        assertThat(actualDetails).hasSize(1);
    }

    @Test
    public void extractDetailsFromAspers1() {
        String statusText = "Aspers Poker Cash Game Update:\n" +
                "#PLO\n" +
                "£1/2 (1)\n" +
                "#NLH \n" +
                "£1/2 (1)\n" +
                "£1/1 (3)\n" +
                "#AspersPoker";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "PLO", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "NLH", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "NLH", "1/1"), 3, updatedAt, twitterUrl);

        String testUserName = "AspersPoker";

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);
    }

    @Test
    public void extractDetailsFromAspers2() {
        String statusText = "Cash Game Update:\n" +
                "\n" +
                "NLH\n" +
                "3x £1/1\n" +
                "4x £1/2\n" +
                "\n" +
                "PLO\n" +
                "1x £1/2\n" +
                "\n" +
                "#AspersPoker";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "NLH", "1/1"), 3, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "NLH", "1/2"), 4, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "PLO", "1/2"), 1, updatedAt, twitterUrl);

        String testUserName = "AspersPoker";

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);
    }

    @Test
    public void extractDetailsFromAspers3WithMissingGameType() {
        String statusText = "Cash Game Update: \n" +
                "\n" +
                "1 x £1/£2 Omaha \n" +
                "2 x £1/£2 NLH \n" +
                "3 x £1/£1\n" +
                "\n" +
                "#AspersPoker #ChristmasCracker";

        String testUserName = "AspersPoker";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "PLO", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "NLH", "1/2"), 2, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2);
        assertThat(actualDetails).hasSize(2);

    }

    @Test
    public void extractDetailsFromAspers4() {
        String statusText = "Cash Game Update:\n" +
                "\n" +
                "£1/2 (2)\n" +
                "£1/1 (2)\n" +
                "\n" +
                "#AspersPoker";

        String testUserName = "AspersPoker";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/2"), 2, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/1"), 2, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2);
        assertThat(actualDetails).hasSize(2);

    }

    @Test
    public void extractDetailsFromEmpire1() {
        String statusText = "NEW GAME\n" +
                "1 x Private Table\n" +
                "5 x 1/2 NLH\n" +
                "1 x Tourney FT\n" +
                "\n" +
                "#wpae\n" +
                "#56suited";

        String testUserName = "EmpirePokerRoom";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 5, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1);
        assertThat(actualDetails).hasSize(1);
    }

    @Test
    public void extractDetailsFromEmpire2() {
        String statusText = "STILL SIX GAMES!\n" +
                "\n" +
                "5 x £1/£2 NLH\n" +
                "1 x £1/£2 PLO\n" +
                "\n" +
                "#wpae";

        String testUserName = "EmpirePokerRoom";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 5, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "PLO", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2);
        assertThat(actualDetails).hasSize(2);
    }

    @Test
    public void extractDetailsFromEmpire3() {
        String statusText = "4 £1/£2 games running.\n" +
                "\n" +
                "5 left in the tourney.\n" +
                "\n" +
                "#WPAE";

        String testUserName = "EmpirePokerRoom";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 4, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1);
        assertThat(actualDetails).hasSize(1);
    }

    @Test
    public void extractDetailsFromEmpire4() {
        String statusText = "NEW GAME\n" +
                "1 x 1/2 PLO Mixed 4/5/6\n" +
                "5 x 1/2 NLH\n" +
                "\n" +
                "#wpae\n" +
                "#56suited\n" +
                "#teamempire";

        String testUserName = "EmpirePokerRoom";

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 5, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "PLO", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2);
        assertThat(actualDetails).hasSize(2);
    }


    @Test
    public void testNotMatchingStatusTextReturnsEmptyList() {
        String statusText = "crap";
        String testUserName = "ThePokerRoomUK";

        Assert.assertThat(underTest.extract(testUserName, statusText, updatedAt, twitterUrl).size(), is(0));
    }
}