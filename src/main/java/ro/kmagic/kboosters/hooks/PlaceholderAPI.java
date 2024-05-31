package ro.kmagic.kboosters.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.config.ConfigBooster;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.data.types.boosters.BoosterType;
import ro.kmagic.kboosters.util.Utils;

import java.util.List;

public class PlaceholderAPI extends PlaceholderExpansion {

    private Boosters instance;

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    @Override
    public boolean register() {
        if (!canRegister()) return false;

        instance = (Boosters) Bukkit.getPluginManager().getPlugin(getRequiredPlugin());
        if (instance == null) {
            return false;
        }

        return super.register();
    }

    @Override
    public String getRequiredPlugin() {
        return "kBoosters";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "boosters";
    }

    @Override
    public @NotNull String getAuthor() {
        return instance.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return null;

        String[] args = identifier.split("_");

        if(args.length < 1) return null;

        if(args[0].equals("status")) {
            List<Booster> result = instance.getMySQL().getBoostersForServer(args[1]);

            if(result.size() > 0) {
                for (Booster booster : result) {
                    if (!booster.isActive()) continue;
                    ConfigBooster configBooster = instance.getConfigBoosterManager().getBooster(booster.getID());
                    BoosterType type = configBooster.getType();
                    OfflinePlayer owner = Bukkit.getOfflinePlayer(booster.getOwner());

                    if(owner == null) {
                        return instance.getConfig().getString("placeholderapi.inactive-booster");
                    }

                    if(booster.getDuration() < System.currentTimeMillis() * 1000L) {
                        return instance.getConfig().getString("placeholderapi.inactive-booster");
                    }

                    StringBuilder builder = new StringBuilder();

                    if (type == BoosterType.NETWORK) {
                        for (String str : instance.getConfig().getStringList("placeholderapi.active-booster")) {
                            builder.append(Utils.color(str)
                                    .replace("%multiplier%", "" + configBooster.getMultiplier())
                                    .replace("%player%", owner.getName())).append("\n");
                        }

                        return builder.toString();
                    }
                }
            }

            return instance.getConfig().getString("placeholderapi.inactive-booster");
        }

        if(args[0].equals("active")) {
            List<Booster> result = instance.getMySQL().getBoostersForServer(args[1]);

            return String.valueOf(result.stream().anyMatch(Booster::isActive));
        }

        return null;
    }

}
