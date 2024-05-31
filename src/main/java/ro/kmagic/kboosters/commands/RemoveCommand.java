package ro.kmagic.kboosters.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.data.config.ConfigBooster;
import ro.kmagic.kboosters.util.Utils;

public class RemoveCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("boosters.admin")) return;
        FileConfiguration config = Boosters.getInstance().getConfig();

        if(args.length < 2) {
            sender.sendMessage(Utils.color(config.getString("messages.remove-usage")));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if(player == null) {
            sender.sendMessage(Utils.color(config.getString("messages.player-not-online")));
            return;
        }

        ConfigBooster boosterType;

        try {
            boosterType = Boosters.getInstance().getConfigBoosterManager().getBooster(Integer.parseInt(args[1]));
        } catch(NumberFormatException e) {
            sender.sendMessage(Utils.color(config.getString("messages.booster-invalid")));
            return;
        }

        if(boosterType == null) {
            sender.sendMessage(Utils.color(config.getString("messages.booster-invalid")));
            return;
        }

        if(Boosters.getInstance().getPlayerManager().getPlayer(player.getUniqueId()) == null) {
            sender.sendMessage(Utils.color(config.getString("messages.player-not-valid")));
            return;
        }

        try {
            Boosters.getInstance().getPlayerManager().removeBooster(player.getUniqueId(), boosterType.getID());
            sender.sendMessage(Utils.color(config.getString("messages.booster-removed-successfully")));
        } catch(Exception e) {
            sender.sendMessage(Utils.color(config.getString("messages.booster-not-found")));
        }


    }
}
