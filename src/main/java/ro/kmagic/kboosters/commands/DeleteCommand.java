package ro.kmagic.kboosters.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.data.config.ConfigBooster;
import ro.kmagic.kboosters.data.types.boosters.BoosterType;
import ro.kmagic.kboosters.util.Utils;

import java.util.Arrays;

public class DeleteCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("boosters.admin")) return;
        FileConfiguration config = Boosters.getInstance().getConfig();

        if(args.length < 1) {
            sender.sendMessage(Utils.color(config.getString("messages.delete-usage")));
            return;
        }

        int id = Integer.parseInt(args[0]);

        if(Boosters.getInstance().getConfigBoosterManager().getBooster(id) == null) {
            sender.sendMessage(Utils.color(config.getString("messages.booster-invalid")));
            return;
        }

        Boosters.getInstance().getConfigBoosterManager().deleteBooster(id);

        sender.sendMessage(Utils.color(config.getString("messages.booster-created-successfully")));

    }
}
