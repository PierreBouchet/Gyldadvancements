package com.gylderia.gyldadvancements.WorldGuard.Handler;

import com.gylderia.gyldadvancements.AchievementManager;
import com.gylderia.gyldadvancements.WorldGuard.AdvancementFlag;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class AdvancementFlagHandler extends FlagValueChangeHandler<String> {

    public static Factory FACTORY() {
        return new Factory();
    }

    public static class Factory extends Handler.Factory<AdvancementFlagHandler> {
        public Factory() {

        }
        @Override
        public AdvancementFlagHandler create(Session session) {
            // create an instance of a handler for the particular session
            // if you need to pass certain variables based on, for example, the player
            // whose session this is, do it here
            return new AdvancementFlagHandler(session);
        }
    }
    // construct with your desired flag to track changes
    public AdvancementFlagHandler(Session session) {
        super(session, AdvancementFlag.advancementFlag);
    }
    // ... override handler methods here

    @Override
    protected void onInitialValue(LocalPlayer localPlayer, ApplicableRegionSet applicableRegionSet, String s) {}

    @Override
    protected boolean onSetValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, String s, String t1, MoveType moveType) {
        //ici je gere le code
        System.out.println("étape 1");
        for (ProtectedRegion region : applicableRegionSet) {
            String advancementRegion = region.getFlag(AdvancementFlag.advancementFlag);
            if (advancementRegion != null) {
                System.out.println("étape2");
                AdvancementManager manager = AchievementManager.manager;
                Advancement advancement = manager.getAdvancement(new NameKey("Gylderia", advancementRegion));
                if (advancement != null) {
                    System.out.println("étape3");
                    if (!advancement.isGranted(localPlayer.getUniqueId())) {
                        System.out.println("'étape4'");
                        manager.grantAdvancement(localPlayer.getUniqueId(), advancement);
                        manager.saveProgress(localPlayer.getUniqueId());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        for (ProtectedRegion region : toSet) {
            String advancementRegion = region.getFlag(AdvancementFlag.advancementFlag);
            if (advancementRegion != null) {
                AdvancementManager manager = AchievementManager.manager;
                Advancement advancement = manager.getAdvancement(new NameKey("Gylderia", advancementRegion));
                if (advancement != null) {
                    if (!advancement.isGranted(player.getUniqueId())) {
                        manager.grantAdvancement(player.getUniqueId(), advancement);
                        manager.saveProgress(player.getUniqueId());
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, String s, MoveType moveType) {
        return false;
    }

}
