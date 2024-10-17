package com.github.niketion.bansystem;

import com.github.niketion.bansystem.commands.*;
import com.github.niketion.bansystem.listeners.ChatListener;
import com.github.niketion.bansystem.listeners.ConnectionListener;
import com.github.niketion.bansystem.manager.BanManager;
import com.github.niketion.bansystem.manager.StorageManager;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BanSystemPlugin extends JavaPlugin {
    @Getter private StorageManager storageManager;
    private BanManager banManager;

    @Override
    public void onEnable() {
        this.banManager = new BanManager(this);

        this.storageManager = new StorageManager(this, this.banManager);
        this.storageManager.initStorage();
        this.storageManager.loadAllBanPlayers();

        this.registerListeners(
                new ChatListener(this, banManager),
                new ConnectionListener(this, banManager)
        );

        getCommand("ban").setExecutor(new BanCommand(banManager));
        getCommand("kick").setExecutor(new KickCommand(banManager));
        getCommand("mute").setExecutor(new MuteCommand(banManager));
        getCommand("tempban").setExecutor(new TempBanCommand(banManager));
        getCommand("tempmute").setExecutor(new TempMuteCommand(banManager));
        getCommand("bansystem").setExecutor(new BanSystemCommand(banManager, storageManager));
        getCommand("unban").setExecutor(new UnBanCommand(banManager, storageManager));
        getCommand("unmute").setExecutor(new UnMuteCommand(banManager, storageManager));
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
}
