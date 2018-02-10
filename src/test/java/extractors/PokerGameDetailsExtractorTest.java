package extractors;

import model.PokerGame;
import model.PokerGameDetail;
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
        updatedAt = LocalDateTime.of(2016, 7, 28, 12, 23, 5);
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

        PokerVenue pokerVenue = PokerVenue.Vic;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "NLH", "1/1"), 3, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "NLH", "1/2"), 4, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Vic, "PLO", "10/25"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, testStatus, updatedAt, twitterUrl);

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

        PokerVenue pokerVenue = PokerVenue.Hippo;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "ROE", "1/2"), 6, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "2/5"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "PLO", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, testStatus, updatedAt, twitterUrl);

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

        PokerVenue pokerVenue = PokerVenue.Hippo;

        PokerGameDetail expectedDetail = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, testStatus, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail);
        assertThat(actualDetails).hasSize(1);
    }

    @Test
    public void extractDetailsFromHippo3() {
        String testStatus = "Cash Game Update\n" +
                "\n" +
                "1 X £2/£5 NLH\n" +
                "6 x £1/£2 NLH\n" +
                "\n" +
                "We Are Poker,\n" +
                "Any Game, Any Time!\n" +
                "\n" +
                "#PSLive\n" +
                "#PokerStarsFestival";

        PokerVenue pokerVenue = PokerVenue.Hippo;

        PokerGameDetail expectedDetail = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "2/5"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "1/2"), 6, updatedAt, twitterUrl);


        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, testStatus, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail, expectedDetail1);
        assertThat(actualDetails).hasSize(2);
    }

    @Test
    public void extractDetailsFromHippo4() {
        String testStatus = "\uD83D\uDCA5£1/£2 NLH DEEPSTACK IS OPEN!!!!\uD83D\uDCA5\n" +
                "\n" +
                "CASH GAME UPDATE:\n" +
                "\n" +
                "♦️5  x £1/£2 NLH\n" +
                "♦️1  x £1/£2 NLH DEEPSTACK (£150 min- no max)\n" +
                "\n" +
                "#PSLive\n" +
                "#PokerstarsMegaStackLondon 23rd-25th February";

        PokerVenue pokerVenue = PokerVenue.Hippo;

        PokerGameDetail expectedDetail = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "1/2"), 5, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH Deepstack", "1/2"), 1, updatedAt, twitterUrl);


        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, testStatus, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail, expectedDetail1);
        assertThat(actualDetails).hasSize(2);
    }

    @Test
    public void extractDetailsFromHippo5() {
        String testStatus = "CASH GAME UPDATE:\n" +
                "\n" +
                "1 x £1/£2 NLH DeepStack (£150 Min - No Max)\n" +
                "6 x £1/£2 NLH \n" +
                "\n" +
                "We Are Poker!\n" +
                "Any Game, Any Time!\n" +
                "\n" +
                "#PSLive\n" +
                "#PokerstarsMegastackLondon 23rd - 25th February 2018";

        PokerVenue pokerVenue = PokerVenue.Hippo;

        PokerGameDetail expectedDetail = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "1/2"), 6, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH Deepstack", "1/2"), 1, updatedAt, twitterUrl);


        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, testStatus, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail, expectedDetail1);
        assertThat(actualDetails).hasSize(2);
    }

    @Test
    public void extractDetailsFromHippo6() {
        String testStatus = "\uD83D\uDCA5Cash Game Update\uD83D\uDCA5\n" +
                "\n" +
                "\uD83D\uDC492 x £1/£2 Deepstack NLH (£150 min - No max)\n" +
                "\n" +
                "\uD83D\uDC496 x £1/£2 NLH\n" +
                "\n" +
                "♠️We Are Poker♥️\n" +
                "♦️Any Game, Any Time♣️";

        PokerVenue pokerVenue = PokerVenue.Hippo;

        PokerGameDetail expectedDetail = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH", "1/2"), 6, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Hippo, "NLH Deepstack", "1/2"), 2, updatedAt, twitterUrl);


        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, testStatus, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail, expectedDetail1);
        assertThat(actualDetails).hasSize(2);
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

        PokerVenue pokerVenue = PokerVenue.Aspers;

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

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

        PokerVenue pokerVenue = PokerVenue.Aspers;

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

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

        PokerVenue pokerVenue = PokerVenue.Aspers;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "PLO", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers, "NLH", "1/2"), 2, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

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

        PokerVenue pokerVenue = PokerVenue.Aspers;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/2"), 2, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/1"), 2, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2);
        assertThat(actualDetails).hasSize(2);

    }

    @Test
    public void extractDetailsFromAspers5() {
        String statusText = "Cash game Update:\n" +
                "\n" +
                "#NLH\n" +
                "2 x £1/2\n" +
                "4 x £1/1\n" +
                "\n" +
                "#AspersPoker";

        PokerVenue pokerVenue = PokerVenue.Aspers;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/2"), 2, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/1"), 4, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2);
        assertThat(actualDetails).hasSize(2);

    }

    @Test
    public void extractDetailsFromAspers6() {
        String statusText = "Cash Game Update:\n" +
                "\n" +
                "#PLO\n" +
                "1x £1/2\n" +
                "#NLH\n" +
                "3x £1/2 \n" +
                "3x £1/1\n" +
                "\n" +
                "#AspersPoker\n" +
                "#DoublePointsDay";

        PokerVenue pokerVenue = PokerVenue.Aspers;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "PLO", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/2"), 3, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/1"), 3, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);

    }

    @Test
    public void extractDetailsFromAspers7() {
        String statusText = "Cash Game Update:\n" +
                "#PLO\n" +
                "£1/£2 x 1\n" +
                "#NLH\n" +
                "£1/£1 x 5\n" +
                "£1/£2 x 2\n" +
                "\n" +
                "#AspersPoker";

        PokerVenue pokerVenue = PokerVenue.Aspers;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "PLO", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/2"), 2, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/1"), 5, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);
    }

    @Test
    public void extractDetailsFromAspers8() {
        String statusText = "Aspers Poker Cash Game Update: #NLH £1/1(3) £1/2(1) #PLO £1/2 (1)";

        PokerVenue pokerVenue = PokerVenue.Aspers;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "PLO", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/1"), 3, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);
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

        PokerVenue pokerVenue = PokerVenue.Empire;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 5, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

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

        PokerVenue pokerVenue = PokerVenue.Empire;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 5, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "PLO", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

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

        PokerVenue pokerVenue = PokerVenue.Empire;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 4, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

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

        PokerVenue pokerVenue = PokerVenue.Empire;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 5, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "PLO", "1/2"), 1, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2);
        assertThat(actualDetails).hasSize(2);
    }

    @Test
    public void extractDetailsFromEmpire5() {
        String statusText = "4 1/2 games running now!!\n" +
                "\n" +
                "Still 5 tables in the tourney.\n" +
                "\n" +
                "#WPAE";

        PokerVenue pokerVenue = PokerVenue.Empire;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 4, updatedAt, twitterUrl);


        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1);
        assertThat(actualDetails).hasSize(1);
    }

    @Test
    public void extractDetailsFromEmpire6() {
        String statusText = "Let there be 10!\n" +
                "\n" +
                "1 x £1/£2 NLH\n" +
                "\n" +
                "2pm comp is our £20 rebuy \n" +
                "\n" +
                "Badbeat jackpot is £52,871\n" +
                "\n" +
                "#56suited \n" +
                "#teamempire \n" +
                "#wpae";

        PokerVenue pokerVenue = PokerVenue.Empire;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 1, updatedAt, twitterUrl);


        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1);
        assertThat(actualDetails).hasSize(1);
    }

    @Test
    public void extractDetailsFromEmpire7() {
        String statusText = "PLO is now open....Buzzing!!!\n" +
                "\n" +
                "1 x £1/£2 PLO 456 single\n" +
                "4 x £1/£2 NLH\n" +
                "4 x Tourney\n" +
                "\n" +
                "#teamempire\n" +
                "#wpae";

        PokerVenue pokerVenue = PokerVenue.Empire;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "PLO", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 4, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1);
        assertThat(actualDetails).contains(expectedDetail2);
        assertThat(actualDetails).hasSize(2);
    }

    @Test
    public void extractDetailsFromEmpire8() {
        String statusText = "We don't have a Garden - we have 8\n" +
                "1 x 1/2 PLO mixed 4/5/6\n" +
                "7 x 1/2 NLH\n" +
                "\n" +
                "#teamempire\n" +
                "#56suited\n" +
                "#wpae";

        PokerVenue pokerVenue = PokerVenue.Empire;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "PLO", "1/2"), 1, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Empire, "NLH", "1/2"), 7, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1);
        assertThat(actualDetails).contains(expectedDetail2);
        assertThat(actualDetails).hasSize(2);
    }

    @Test
    public void extractDetailsFromAspers9() {
        String statusText = "Cash Game Update:\n" +
                "#PLO\n" +
                "£1/£2 x 2\n" +
                "#NLH\n" +
                "£1/£1 x 10\n" +
                "£1/£2 x 5\n" +
                "40K Overlay in 888 Live ME\n" +
                "#AspersPoker #888Live";

        PokerVenue pokerVenue = PokerVenue.Aspers;

        PokerGameDetail expectedDetail1 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "PLO", "1/2"), 2, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail2 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/1"), 10, updatedAt, twitterUrl);
        PokerGameDetail expectedDetail3 = new PokerGameDetail(new PokerGame(PokerVenue.Aspers
                , "NLH", "1/2"), 5, updatedAt, twitterUrl);

        List<PokerGameDetail> actualDetails = underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);
    }

    @Test
    public void testNotMatchingStatusTextReturnsEmptyList() {
        String statusText = "crap";
        PokerVenue pokerVenue = PokerVenue.Vic;

        Assert.assertThat(underTest.extract(pokerVenue, statusText, updatedAt, twitterUrl).size(), is(0));
    }
}