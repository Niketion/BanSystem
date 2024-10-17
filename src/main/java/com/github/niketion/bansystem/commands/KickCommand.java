package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.manager.BanManager;
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

    public KickCommand(BanManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("kick.player")) {
            return false;
        }

        if (strings.length < 2) {
            commandSender.sendMessage("/kick <player> <message>");
            return false;
        }

        String playerName = strings[0];
        String message = String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));

        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        BanPlayer banPlayer = this.manager.getBanPlayer(uuid);

        if (banPlayer == null) {
            commandSender.sendMessage("Player not found...");
            return false;
        }

        manager.kickPlayer(banPlayer, commandSender.getName(), message);
        return true;
    }
}
