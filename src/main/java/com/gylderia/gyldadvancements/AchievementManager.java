package com.gylderia.gyldadvancements;

import com.gylderia.gyldadvancements.Manager.MythicManager;
import eu.endercentral.crazy_advancements.JSONMessage;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import eu.endercentral.crazy_advancements.advancement.criteria.Criteria;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

public class AchievementManager {
    public static AdvancementManager manager;
    private final Gyldadvancements plugin;
    private MythicManager mythicManager;
    public AchievementManager(Gyldadvancements plugin, String namespace, String key) {
        this.plugin = plugin;
        manager = new AdvancementManager(new NameKey(namespace,key));
        manager.makeAccessible();

        if (MythicBukkit.inst() != null) {
            this.mythicManager = new MythicManager(plugin);
            System.out.println("[Gyldadvancements] Successfully linked with MythicMobs API");
        } else {
            System.out.println("[Gyldadvancements] Failed to link with MythicMobs API");
        }
    }
    public void addPlayer(Player p) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> manager.addPlayer(p), 2);
    }

    public void savePlayer(Player p) {
        manager.saveProgress(p);
    }

    public void loadAchievements(Player p) {
        System.out.println("load data pour le joueur " + p);
        manager.loadProgress(p);
    }

    private HashMap<configOptions, Object> getAdvancementData(ConfigurationSection section, Boolean isRoot) {
        System.out.println("isRoot:" + isRoot);
        HashMap<configOptions, Object> data = new HashMap<>();
        for (configOptions option : configOptions.values()) {
            if ((option.rootOnly() && !isRoot) || (isRoot && option.advancementOnly()) || option.name.equals("key")) {
                continue;
            }
            data.put(option, section.get(option.name));
            System.out.println(option.name);
            System.out.println(option.name + "/" + section.get(option.name));
        }
        data.put(configOptions.KEY, section.getName());
        return data;
    }

    private void addAdvancement(Boolean isRoot, HashMap<configOptions, Object> data, File file) {

        Advancement advancement;

        ItemStack icon = new ItemStack(Material.getMaterial(data.get(configOptions.ICON).toString().toUpperCase()));
        JSONMessage title = new JSONMessage(new TextComponent(String.valueOf(data.get(configOptions.NAME))));
        JSONMessage description = new JSONMessage(new TextComponent(String.valueOf(data.get(configOptions.DESCRIPTION))));

        AdvancementDisplay.AdvancementFrame frame = AdvancementDisplay.AdvancementFrame.parse((String) data.get(configOptions.FRAME));
        AdvancementVisibility visibility = AdvancementVisibility.parseVisibility((String) data.get(configOptions.VISIBILITY));
        AdvancementDisplay display = new AdvancementDisplay(icon, title, description, frame, visibility);
        Double x = (Double) data.get(configOptions.X);
        float xf = x.floatValue();
        Double y = (Double) data.get(configOptions.Y);
        float yf = y.floatValue();
        display.setX(xf);
        display.setY(yf);
        AdvancementFlag flags = AdvancementFlag.DISPLAY_MESSAGE;

        if ((Integer) data.get(configOptions.MODEL_DATA) != 0) {
            ItemMeta meta = icon.getItemMeta();
            meta.setCustomModelData((Integer) data.get(configOptions.MODEL_DATA));
            icon.setItemMeta(meta);
        }
        String trigger = (String) data.get(configOptions.TRIGGER);




        if (isRoot) {
            System.out.println("Adding root advancement");
            display.setBackgroundTexture((String) data.get(configOptions.TEXTURE));
            advancement = new Advancement(new NameKey("Gylderia", file.getName() + "." + data.get(configOptions.KEY)), display, flags);

        } else {
            System.out.println("Adding children advancement");
            advancement = new Advancement(manager.getAdvancement(new NameKey("Gylderia", file.getName() + "." + data.get(configOptions.PARENT))), new NameKey("Gylderia", file.getName() + "." + data.get(configOptions.KEY)), display, flags);
        }

        manager.addAdvancement(advancement);

        if (trigger != null) {
            System.out.println("trigger set");
            System.out.println("HERRRRRRE" + Triggers.MYTHIC_MOB_KILL.name());
            if (MythicManager.status && trigger.equalsIgnoreCase(Triggers.MYTHIC_MOB_KILL.name())) {
                System.out.println("mythic mob kill set");
                MythicMob mob;
                Integer number;
                Optional<MythicMob> optionalMob = MythicBukkit.inst().getMobManager().getMythicMob(String.valueOf(data.get(configOptions.MOB)));
                if (optionalMob.isPresent()) {
                    mob = optionalMob.get();
                    System.out.println(mob + "set");
                    number = (Integer) data.get(configOptions.NUMBER);
                    System.out.println("number" + number + "set");
                    mythicManager.registerMobDeathEvent(mob, advancement);
                } else {
                    return;
                }
                //save Listener if it doesn't exist
                //set Criteria
                advancement.setCriteria(new Criteria(number));
            }
        }
    }

    public void loadData(File dir) {
        System.out.println("loaddata");
        File[] rootList = dir.listFiles();
        if (rootList != null) {
            System.out.println("debug 1");
            for (File file : rootList) {
                System.out.println("debug2");
                if (file.isDirectory()) { // alors on veut créer un onglet
                    File configFile = new File(file.getAbsolutePath() + "/config.yml");
                    if (configFile.exists()) {
                        System.out.println("debug3");
                        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(configFile);
                        if (yamlFile.getKeys(false).contains("root")) {
                            for (String key : yamlFile.getKeys(false)) {
                                ConfigurationSection config = yamlFile.getConfigurationSection(key);
                                addAdvancement(key.equals("root"), getAdvancementData(config, key.equals("root")), file);
                            }
                        } else {
                            System.out.println("[Gyldadvancements]Error : Le fichier" + file.getAbsolutePath() + " ne contient pas de section 'root'");
                        }
                    } else {
                        System.out.println("[Gyldadvancements]Nous n'avons pas trouvé de fichier de config pour l'onglet" + file.getName());
                    }
                }
            }
        }
    }


}
