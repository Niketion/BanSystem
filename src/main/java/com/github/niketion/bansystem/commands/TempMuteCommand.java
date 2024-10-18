package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.ConfigManager;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.model.Punishment;
import com.github.niketion.bansystem.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class TempMuteCommand implements CommandExecutor {
    private BanManager manager;
    private ConfigManager configManager;

    public TempMuteCommand(ConfigManager configManager, BanManager manager) {
        this.manager = manager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("tempmute.player")) {
            return false;
        }

        if (strings.length < 3) {
            commandSender.sendMessage(ConfigManager.Value.TEMPMUTE_USAGE.toString());
            return false;
        }

        String playerName = strings[0];

        long duration;
        try {
            duration = TimeUtils.parseDuration(strings[1]);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(ConfigManager.Value.FORMATTING_ERROR.toString());
            return false;
        }

        String message = String.join(" ", Arrays.copyOfRange(strings, 2, strings.length));

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        UUID uuid = offlinePlayer.getUniqueId();
        BanPlayer banPlayer = this.manager.getBanPlayer(uuid);

        if (banPlayer == null) {
            commandSender.sendMessage(ConfigManager.Value.PLAYER_NOT_FOUND.toString());
            return false;
        }

        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player != null) player.sendMessage(ConfigManager.Value.VICTIM_MUTED.formatted(commandSender.getName(), message));
        }

        commandSender.sendMessage(ConfigManager.Value.PLAYER_MUTED.formatted(playerName, strings[0]));
        manager.createPunishment(banPlayer, commandSender.getName(), Punishment.Type.MUTE, message, duration);
        return true;
    }
}
