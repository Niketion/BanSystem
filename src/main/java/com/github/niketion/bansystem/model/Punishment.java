package com.github.niketion.bansystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.UUID;

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

    public String getStaffer() {
        return staffer;
    }

    public void setStaffer(String staffer) {
        this.staffer = staffer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
