package model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PokerVenueTest {

    @Test
    public void getsPokerVenueFromTwitterName() {
		

        PokerVenue actual = PokerVenue.fromTwitterName("ThePokerRoomUK");

        assertThat(actual, is(PokerVenue.Vic));
    }

    @Test
    public void getsPokerVenueFromTwitterNameWithDodgySpaceAndSomeLowercase() {

        PokerVenue actual = PokerVenue.fromTwitterName("ThepokerRoomUK ");

        assertThat(actual, is(PokerVenue.Vic));
    }

    @Test(expected = IllegalStateException.class)
    public void throwsExceptionIfNotFound() {

        PokerVenue.fromTwitterName("crap");
    }

    @Test
    public void checksIfItIsExistingTwitterName() {
        boolean first = PokerVenue.isExistingTwitterName("ThePokerRoomUK");
        boolean second = PokerVenue.isExistingTwitterName("crap");

        assertTrue(first);
        assertFalse(second);

    }
}