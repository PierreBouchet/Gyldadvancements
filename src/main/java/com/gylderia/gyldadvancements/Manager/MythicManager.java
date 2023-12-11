package com.gylderia.gyldadvancements.Manager;

import com.gylderia.gyldadvancements.Events.MythicMobDeathEvent;
import com.gylderia.gyldadvancements.Events.QuitEvent;
import com.gylderia.gyldadvancements.Gyldadvancements;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;

public class MythicManager {
    public static boolean status;
    private Gyldadvancements plugin;

    public MythicManager(Gyldadvancements plugin) {
        status = true;
        this.plugin = plugin;
    }
    public void registerMobDeathEvent(MythicMob mob, Advancement advancement) {
        Bukkit.getPluginManager().registerEvents(new MythicMobDeathEvent(mob, advancement), plugin);
    }
}
