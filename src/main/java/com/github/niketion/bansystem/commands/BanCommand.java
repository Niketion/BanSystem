package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.BanSystemPlugin;
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

public class BanCommand implements CommandExecutor {
    private BanManager manager;
    private BanSystemPlugin plugin;

    public BanCommand(BanSystemPlugin plugin, BanManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("ban.player")) {
            return false;
        }

        if (strings.length < 2) {
            commandSender.sendMessage("/ban <player> <message>");
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

        manager.createPunishment(banPlayer, commandSender.getName(), Punishment.Type.BAN, message, true);
        return true;
    }
}
