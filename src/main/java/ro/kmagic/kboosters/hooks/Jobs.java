package ro.kmagic.kboosters.hooks;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.data.types.players.BoostersPlayer;

public class Jobs implements Listener {

    @EventHandler
    public void onJobsExpGain(JobsExpGainEvent event) {
        Boosters instance = Boosters.getInstance();
        double xp = event.getExp();
        if (instance.getPlayerManager().getActiveBoosters().size() > 0) {
            Booster booster = instance.getPlayerManager().getActiveBoosters().get(0);
            double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
            xp = xp * multiplier - 1;
            event.setExp(xp);
        } else if (instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId()) != null && instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).getActiveBoosters().size() > 0) {
            BoostersPlayer player = instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            for (Booster booster : instance.getPlayerManager().getPersonalBoosters(player.getUUID())) {
                if (instance.getConfigBoosterManager().getBooster(booster.getID()).getServer().equalsIgnoreCase(instance.getConfig().getString("server")) && booster.isActive()) {
                    double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
                    xp = xp * multiplier - 1;
                    event.setExp(xp);
                }
            }
        }
    }

    @EventHandler
    public void onJobsPrePayment(JobsPrePaymentEvent event) {
        Boosters instance = Boosters.getInstance();
        double points = event.getAmount();
        if (instance.getPlayerManager().getActiveBoosters().size() > 0) {
            Booster booster = instance.getPlayerManager().getActiveBoosters().get(0);
            double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
            points = points * multiplier - 1;
            event.setAmount(points);
        } else if (instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId()) != null && instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).getActiveBoosters().size() > 0) {
            BoostersPlayer player = instance.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            for (Booster booster : instance.getPlayerManager().getPersonalBoosters(player.getUUID())) {
                if (instance.getConfigBoosterManager().getBooster(booster.getID()).getServer().equalsIgnoreCase(instance.getConfig().getString("server")) && booster.isActive()) {
                    double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
                    points = points * multiplier - 1;
                    event.setAmount(points);
                }
            }
        }
    }

}
