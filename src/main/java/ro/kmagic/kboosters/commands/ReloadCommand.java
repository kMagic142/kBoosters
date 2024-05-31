package ro.kmagic.kboosters.commands;

import org.bukkit.command.CommandSender;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.util.Utils;

public class ReloadCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("boosters.admin")) return;
        Boosters.getInstance().reload();
        sender.sendMessage(Utils.color(Boosters.getInstance().getConfig().getString("messages.reloaded")));
    }
}
