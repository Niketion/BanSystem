package com.github.niketion.bansystem.listeners;

import com.github.niketion.bansystem.BanSystemPlugin;
import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.StorageManager;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.model.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class ConnectionListener implements Listener {
    private BanSystemPlugin plugin;
    private BanManager banManager;

    public ConnectionListener(BanSystemPlugin plugin, BanManager banManager) {
        this.plugin = plugin;
        this.banManager = banManager;
    }

    @EventHandler
    public void on(AsyncPlayerPreLoginEvent event) {
        Optional<Punishment> isBanned = banManager.getActivePunishments(event.getUniqueId())
                .stream()
                .filter((punishment -> punishment.getType() == Punishment.Type.BAN))
                .findAny();

        if (isBanned.isPresent()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, isBanned.get().getMessage());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.banManager.createBanPlayer(player.getUniqueId());
    }
}
