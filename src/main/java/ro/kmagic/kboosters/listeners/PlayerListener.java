package ro.kmagic.kboosters.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ro.kmagic.kboosters.Boosters;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Boosters.getInstance().getPlayerManager().loadPlayer(event.getPlayer().getUniqueId());
        Boosters.getInstance().getPlayerManager().refreshActiveBoosters();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Boosters.getInstance().getPlayerManager().removePlayer(event.getPlayer().getUniqueId());
        Boosters.getInstance().getPlayerManager().refreshActiveBoosters();
    }

}
