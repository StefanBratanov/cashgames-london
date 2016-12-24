package model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@Entity
public class PokerGameDetail implements Serializable {

    public PokerGameDetail() {
    }

    public PokerGameDetail(PokerGame pokerGame, Integer numberOfTables, LocalDateTime updatedAt, String twitterUrl) {
        this.pokerGame = pokerGame;
        this.numberOfTables = numberOfTables;
        this.updatedAt = updatedAt;
        this.twitterUrl = twitterUrl;
    }

    @Id
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PokerGame pokerGame;

    @Column(name = "NumberOfTables")
    private Integer numberOfTables;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "twitterUrl")
    private String twitterUrl;

    public PokerGame getPokerGame() {
        return pokerGame;
    }

}
