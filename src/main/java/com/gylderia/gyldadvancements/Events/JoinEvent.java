package com.gylderia.gyldadvancements.Events;

import com.gylderia.gyldadvancements.AchievementManager;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.EventListener;

public class JoinEvent implements Listener {

    private final AchievementManager manager;

    public JoinEvent(AchievementManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        // add to AdvancementManager
        manager.addPlayer(p);
        manager.loadAchievements(p);
    }
}
