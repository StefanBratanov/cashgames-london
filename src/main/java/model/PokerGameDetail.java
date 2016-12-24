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

    public PokerGameDetail(PokerGame pokerGame, Integer numberOfTables, LocalDateTime updatedAt) {
        this.pokerGame = pokerGame;
        this.numberOfTables = numberOfTables;
        this.updatedAt = updatedAt;
    }

    @Id
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PokerGame pokerGame;

    @Column(name = "NumberOfTables")
    private Integer numberOfTables;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    public PokerGame getPokerGame() {
        return pokerGame;
    }

}
