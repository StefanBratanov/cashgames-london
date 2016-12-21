package model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Data
@Entity
@IdClass(PokerGameId.class)
public class PokerGame implements Serializable {


    @Id
    @Enumerated(EnumType.STRING)
    private final PokerVenue venue;

    @Id
    @Column(name = "Game")
    private final String game;

    @Id
    @Column(name = "Stakes")
    private final String limit;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pokerGame", cascade = CascadeType.ALL)
    private PokerGameDetail pokerGameDetail;
}
