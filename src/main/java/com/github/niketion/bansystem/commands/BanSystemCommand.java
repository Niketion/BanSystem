package com.github.niketion.bansystem.commands;

import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.ConfigManager;
import com.github.niketion.bansystem.manager.StorageManager;
import com.github.niketion.bansystem.model.BanPlayer;
import com.github.niketion.bansystem.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class BanSystemCommand implements CommandExecutor {
    private BanManager manager;
    private ConfigManager configManager;
    private StorageManager storageManager;

    public BanSystemCommand(ConfigManager configManager, BanManager manager, StorageManager storageManager) {
        this.manager = manager;
        this.storageManager = storageManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length < 2) {
            commandSender.sendMessage("/bansystem history <player>");
            return false;
        }

        if (!strings[0].equalsIgnoreCase("history")) {
            return false;
        }

        String playerName = strings[1];
        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        BanPlayer banPlayer = this.manager.getBanPlayer(uuid);

        if (banPlayer == null || banPlayer.getPunishments().isEmpty()) {
            commandSender.sendMessage(ConfigManager.Value.PLAYER_NOT_FOUND.toString());
            return false;
        }

        banPlayer.getPunishments().forEach((punishment) -> {
            commandSender.sendMessage(String.format("* (ID %d) Staffer: %s, Type: %s, fromDate: %s, toDate: %s, permanent: %s, message: %s",
                    punishment.getId(),  // Assuming getId() returns an int
                    punishment.getStaffer(), // Correctly included staffer
                    punishment.getType().name(),
                    TimeUtils.formatToStartOfDay(punishment.getFromDate()),
                    TimeUtils.formatToStartOfDay(punishment.getToDate()),
                    punishment.isPermanent(),
                    punishment.getMessage()
            ));
        });
        return true;
    }
}
