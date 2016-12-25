package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@ToString(exclude = "pokerGameDetail")
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
    }

    @Id
    @Enumerated(EnumType.STRING)
    @Getter
    private PokerVenue venue;

    @Id
    @Column(name = "Game")
    @Getter
    private String game;

    @Id
    @Column(name = "Stakes")
    @Getter
    private String limit;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pokerGame", cascade = CascadeType.ALL)
    private PokerGameDetail pokerGameDetail;

}
