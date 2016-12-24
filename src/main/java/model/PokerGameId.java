package model;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class PokerGameId implements Serializable{

    private PokerVenue venue;
    private String game;
    private String limit;

}
