package com.gylderia.gyldadvancements.Events;

import com.gylderia.gyldadvancements.AchievementManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {
    private final AchievementManager manager;

    public QuitEvent(AchievementManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        manager.savePlayer(p);
    }
}
