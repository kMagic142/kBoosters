package ro.kmagic.kboosters.hooks;

import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameWinEvent;
import io.github.Leonardo0013YT.UltraSkyWars.game.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.data.types.players.BoostersPlayer;

public class UltraSkyWars implements Listener {

    @EventHandler
    public void onGameWin(USWGameWinEvent event) {
        Boosters instance = Boosters.getInstance();
        int xp = io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars.get().getCm().getXpWin();
        int coins = io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars.get().getCm().getCoinsWin();
        int souls = io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars.get().getCm().getSoulsWin();

        for(Player player : event.getWinner().getMembers()) {
            GamePlayer gamePlayer = event.getGame().getGamePlayer().get(player.getUniqueId());
            if (instance.getPlayerManager().getActiveBoosters().size() > 0) {
                Booster booster = instance.getPlayerManager().getActiveBoosters().get(0);
                double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
                xp = (int) (xp * multiplier - 1);
            } else if (instance.getPlayerManager().getPlayer(player.getUniqueId()) != null && instance.getPlayerManager().getPlayer(player.getUniqueId()).getActiveBoosters().size() > 0) {
                BoostersPlayer boostersPlayer = instance.getPlayerManager().getPlayer(player.getUniqueId());
                for (Booster booster : instance.getPlayerManager().getPersonalBoosters(boostersPlayer.getUUID())) {
                    if (instance.getConfigBoosterManager().getBooster(booster.getID()).getServer().equalsIgnoreCase(instance.getConfig().getString("server")) && booster.isActive()) {
                        double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
                        xp = (int) (xp * multiplier - 1);
                    }
                }
            }

            gamePlayer.addCoins(coins);
            gamePlayer.addXP(xp);
            gamePlayer.addSouls(souls);
        }
    }

}
