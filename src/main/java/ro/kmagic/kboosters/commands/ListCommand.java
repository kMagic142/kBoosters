package ro.kmagic.kboosters.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.data.config.ConfigBooster;
import ro.kmagic.kboosters.util.Utils;

public class ListCommand implements CommandInterface {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("boosters.admin")) return;
        FileConfiguration config = Boosters.getInstance().getConfig();

        if(Boosters.getInstance().getConfigBoosterManager().getBoosters().size() < 1) {
            sender.sendMessage(Utils.color(config.getString("messages.no-booster-types")));
            return;
        }

        for(ConfigBooster booster : Boosters.getInstance().getConfigBoosterManager().getBoosters()) {
            sender.sendMessage(Utils.color(
                    config.getString("messages.booster-list-format"))
                    .replace("{id}", "" + booster.getID())
                    .replace("{multiplier}", "" + booster.getMultiplier())
                    .replace("{name}", booster.getName())
                    .replace("{server}", booster.getServer())
                    .replace("{type}", booster.getType().name())
            );
        }
    }
}
