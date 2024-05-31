package ro.kmagic.kboosters.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.mysql.MySQL;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.data.types.boosters.BoosterType;
import ro.kmagic.kboosters.data.types.players.BoostersPlayer;
import ro.kmagic.kboosters.util.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private final HashMap<UUID, BoostersPlayer> playerCache;
    private final List<Booster> activeBoosters;

    private final MySQL database;
    private final Boosters instance;

    public PlayerManager() {
        playerCache = new HashMap<>();
        activeBoosters = new LinkedList<>();

        instance = Boosters.getInstance();
        database = instance.getMySQL();
    }

    public BoostersPlayer getPlayer(UUID player) {
        return playerCache.get(player);
    }

    public void addPlayer(UUID player, List<Booster> active, List<Booster> inactive) {
        playerCache.putIfAbsent(player, new BoostersPlayer(player, active, inactive));
    }

    public void removePlayer(UUID player) {
        playerCache.remove(player);
    }

    public void loadPlayer(UUID player) {
        if(database != null) {
            List<Booster> activeBoosters = instance.getMySQL().getActiveBoosters(player);
            List<Booster> inactiveBoosters = instance.getMySQL().getInactiveBoosters(player);

            addPlayer(player, activeBoosters, inactiveBoosters);
            refreshActiveBoosters();
        }
    }

    public List<Booster> getNetworkBoosters(UUID player) {
        BoostersPlayer boosterPlayer = getPlayer(player);
        List<Booster> network = new LinkedList<>();

        for(Booster booster : boosterPlayer.getInactiveBoosters()) {
            if(instance.getConfigBoosterManager().getBooster(booster.getID()) != null && instance.getConfigBoosterManager().getBooster(booster.getID()).getType() == BoosterType.NETWORK)
                network.add(booster);
        }

        for(Booster booster : boosterPlayer.getActiveBoosters()) {
            if(instance.getConfigBoosterManager().getBooster(booster.getID()) != null && instance.getConfigBoosterManager().getBooster(booster.getID()).getType() == BoosterType.NETWORK)
                network.add(booster);
        }

        return network;
    }

    public List<Booster> getPersonalBoosters(UUID player) {
        BoostersPlayer boosterPlayer = getPlayer(player);
        List<Booster> personal = new LinkedList<>();

        for(Booster booster : boosterPlayer.getInactiveBoosters()) {
            if(instance.getConfigBoosterManager().getBooster(booster.getID()) != null && instance.getConfigBoosterManager().getBooster(booster.getID()).getType() == BoosterType.PERSONAL)
                personal.add(booster);
        }

        for(Booster booster : boosterPlayer.getActiveBoosters()) {
            if(instance.getConfigBoosterManager().getBooster(booster.getID()) != null && instance.getConfigBoosterManager().getBooster(booster.getID()).getType() == BoosterType.PERSONAL)
                personal.add(booster);
        }

        return personal;
    }

    public void removeBooster(UUID player, int id) {
        getPlayer(player).getBoosters().forEach(booster -> {
            if(booster.getID() == id) {
                getPlayer(player).removeActiveBooster(booster);
                getPlayer(player).removeInactiveBooster(booster);
            }
        });
        refreshActiveBoosters();
    }

    public void refreshActiveBoosters() {
        for(BoostersPlayer player : playerCache.values()) {
            player.getActiveBoosters().forEach(booster -> {
                if(instance.getConfigBoosterManager().getBooster(booster.getID()).getType() == BoosterType.NETWORK && instance.getConfigBoosterManager().getBooster(booster.getID()).getServer().equalsIgnoreCase(instance.getConfig().getString("server"))) {
                    activeBoosters.add(booster);
                    player.addAffectedBy(booster);
                }
            });
        }
    }

    public void removeAffectedPlayers(Booster booster) {
        for(BoostersPlayer player : playerCache.values()) {
            player.removeAffectedBy(booster);
        }
        refreshActiveBoosters();
    }

    public BukkitTask checkIfAnyBoosterExpired() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                long unixTime = System.currentTimeMillis() / 1000L;

                for(Booster booster : activeBoosters) {
                    if(booster.getDuration() < unixTime) {
                        Player player = Bukkit.getPlayer(booster.getOwner());

                        if(player != null) {
                            player.sendMessage(Utils.color(instance.getConfig().getString("messages.booster-expired")));
                            activeBoosters.remove(booster);
                            BoostersPlayer boosterPlayer = getPlayer(player.getUniqueId());
                            boosterPlayer.removeActiveBooster(booster);
                            refreshActiveBoosters();
                        } else {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(booster.getOwner());
                            activeBoosters.remove(booster);
                            BoostersPlayer boosterPlayer = getPlayer(offlinePlayer.getUniqueId());
                            boosterPlayer.removeActiveBooster(booster);
                            refreshActiveBoosters();
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(instance, 20L * 20, 20L * 20);
    }

    public List<Booster> getActiveBoosters() {
        return activeBoosters;
    }

}
