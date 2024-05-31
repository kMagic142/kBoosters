package ro.kmagic.kboosters.hooks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import plugily.projects.thebridge.api.StatsStorage;
import plugily.projects.thebridge.api.events.game.TBGameStateChangeEvent;
import plugily.projects.thebridge.api.events.game.TBGameStopEvent;
import plugily.projects.thebridge.arena.ArenaState;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.data.types.players.BoostersPlayer;

public class TheBridge implements Listener {

    @EventHandler
    public void onStatChange(TBGameStopEvent event) {
        if (event.getArena().getArenaState() != ArenaState.ENDING || event.getArena().getWinner() == null) return;
        for (Player player : event.getArena().getWinner().getPlayers()) {
            Boosters instance = Boosters.getInstance();
            int xp = 0;
            if (instance.getPlayerManager().getActiveBoosters().size() > 0) {
                Booster booster = instance.getPlayerManager().getActiveBoosters().get(0);
                double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
                xp = (int) (xp * multiplier - 1);
                StatsStorage.setUserStat(player, StatsStorage.StatisticType.XP, StatsStorage.getUserStats(player, StatsStorage.StatisticType.XP) + xp);
            } else if (instance.getPlayerManager().getPlayer(player.getUniqueId()) != null && instance.getPlayerManager().getPlayer(player.getUniqueId()).getActiveBoosters().size() > 0) {
                BoostersPlayer boosterPlayer = instance.getPlayerManager().getPlayer(player.getUniqueId());
                for (Booster booster : instance.getPlayerManager().getPersonalBoosters(boosterPlayer.getUUID())) {
                    if (instance.getConfigBoosterManager().getBooster(booster.getID()).getServer().equalsIgnoreCase(instance.getConfig().getString("server")) && booster.isActive()) {
                        double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
                        xp = (int) (xp * multiplier - 1);
                        StatsStorage.setUserStat(player, StatsStorage.StatisticType.XP, StatsStorage.getUserStats(player, StatsStorage.StatisticType.XP) + xp);
                    }
                }
            }
        }
    }

}
