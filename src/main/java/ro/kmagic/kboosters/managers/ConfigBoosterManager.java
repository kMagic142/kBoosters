package ro.kmagic.kboosters.managers;

import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.config.ConfigBooster;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ConfigBoosterManager {

    private final List<ConfigBooster> configBoosters;
    private final Boosters instance;

    public ConfigBoosterManager() {
        this.configBoosters = new LinkedList<>();
        this.instance = Boosters.getInstance();
        loadBoosters();
    }

    public List<ConfigBooster> getBoosters() {
        return configBoosters;
    }

    public ConfigBooster getBooster(int id) {
        Optional<ConfigBooster> booster = configBoosters.stream().filter(b -> b.getID() == id).findFirst();
        return booster.orElse(null);
    }

    public void addBooster(ConfigBooster booster) {
        configBoosters.add(booster);
        instance.getMySQL().addBoosterType(booster);
    }

    public void deleteBooster(int id) {
        instance.getMySQL().removeBoosterType(getBooster(id));
        configBoosters.remove(getBooster(id));
    }

    public void loadBoosters() {
        configBoosters.addAll(instance.getMySQL().getBoosterTypes());
    }


}
