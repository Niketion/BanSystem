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
        KICK_USAGE("usages.kick");

        private String path;
        Value(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(this.path));
        }
    }
}
