package com.github.niketion.bansystem.manager;

import com.github.niketion.bansystem.BanSystemPlugin;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.model.Punishment;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BanManager {
    private BanSystemPlugin plugin;
    Map<UUID, BanPlayer> loadedPlayers;

    public BanManager(BanSystemPlugin plugin) {
        this.plugin = plugin;
        this.loadedPlayers = Maps.newHashMap();
    }

    public BanPlayer getBanPlayer(UUID uuid) {
        return this.loadedPlayers.get(uuid);
    }

    public void createBanPlayer(UUID uuid) {
        if (this.loadedPlayers.containsKey(uuid)) {
            return;
        }

        BanPlayer banPlayer = new BanPlayer();
        banPlayer.setPunishments(new ArrayList<>());
        banPlayer.setUuid(uuid);
        plugin.getStorageManager().insertBanPlayer(banPlayer);
        this.loadedPlayers.put(uuid, banPlayer);
    }

    public void createPunishment(BanPlayer player, String staffer, Punishment.Type type, String message, long toDate, boolean permanent) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
        if (bukkitPlayer != null && bukkitPlayer.isOnline() && (type == Punishment.Type.BAN || type == Punishment.Type.KICK)) {
            bukkitPlayer.kickPlayer(message);
        }

        Punishment punishment = new Punishment();
        punishment.setType(type);
        punishment.setStaffer(staffer);
        punishment.setIdPlayer(player.getId());
        punishment.setFromDate(System.currentTimeMillis());
        punishment.setToDate(toDate);
        punishment.setMessage(message);
        punishment.setPermanent(permanent);

        List<Punishment> punishments = player.getPunishments();
        punishments.add(punishment);
        player.setPunishments(punishments);
        plugin.getStorageManager().insertPunishment(punishment);
    }

    public void createPunishment(BanPlayer player, String staffer, Punishment.Type type, String message, boolean permanent) {
        this.createPunishment(player,staffer,type,message,0,permanent);
    }

    public void createPunishment(BanPlayer player, String staffer, Punishment.Type type, String message, long toDate) {
        this.createPunishment(player,staffer,type,message,toDate,false);
    }

    public void kickPlayer(BanPlayer player, String staffer, String message) {
        this.createPunishment(player,staffer, Punishment.Type.KICK,message,0,false);
    }

    public List<Punishment> getActivePunishments(UUID uuid) {
        if (!loadedPlayers.containsKey(uuid)) return new ArrayList<>();

        BanPlayer player = loadedPlayers.get(uuid);
        List<Punishment> active = new ArrayList<>();
        player.getPunishments().forEach((punishment -> {
            long currentTime = System.currentTimeMillis();
            if (punishment.isPermanent() || punishment.getToDate()  > currentTime) {
                active.add(punishment);
            }
        }));

        return active;
    }
}
