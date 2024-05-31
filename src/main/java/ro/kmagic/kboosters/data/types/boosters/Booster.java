package ro.kmagic.kboosters.data.types.boosters;

import org.bukkit.Bukkit;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.util.Utils;

import java.util.UUID;

public class Booster {

    private final UUID owner;
    private final int id;
    private final long duration;
    private boolean active;

    public Booster(UUID owner, int id, long duration, boolean active) {
        this.owner = owner;
        this.id = id;
        this.duration = duration;
        this.active = setActive(active);
    }

    public int getID() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isActive() {
        return active;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean setActive(boolean active) {
        if(Boosters.getInstance().getPlayerManager().getPlayer(owner) == null) {
            this.active = active;
            return active;
        }

        if(active && Boosters.getInstance().getPlayerManager().getPlayer(owner).getAffectedBy().stream().anyMatch(b -> Boosters.getInstance().getConfigBoosterManager().getBooster(b.getID()).getType() == BoosterType.NETWORK && Bukkit.getPlayer(b.getOwner()) != null)) {
            this.active = false;
            Bukkit.getPlayer(owner).sendMessage(Utils.color(Boosters.getInstance().getConfig().getString("messages.network-booster-already-active")));
        } else {
            this.active = active;
        }

        if(!active || !this.active) {
            Boosters.getInstance().getPlayerManager().removeAffectedPlayers(this);
        }

        return this.active;
    }

}
