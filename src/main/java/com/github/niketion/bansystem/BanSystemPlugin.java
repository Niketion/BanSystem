package com.github.niketion.bansystem;

import com.github.niketion.bansystem.commands.*;
import com.github.niketion.bansystem.listeners.ChatListener;
import com.github.niketion.bansystem.listeners.ConnectionListener;
import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.ConfigManager;
import com.github.niketion.bansystem.manager.StorageManager;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BanSystemPlugin extends JavaPlugin {
    private ConfigManager configManager;
    private StorageManager storageManager;
    private BanManager banManager;

    @Override
    public void onEnable() {
        this.banManager = new BanManager(this);

        this.storageManager = new StorageManager(this, this.banManager);
        this.configManager = new ConfigManager(this);
        this.configManager.initConfig();
        this.storageManager.initStorage();
        this.storageManager.loadAllBanPlayers();

        this.registerListeners(
                new ChatListener(this, banManager),
                new ConnectionListener(this, banManager)
        );

        getCommand("ban").setExecutor(new BanCommand(configManager, banManager));
        getCommand("kick").setExecutor(new KickCommand(configManager,banManager));
        getCommand("mute").setExecutor(new MuteCommand(configManager,banManager));
        getCommand("tempban").setExecutor(new TempBanCommand(configManager,banManager));
        getCommand("tempmute").setExecutor(new TempMuteCommand(configManager,banManager));
        getCommand("bansystem").setExecutor(new BanSystemCommand(configManager,banManager, storageManager));
        getCommand("unban").setExecutor(new UnBanCommand(configManager,banManager, storageManager));
        getCommand("unmute").setExecutor(new UnMuteCommand(configManager,banManager, storageManager));
    }

    @Override
    public void onDisable() {
        this.storageManager.closeConnection();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
