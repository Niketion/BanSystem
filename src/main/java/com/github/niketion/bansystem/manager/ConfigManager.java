package com.github.niketion.bansystem.manager;

import com.github.niketion.bansystem.BanSystemPlugin;
import org.bukkit.ChatColor;

public class ConfigManager {
    private static BanSystemPlugin plugin;

    public ConfigManager(BanSystemPlugin plugin) {
        ConfigManager.plugin = plugin;
    }

    public void initConfig() {
        plugin.saveDefaultConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }

    public enum Value {
        BAN_USAGE("usages.ban"),
        TEMPBAN_USAGE("usages.tempban"),
        TEMPMUTE_USAGE("usages.tempmute"),
        MUTE_USAGE("usages.mute"),
        UNBAN_USAGE("usages.unban"),
        UNMUTE_USAGE("usages.unmute"),
        PLAYER_NOT_FOUND("player-not-found"),
        FORMATTING_ERROR("formatting-error"),
        KICK_USAGE("usages.kick"),
        PLAYER_UNMUTED("player-unmuted"),
        PLAYER_UNBANNED("player-unbanned"),
        PLAYER_NOT_BANNED("player-not-banned"),
        PLAYER_NOT_MUTED("player-not-muted"),
        PLAYER_BANNED("actions.player-banned"),
        PLAYER_MUTED("actions.player-muted"),
        PLAYER_KICKED("actions.player-kicked"),
        VICTIM_MUTED("actions.victim-muted");

        private String path;
        Value(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(this.path));
        }

        public String formatted(Object... args) {
            String message = plugin.getConfig().getString(this.path);
            message = ChatColor.translateAlternateColorCodes('&', message);
            return String.format(message, args);
        }
    }
}
