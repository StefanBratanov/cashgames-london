package model;

import java.io.Serializable;

public enum PokerVenue implements Serializable {

    Vic("ThePokerRoomUK", 181360931L),
    Hippo("PSLive_Hippo", 715122811L),
    Aspers("AspersPoker", 482125393L);

    private final String twitterName;
    private final Long twitterId;

    PokerVenue(String twitterName, Long twitterId) {
        this.twitterName = twitterName;
        this.twitterId = twitterId;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public Long getTwitterId() {
        return twitterId;
    }

    public static PokerVenue fromTwitterName(String twitterName) {
        if (twitterName != null) {
            for (PokerVenue venue : PokerVenue.values()) {
                if (twitterName.trim().equalsIgnoreCase(venue.getTwitterName())) {
                    return venue;
                }
            }
        }
        throw new IllegalStateException("Could not retrieve poker venue from: " + twitterName);
    }
}
