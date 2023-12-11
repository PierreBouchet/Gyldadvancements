package com.gylderia.gyldadvancements;

import com.gylderia.gyldadvancements.Events.JoinEvent;
import com.gylderia.gyldadvancements.Events.QuitEvent;
import eu.endercentral.crazy_advancements.CrazyAdvancementsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Gyldadvancements extends JavaPlugin  {

    AchievementManager manager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        if (CrazyAdvancementsAPI.getInstance() != null) {
            System.out.println("[Gyldadvancements] Successfully linked with CrazyAdvancementsAPI");
        }  else {
            System.out.println("[Gyldadvancements] Error when trying to link with CrazyAdvancementsAPI");
        }

        System.out.println("[Gyldadvancements]The plugin has been activated");
        this.manager = new AchievementManager(this, "Gylderia", "manager");

        Bukkit.getPluginManager().registerEvents(new JoinEvent(manager), this);
        Bukkit.getPluginManager().registerEvents(new QuitEvent(manager), this);


        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configDir = new File(getDataFolder().getAbsolutePath());
        manager.loadData(configDir);



        /*ItemMeta meta = icon.getItemMeta();
        meta.setCustomModelData(1);
        icon.setItemMeta(meta);

        JSONMessage title = new JSONMessage(new TextComponent("test"));
        JSONMessage description = new JSONMessage(new TextComponent("This is my description"));

        AdvancementDisplay.AdvancementFrame frame = AdvancementDisplay.AdvancementFrame.TASK;
        AdvancementVisibility visibility = AdvancementVisibility.ALWAYS;
        AdvancementDisplay display = new AdvancementDisplay(icon, title, description, frame, visibility);
        display.setBackgroundTexture("textures/block/jungle_planks.png");
        display.setX(1);
        display.setY(1.5F);
        Advancement root = new Advancement(new NameKey("gylderia", "root"), display, AdvancementFlag.SHOW_TOAST);

        ItemStack icon1 = new ItemStack(Material.OAK_DOOR);
        JSONMessage title1 = new JSONMessage(new TextComponent("my first advancement"));
        JSONMessage description1 = new JSONMessage(new TextComponent("This is my first description"));

        AdvancementDisplay.AdvancementFrame frame1 = AdvancementDisplay.AdvancementFrame.TASK;
        AdvancementVisibility visibility1 = AdvancementVisibility.ALWAYS;
        AdvancementDisplay display1 = new AdvancementDisplay(icon1, title1, description1, frame1, visibility1);
        display.setBackgroundTexture("textures/block/jungle_planks.png");
        display.setX(2);
        display.setY(1.5F);

        Advancement firstAdvancement = new Advancement(root, new NameKey("gylderia", "firstadvancement"), display1, AdvancementFlag.TOAST_AND_MESSAGE);
        manager.manager.addAdvancement(root);
        manager.manager.addAdvancement(firstAdvancement);*/
    }

    @Override
    public void onDisable() {
        //save for online players
        for (Player p : Bukkit.getOnlinePlayers()) {
            AchievementManager.manager.saveProgress(p);
        }

    }
}
