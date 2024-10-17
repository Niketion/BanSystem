package com.github.niketion.bansystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class Punishment {
    private int id;
    private int idPlayer;
    private String staffer;
    private long fromDate;
    private long toDate;
    private Type type;
    private boolean permanent;
    private String message;

    public enum Type {
        BAN,
        MUTE,
        KICK
    }
}
