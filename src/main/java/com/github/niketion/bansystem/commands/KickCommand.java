package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.ConfigManager;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.model.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class KickCommand implements CommandExecutor {
    private BanManager manager;
    private ConfigManager configManager;

    public KickCommand(ConfigManager configManager, BanManager manager) {
        this.manager = manager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("kick.player")) {
            return false;
        }

        if (strings.length < 2) {
            commandSender.sendMessage(ConfigManager.Value.KICK_USAGE.toString());
            return false;
        }

        String playerName = strings[0];
        String message = String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));

        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        BanPlayer banPlayer = this.manager.getBanPlayer(uuid);

        if (banPlayer == null) {
            commandSender.sendMessage(ConfigManager.Value.PLAYER_NOT_FOUND.toString());
            return false;
        }

        commandSender.sendMessage(ConfigManager.Value.PLAYER_KICKED.formatted(playerName));
        manager.kickPlayer(banPlayer, commandSender.getName(), message);
        return true;
    }
}
