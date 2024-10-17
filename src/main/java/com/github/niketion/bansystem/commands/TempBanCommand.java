package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.model.Punishment;
import com.github.niketion.bansystem.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class TempBanCommand implements CommandExecutor {
    private BanManager manager;

    public TempBanCommand(BanManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("tempban.player")) {
            return false;
        }

        if (strings.length < 3) {
            commandSender.sendMessage("/tempban <player> <duration><s/m/h/d> <message>");
            return false;
        }

        String playerName = strings[0];

        long duration;
        try {
            duration = TimeUtils.parseDuration(strings[1]);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage("Error in formatting.");
            return false;
        }

        String message = String.join(" ", Arrays.copyOfRange(strings, 2, strings.length));

        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        BanPlayer banPlayer = this.manager.getBanPlayer(uuid);

        if (banPlayer == null) {
            commandSender.sendMessage("Player not found...");
            return false;
        }

        manager.createPunishment(banPlayer, commandSender.getName(), Punishment.Type.BAN, message, duration);
        return true;
    }
}
