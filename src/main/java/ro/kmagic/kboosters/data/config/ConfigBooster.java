package ro.kmagic.kboosters.data.config;

import ro.kmagic.kboosters.data.types.boosters.BoosterType;
import ro.kmagic.kboosters.util.Utils;

public class ConfigBooster {

    private final int id;
    private final double multiplier;
    private final BoosterType type;
    private final String server;
    private final String name;


    public ConfigBooster(int id, double multiplier, String type, String server, String name) {
        this.id = id;
        this.multiplier = multiplier;
        this.type = type.equalsIgnoreCase("NETWORK") ? BoosterType.NETWORK : BoosterType.PERSONAL;
        this.server = server;
        this.name = Utils.color(name);
    }

    public int getID() {
        return id;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public BoosterType getType() {
        return type;
    }

    public String getServer() {
        return server;
    }

    public String getName() {
        return name;
    }

}
