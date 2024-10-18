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
        PLAYER_BANNED(""),
        PLAYER_MUTED(""),
        PLAYER_KICKED(""),
        VICTIM_MUTED("");

        private String path;
        Value(String path) {
            this.path = path;
        }
        // TODO: REPLACES
        @Override
        public String toString() {
            return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(this.path));
        }
    }
}
