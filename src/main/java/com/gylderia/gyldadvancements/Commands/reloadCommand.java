package com.gylderia.gyldadvancements.Commands;

import com.gylderia.gyldadvancements.AchievementManager;
import com.gylderia.gyldadvancements.Gyldadvancements;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import io.lumine.mythic.bukkit.utils.Players;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class reloadCommand implements CommandExecutor {

    private final AchievementManager achievementManager;
    public reloadCommand(AchievementManager achievementManager) {
        this.achievementManager = achievementManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ArrayList<Player> players = AchievementManager.manager.getPlayers();
        for (Player player : players) {
            achievementManager.savePlayer(player);
        }
        for (Advancement advancement : AchievementManager.manager.getAdvancements()) {
            AchievementManager.manager.removeAdvancement(advancement);
        }
        achievementManager.loadData(Gyldadvancements.configDir);
        for (Player player : players) {
            achievementManager.loadAchievements(player);
        }
        for (Advancement advancement : AchievementManager.manager.getAdvancements()) {
            AchievementManager.manager.updateAdvancement(advancement);
        }
        return false;
    }
}
