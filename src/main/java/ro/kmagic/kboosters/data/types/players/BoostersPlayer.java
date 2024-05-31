package ro.kmagic.kboosters.data.types.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.types.boosters.Booster;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BoostersPlayer {

    private final UUID uuid;
    private final List<Booster> activeBoosters;
    private final List<Booster> inactiveBoosters;
    private final List<Booster> affectedBy = new LinkedList<>();

    public BoostersPlayer(UUID player, List<Booster> active, List<Booster> inactive) {
        this.uuid = player;
        this.activeBoosters = active;
        this.inactiveBoosters = inactive;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public List<Booster> getActiveBoosters() {
        return activeBoosters;
    }

    public List<Booster> getInactiveBoosters() {
        return inactiveBoosters;
    }

    public List<Booster> getAffectedBy() {
        return affectedBy;
    }

    public List<Booster> getBoosters() {
        LinkedList<Booster> merged = new LinkedList<>(activeBoosters);
        merged.addAll(inactiveBoosters);

        return merged;
    }

    public void removeInactiveBooster(Booster booster) {
        inactiveBoosters.remove(booster);
        Boosters.getInstance().getMySQL().removeBooster(uuid, booster.getID(), booster.getDuration(), false, Boosters.getInstance().getConfigBoosterManager().getBooster(booster.getID()).getServer());
    }

    public void removeActiveBooster(Booster booster) {
        activeBoosters.remove(booster);
        Boosters.getInstance().getMySQL().removeBooster(uuid, booster.getID(), booster.getDuration(), true, Boosters.getInstance().getConfigBoosterManager().getBooster(booster.getID()).getServer());
    }

    public void setBoosterActive(Booster booster) {
        inactiveBoosters.remove(booster);
        activeBoosters.add(booster);
        booster.setActive(true);

        Boosters.getInstance().getMySQL().setActive(booster);
    }

    public void setBoosterInactive(Booster booster) {
        inactiveBoosters.add(booster);
        activeBoosters.remove(booster);
        booster.setActive(false);

        Boosters.getInstance().getMySQL().setInactive(booster);
    }

    public void removeAffectedBy(Booster booster) {
        affectedBy.remove(booster);
    }

    public void addAffectedBy(Booster booster) {
        affectedBy.add(booster);
    }

}
