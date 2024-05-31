package ro.kmagic.kboosters.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.kmagic.kboosters.commands.handler.CommandInterface;
import ro.kmagic.kboosters.menus.MainGUI;

public class BoostersCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("boosters.use")) return;
            new MainGUI(player).getGui().open(player);
        }

    }
}
