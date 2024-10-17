package com.github.niketion.bansystem.model;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

public class BanPlayer {
    private int id;
    private UUID uuid;
    private List<Punishment> punishments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Punishment> getPunishments() {
        return punishments;
    }

    public void setPunishments(List<Punishment> punishments) {
        this.punishments = punishments;
    }
}
