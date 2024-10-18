package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.ConfigManager;
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
    private ConfigManager configManager;
    private StorageManager storageManager;

    public UnMuteCommand(ConfigManager configManager, BanManager manager, StorageManager storageManager) {
        this.manager = manager;
        this.storageManager = storageManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("mute.player")) {
            return false;
        }

        if (strings.length < 1) {
            commandSender.sendMessage(ConfigManager.Value.UNMUTE_USAGE.toString());
            return false;
        }

        String playerName = strings[0];

        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        BanPlayer banPlayer = this.manager.getBanPlayer(uuid);

        if (banPlayer == null) {
            commandSender.sendMessage(ConfigManager.Value.PLAYER_NOT_FOUND.toString());
            return false;
        }

        List<Punishment> activeBans = manager.getActivePunishments(uuid)
                .stream()
                .filter(punishment -> punishment.getType() == Punishment.Type.MUTE)
                .collect(Collectors.toList());

        if (activeBans.isEmpty()) {
            commandSender.sendMessage(ConfigManager.Value.PLAYER_NOT_MUTED.toString());
            return true;
        }
        activeBans.forEach(punishment -> {
            punishment.setToDate(System.currentTimeMillis());
            punishment.setPermanent(false);
            this.storageManager.updatePunishment(punishment);
        });

        commandSender.sendMessage(ConfigManager.Value.PLAYER_UNMUTED.toString());
        return true;
    }
}
