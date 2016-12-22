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

    @Before
    public void init() {
        updatedAt = LocalDateTime.of(2016, 07, 28, 12, 23, 5);
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

        PokerGameDetail expectedDetail1 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Vic).game("NLH")
                        .limit("1/1").build()).numberOfTables(3).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail2 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Vic).game("NLH")
                        .limit("1/2").build()).numberOfTables(4).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail3 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Vic).game("PLO")
                        .limit("10/25").build()).numberOfTables(1).updatedAt(updatedAt).build();

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, testStatus, updatedAt);

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

        PokerGameDetail expectedDetail1 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Hippo).game("ROE")
                        .limit("1/2").build()).numberOfTables(6).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail2 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Hippo).game("NLH")
                        .limit("2/5").build()).numberOfTables(1).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail3 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Hippo).game("PLO")
                        .limit("1/2").build()).numberOfTables(1).updatedAt(updatedAt).build();

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, testStatus, updatedAt);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2, expectedDetail3);
        assertThat(actualDetails).hasSize(3);
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

        PokerGameDetail expectedDetail1 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Aspers).game("PLO")
                        .limit("1/2").build()).numberOfTables(1).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail2 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Aspers).game("NLH")
                        .limit("1/2").build()).numberOfTables(1).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail3 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Aspers).game("NLH")
                        .limit("1/1").build()).numberOfTables(3).updatedAt(updatedAt).build();

        String testUserName = "AspersPoker";

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt);

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

        PokerGameDetail expectedDetail1 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Aspers).game("NLH")
                        .limit("1/1").build()).numberOfTables(3).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail2 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Aspers).game("NLH")
                        .limit("1/2").build()).numberOfTables(4).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail3 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Aspers).game("PLO")
                        .limit("1/2").build()).numberOfTables(1).updatedAt(updatedAt).build();

        String testUserName = "AspersPoker";

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt);

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

        PokerGameDetail expectedDetail1 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Aspers).game("PLO")
                        .limit("1/2").build()).numberOfTables(1).updatedAt(updatedAt).build();

        PokerGameDetail expectedDetail2 = PokerGameDetail.builder()
                .pokerGame(PokerGame.builder().venue(PokerVenue.Aspers).game("NLH")
                        .limit("1/2").build()).numberOfTables(2).updatedAt(updatedAt).build();

        List<PokerGameDetail> actualDetails = underTest.extract(testUserName, statusText, updatedAt);

        assertThat(actualDetails).contains(expectedDetail1, expectedDetail2);
        assertThat(actualDetails).hasSize(2);

    }



    @Test
    public void testNotMatchingStatusTextReturnsEmptyList() {
        String statusText = "crap";
        String testUserName = "ThePokerRoomUK";

        Assert.assertThat(underTest.extract(testUserName, statusText, updatedAt).size(), is(0));
    }
}