package model;

import common.LocalDateTimeSerializer;
import lombok.Data;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class PokerVenueDetail implements Serializable {

    public PokerVenueDetail() {
    }

    public PokerVenueDetail(PokerVenue pokerVenue, String profilePicUrl) {
        this.pokerVenue = pokerVenue;
        this.profilePicUrl = profilePicUrl;
    }

    @Id
    @Enumerated(EnumType.STRING)
    private PokerVenue pokerVenue;

    @Column(name = "profilePicUrl")
    private String profilePicUrl;

    @Transient
    private List<PokerGameDetail> pokerGameDetails;

    @Transient
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime latestUpdateTime;

}
