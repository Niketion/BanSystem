package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.ConfigManager;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.model.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class MuteCommand implements CommandExecutor {
    private BanManager manager;
    private ConfigManager configManager;

    public MuteCommand(ConfigManager configManager, BanManager manager) {
        this.manager = manager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("mute.player")) {
            return false;
        }

        if (strings.length < 2) {
            commandSender.sendMessage(ConfigManager.Value.MUTE_USAGE.toString());
            return false;
        }

        String playerName = strings[0];
        String message = String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        BanPlayer banPlayer = this.manager.getBanPlayer(uuid);

        if (banPlayer == null) {
            commandSender.sendMessage(ConfigManager.Value.PLAYER_NOT_FOUND.toString());
            return false;
        }

        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player != null) player.sendMessage(ConfigManager.Value.VICTIM_MUTED.formatted(commandSender.getName(), message));
        }

        commandSender.sendMessage(ConfigManager.Value.PLAYER_MUTED.formatted(playerName, "permanently"));
        manager.createPunishment(banPlayer, commandSender.getName(), Punishment.Type.MUTE, message, true);
        return true;
    }
}
