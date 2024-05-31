package ro.kmagic.kboosters.commands;

import org.bukkit.command.CommandSender;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.util.Utils;

public class HelpCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("boosters.use")) return;
        sender.sendMessage(Utils.colorList(Boosters.getInstance().getConfig().getStringList("messages.help-message")).toArray(new String[0]));
    }
}
