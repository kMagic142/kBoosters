package ro.kmagic.kboosters.commands.handler;

import org.bukkit.command.CommandSender;

public interface CommandInterface {

    void onCommand(CommandSender sender, String[] args);

}
