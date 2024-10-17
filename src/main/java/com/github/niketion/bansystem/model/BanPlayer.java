package com.github.niketion.bansystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class BanPlayer {
    private int id;
    private UUID uuid;
    private List<Punishment> punishments;
}
