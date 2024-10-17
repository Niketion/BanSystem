package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.StorageManager;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.model.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnMuteCommand implements CommandExecutor {
    private BanManager manager;
    private StorageManager storageManager;

    public UnMuteCommand(BanManager manager, StorageManager storageManager) {
        this.manager = manager;
        this.storageManager = storageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("mute.player")) {
            return false;
        }

        if (strings.length < 1) {
            commandSender.sendMessage("/unmute <player>");
            return false;
        }

        String playerName = strings[0];

        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        BanPlayer banPlayer = this.manager.getBanPlayer(uuid);

        if (banPlayer == null) {
            commandSender.sendMessage("Player not found...");
            return false;
        }

        List<Punishment> activeBans = manager.getActivePunishments(uuid)
                .stream()
                .filter(punishment -> punishment.getType() == Punishment.Type.MUTE)
                .collect(Collectors.toList());

        if (activeBans.isEmpty()) {
            commandSender.sendMessage("Player isn't muted.");
            return true;
        }
        activeBans.forEach(punishment -> {
            punishment.setToDate(System.currentTimeMillis());
            punishment.setPermanent(false);
            this.storageManager.updatePunishment(punishment);
        });

        commandSender.sendMessage("Player is now not muted.");
        return true;
    }
}
