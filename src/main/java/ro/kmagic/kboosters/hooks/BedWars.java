package ro.kmagic.kboosters.hooks;

import com.andrei1058.bedwars.api.events.player.PlayerXpGainEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.data.types.players.BoostersPlayer;

public class BedWars implements Listener {

    @EventHandler
    public void onPlayerXPGain(PlayerXpGainEvent event) {
        Boosters instance = Boosters.getInstance();
        com.andrei1058.bedwars.api.BedWars api = Bukkit.getServicesManager().getRegistration(com.andrei1058.bedwars.api.BedWars.class).getProvider();
        PlayerXpGainEvent.XpSource source = event.getXpSource();
        if (instance.getPlayerManager().getActiveBoosters().size() > 0) {
            Booster booster = instance.getPlayerManager().getActiveBoosters().get(0);
            double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
            int xp = event.getAmount();

            if (source == PlayerXpGainEvent.XpSource.GAME_WIN) {
                api.getLevelsUtil().addXp(event.getPlayer(), (int) (xp * multiplier - 1), PlayerXpGainEvent.XpSource.GAME_WIN);
            } else if (source == PlayerXpGainEvent.XpSource.PER_TEAMMATE) {
                api.getLevelsUtil().addXp(event.getPlayer(), (int) (xp * multiplier - 1), PlayerXpGainEvent.XpSource.PER_TEAMMATE);
            }
        } else if(instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId()) != null && instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).getActiveBoosters().size() > 0) {
            BoostersPlayer player = instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            for(Booster booster : instance.getPlayerManager().getPersonalBoosters(player.getUUID())) {
                if(instance.getConfigBoosterManager().getBooster(booster.getID()).getServer().equalsIgnoreCase(instance.getConfig().getString("server")) && booster.isActive()) {
                    double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
                    int xp = event.getAmount();

                    if (source == PlayerXpGainEvent.XpSource.GAME_WIN) {
                        api.getLevelsUtil().addXp(event.getPlayer(), (int) (xp * multiplier - 1), PlayerXpGainEvent.XpSource.GAME_WIN);
                    } else if (source == PlayerXpGainEvent.XpSource.PER_TEAMMATE) {
                        api.getLevelsUtil().addXp(event.getPlayer(), (int) (xp * multiplier - 1), PlayerXpGainEvent.XpSource.PER_TEAMMATE);
                    }
                }
            }
        }
    }

}
