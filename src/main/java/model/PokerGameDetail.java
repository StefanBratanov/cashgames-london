package model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@Entity
public class PokerGameDetail implements Serializable {

    @Id
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final PokerGame pokerGame;

    @Column(name = "NumberOfTables")
    private final Integer numberOfTables;

    @Column(name = "UpdatedAt")
    private final LocalDateTime updatedAt;
}
