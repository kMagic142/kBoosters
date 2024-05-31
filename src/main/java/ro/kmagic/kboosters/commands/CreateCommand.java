package ro.kmagic.kboosters.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.data.config.ConfigBooster;
import ro.kmagic.kboosters.data.types.boosters.BoosterType;
import ro.kmagic.kboosters.util.Utils;

import java.util.Arrays;

public class CreateCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("boosters.admin")) return;
        FileConfiguration config = Boosters.getInstance().getConfig();

        if(args.length < 4) {
            sender.sendMessage(Utils.color(config.getString("messages.create-usage")));
            return;
        }

        int id = Integer.parseInt(args[0]);
        double multiplier = Integer.parseInt(args[1]);

        BoosterType boosterType = null;

        if(args[2].equalsIgnoreCase("PERSONAL"))
            boosterType = BoosterType.PERSONAL;
        if(args[2].equalsIgnoreCase("NETWORK"))
            boosterType = BoosterType.NETWORK;

        if(boosterType == null) {
            sender.sendMessage(Utils.color(config.getString("messages.booster-type-invalid")));
            return;
        }

        String server = args[3];
        String name = String.join(" ", Arrays.copyOfRange(args, 4, args.length));

        Boosters.getInstance().getConfigBoosterManager().addBooster(new ConfigBooster(id, multiplier, boosterType.name(), server, name));

        sender.sendMessage(Utils.color(config.getString("messages.booster-created-successfully")));

    }
}
