package ro.kmagic.kboosters.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.data.config.ConfigBooster;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.util.Utils;

public class GiveCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("boosters.admin")) return;
        FileConfiguration config = Boosters.getInstance().getConfig();

        if(args.length < 3) {
            sender.sendMessage(Utils.color(config.getString("messages.give-usage")));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if(player == null) {
            sender.sendMessage(Utils.color(config.getString("messages.player-not-online")));
            return;
        }

        int time;

        if(args[1].equalsIgnoreCase("unlimited")) {
            time = 999999;
        } else {
            time = Utils.parseShortTime(args[1]);
        }

        if(time < 60) {
            sender.sendMessage(Utils.color(config.getString("messages.duration-too-short")));
            return;
        }

        long unixTime = System.currentTimeMillis() / 1000L;
        long duration = unixTime + time;

        ConfigBooster boosterType;

        try {
            boosterType = Boosters.getInstance().getConfigBoosterManager().getBooster(Integer.parseInt(args[2]));
        } catch(NumberFormatException e) {
            sender.sendMessage(Utils.color(config.getString("messages.booster-invalid")));
            return;
        }

        if(boosterType == null) {
            sender.sendMessage(Utils.color(config.getString("messages.booster-invalid")));
            return;
        }

        Booster booster = new Booster(player.getUniqueId(), boosterType.getID(), duration, false);

        Boosters.getInstance().getPlayerManager()
                .getPlayer(player.getUniqueId())
                .setBoosterInactive(booster);


        sender.sendMessage(Utils.color(config.getString("messages.booster-added-successfully")));


    }
}
