package model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@EqualsAndHashCode
@Entity
@IdClass(PokerGameId.class)
public class PokerGame implements Serializable {

    public PokerGame() {
    }

    public PokerGame(PokerVenue venue, String game, String limit) {
        this.venue = venue;
        this.game = game;
        this.limit = limit;
        this.pokerGameDetail = null;
    }

    @Id
    @Enumerated(EnumType.STRING)
    private PokerVenue venue;

    @Id
    @Column(name = "Game")
    private String game;

    @Id
    @Column(name = "Stakes")
    private String limit;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pokerGame", cascade = CascadeType.ALL)
    private PokerGameDetail pokerGameDetail;

}
