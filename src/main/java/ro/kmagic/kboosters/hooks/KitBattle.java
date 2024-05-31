package ro.kmagic.kboosters.hooks;

import me.wazup.kitbattle.Kitbattle;
import me.wazup.kitbattle.KitbattleAPI;
import me.wazup.kitbattle.PlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.data.types.players.BoostersPlayer;

public class KitBattle implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(EntityDeathEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();

        if(!(damageEvent instanceof EntityDamageByEntityEvent)) return;

        final LivingEntity entity = event.getEntity();

        Player player = entity.getKiller();

        Boosters instance = Boosters.getInstance();
        PlayerData playerdata = KitbattleAPI.getPlayerData(player);
        Kitbattle kb = Kitbattle.getInstance();
        int coins = kb.getConfig().getInt("Earned-Coins-Per-Kill");
        int xp = kb.getConfig().getInt("Minimum-Exp-Per-Kill");

        if (instance.getPlayerManager().getActiveBoosters().size() > 0) {
            Booster booster = instance.getPlayerManager().getActiveBoosters().get(0);
            double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
            xp = (int) (xp * multiplier - 1);
        } else if (instance.getPlayerManager().getPlayer(player.getUniqueId()) != null && instance.getPlayerManager().getPlayer(player.getUniqueId()).getActiveBoosters().size() > 0) {
            BoostersPlayer boosterPlayer = instance.getPlayerManager().getPlayer(player.getUniqueId());
            for (Booster booster : instance.getPlayerManager().getPersonalBoosters(boosterPlayer.getUUID())) {
                if (instance.getConfigBoosterManager().getBooster(booster.getID()).getServer().equalsIgnoreCase(instance.getConfig().getString("server")) && booster.isActive()) {
                    double multiplier = instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier();
                    xp = (int) (xp * multiplier - 1);
                }
            }
        }

        playerdata.addExp(player, xp);
        playerdata.addCoins(player, coins);

    }

}
