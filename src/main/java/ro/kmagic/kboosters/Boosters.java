package ro.kmagic.kboosters;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ro.kmagic.kboosters.commands.*;
import ro.kmagic.kboosters.commands.handler.CommandHandler;
import ro.kmagic.kboosters.data.mysql.MySQL;
import ro.kmagic.kboosters.hooks.*;
import ro.kmagic.kboosters.listeners.PlayerListener;
import ro.kmagic.kboosters.managers.ConfigBoosterManager;
import ro.kmagic.kboosters.managers.PlayerManager;
import ro.kmagic.kboosters.util.Utils;

import java.util.logging.Level;

public final class Boosters extends JavaPlugin {

    private static Boosters instance;
    private MySQL mySQL;

    private PlayerManager playerManager;
    private ConfigBoosterManager configBoosterManager;
    private BukkitTask boosterChecker;

    @Override
    public void onEnable() {
        instance = this;

        Utils.log(Level.INFO, "Boosters enabled successfully.");

        loadData();
        loadHooks();
        loadCommands();
    }

    @Override
    public void onDisable() {
        if(boosterChecker != null) boosterChecker.cancel();
        if(mySQL != null) mySQL.getConnectionPoolManager().closePool();
        getLogger().info("Boosters disabled successfully.");
    }

    private void loadData() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        mySQL = new MySQL();

        playerManager = new PlayerManager();
        boosterChecker = playerManager.checkIfAnyBoosterExpired();

        configBoosterManager = new ConfigBoosterManager();
    }

    private void loadHooks() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI().register();
            Utils.log(Level.INFO, "Boosters hooked successfully into PlaceholderAPI.");
        }

        if(Bukkit.getPluginManager().getPlugin("BedWars1058") != null) {
            Bukkit.getPluginManager().registerEvents(new ro.kmagic.kboosters.hooks.BedWars(), this);
            Utils.log(Level.INFO, "Boosters hooked successfully into BedWars1058.");
        }

        if(Bukkit.getPluginManager().getPlugin("Jobs") != null) {
            Bukkit.getPluginManager().registerEvents(new Jobs(), this);
            Utils.log(Level.INFO, "Boosters hooked successfully into Jobs.");
        }


        if(Bukkit.getPluginManager().getPlugin("TheBridge") != null) {
            Bukkit.getPluginManager().registerEvents(new TheBridge(), this);
            Utils.log(Level.INFO, "Boosters hooked successfully into TheBridge.");
        }

        if(Bukkit.getPluginManager().getPlugin("UltraSkyWars") != null) {
            Bukkit.getPluginManager().registerEvents(new UltraSkyWars(), this);
            Utils.log(Level.INFO, "Boosters hooked successfully into UltraSkyWars.");
        }

        if(Bukkit.getPluginManager().getPlugin("KitBattle") != null) {
            Bukkit.getPluginManager().registerEvents(new KitBattle(), this);
            Utils.log(Level.INFO, "Boosters hooked successfully into KitBattle.");
        }
    }

    private void loadCommands() {
        CommandHandler commandHandler = new CommandHandler();

        commandHandler.register("boosters", new BoostersCommand());
        commandHandler.register("give", new GiveCommand());
        commandHandler.register("remove", new RemoveCommand());
        commandHandler.register("reset", new ResetCommand());
        commandHandler.register("help", new HelpCommand());
        commandHandler.register("reload", new ReloadCommand());
        commandHandler.register("create", new CreateCommand());
        commandHandler.register("delete", new DeleteCommand());
        commandHandler.register("list", new ListCommand());
        getCommand("boosters").setExecutor(commandHandler);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void reload() {
        reloadConfig();
    }

    public static Boosters getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ConfigBoosterManager getConfigBoosterManager() {
        return configBoosterManager;
    }
}
