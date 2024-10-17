package com.github.niketion.bansystem.listeners;

import com.github.niketion.bansystem.BanSystemPlugin;
import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.model.Punishment;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Optional;
import java.util.UUID;

public class ChatListener implements Listener {
    private BanSystemPlugin plugin;
    private BanManager banManager;

    public ChatListener(BanSystemPlugin plugin, BanManager banManager) {
        this.plugin = plugin;
        this.banManager = banManager;
    }

    @EventHandler
    public void on(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Optional<Punishment> isMuted = banManager.getActivePunishments(player.getUniqueId())
                .stream()
                .filter((punishment -> punishment.getType() == Punishment.Type.MUTE))
                .findAny();

        if (isMuted.isPresent()) {
            event.setCancelled(true);
            player.sendMessage(isMuted.get().getMessage());
        }
    }
}
