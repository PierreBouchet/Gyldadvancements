package com.gylderia.gyldadvancements;

import com.gylderia.gyldadvancements.Commands.reloadCommand;
import com.gylderia.gyldadvancements.Events.JoinEvent;
import com.gylderia.gyldadvancements.Events.QuitEvent;
import com.gylderia.gyldadvancements.WorldGuard.AdvancementFlag;
import com.gylderia.gyldadvancements.WorldGuard.Handler.AdvancementFlagHandler;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.SessionManager;
import eu.endercentral.crazy_advancements.CrazyAdvancementsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Gyldadvancements extends JavaPlugin  {

    AchievementManager manager;
    public static File configDir;

    @Override
    public void onLoad() {
        super.onLoad();
        AdvancementFlag advancementFlag = new AdvancementFlag();
        advancementFlag.registerFlag();
    }

    @Override
    public void onEnable() {


        if (CrazyAdvancementsAPI.getInstance() != null) {
            System.out.println("[Gyldadvancements] Successfully linked with CrazyAdvancementsAPI");
        }  else {
            System.out.println("[Gyldadvancements] Error when trying to link with CrazyAdvancementsAPI");
            return;
        }

        System.out.println("[Gyldadvancements]The plugin has been activated");
        this.manager = new AchievementManager(this, "Gylderia", "manager");

        getCommand("advancementreload").setExecutor(new reloadCommand(manager));

        Bukkit.getPluginManager().registerEvents(new JoinEvent(manager), this);
        Bukkit.getPluginManager().registerEvents(new QuitEvent(manager), this);


        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        configDir = new File(getDataFolder().getAbsolutePath());
        manager.loadData(configDir);

        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(AdvancementFlagHandler.FACTORY(), null); // only set our field if there was no error

    }

    @Override
    public void onDisable() {
        //save for online players
        for (Player p : Bukkit.getOnlinePlayers()) {
            AchievementManager.manager.saveProgress(p);
        }

    }
}
