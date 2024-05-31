package ro.kmagic.kboosters.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.util.Utils;

public class ResetCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("boosters.admin")) return;
        FileConfiguration config = Boosters.getInstance().getConfig();

        if(args.length < 1) {
            sender.sendMessage(Utils.color(config.getString("messages.reset-usage")));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if(player == null) {
            sender.sendMessage(Utils.color(config.getString("messages.player-not-online")));
            return;
        }

        if(Boosters.getInstance().getPlayerManager().getPlayer(player.getUniqueId()) == null) {
            sender.sendMessage(Utils.color(config.getString("messages.player-not-valid")));
            return;
        }

        for(Booster booster : Boosters.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).getBoosters()) {
            if(booster.isActive()) {
                Boosters.getInstance().getPlayerManager().getPlayer(booster.getOwner()).removeActiveBooster(booster);
            } else {
                Boosters.getInstance().getPlayerManager().getPlayer(booster.getOwner()).removeInactiveBooster(booster);
            }
        }

        Boosters.getInstance().getPlayerManager().refreshActiveBoosters();

        sender.sendMessage(Utils.color(config.getString("messages.player-reset-successfully")));
    }
}
