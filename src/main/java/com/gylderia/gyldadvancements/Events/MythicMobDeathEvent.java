package com.gylderia.gyldadvancements.Events;

import com.gylderia.gyldadvancements.AchievementManager;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import io.lumine.mythic.api.mobs.MythicMob;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;


public class MythicMobDeathEvent implements Listener {
    MythicMob mob;
    Advancement advancement;

    AdvancementManager manager = AchievementManager.manager;
    public static HashMap<MythicMob, Advancement> activesListeners;

    public MythicMobDeathEvent(MythicMob mob, Advancement advancement) {
        this.mob = mob;
        this.advancement = advancement;
    }
    @EventHandler
    public void onMythicMobDeath(io.lumine.mythic.bukkit.events.MythicMobDeathEvent e) {
        System.out.println("death of mob");
        if (e.getKiller().getType() == EntityType.PLAYER) {
            Player p = (Player) e.getKiller();
            System.out.println(p);
            MythicMob mob = e.getMobType();
            System.out.println(mob);
            System.out.println(this.mob);
            if (mob == this.mob) {
                System.out.println("good mob");
                int progress = manager.getCriteriaProgress(p, advancement);
                manager.setCriteriaProgress(p, advancement, progress + 1);
                manager.saveProgress(p, advancement);
            }
        }
    }

}
