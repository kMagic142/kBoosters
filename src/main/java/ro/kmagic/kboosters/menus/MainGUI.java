package ro.kmagic.kboosters.menus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.util.Utils;

import java.util.List;

public class MainGUI {

    private final Gui gui;

    public MainGUI(Player player) {
        Boosters instance = Boosters.getInstance();
        ConfigurationSection menuSection = instance.getConfig().getConfigurationSection("main-menu");

        gui = Gui.gui()
                .title(Component.text(Utils.color(menuSection.getString("title"))))
                .rows(menuSection.getInt("rows"))
                .disableAllInteractions()
                .create();


        String name = menuSection.getConfigurationSection("personal").getString("name");
        List<String> lore = menuSection.getConfigurationSection("personal").getStringList("lore");
        int slot = menuSection.getConfigurationSection("personal").getInt("slot") - 1;
        int id = menuSection.getConfigurationSection("personal").getInt("id");

        GuiItem personal = ItemBuilder.from(new MaterialData(id).getItemType())
                .name(Component.text(Utils.color(name)))
                .lore(Utils.formatComponentList((lore)))
                .asGuiItem(event -> {
                    if(instance.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId()) == null || instance.getPlayerManager().getPersonalBoosters(event.getWhoClicked().getUniqueId()).size() < 1) {
                        event.getWhoClicked().sendMessage(Utils.color(instance.getConfig().getString("messages.no-boosters")));
                        gui.close(event.getWhoClicked());
                        return;
                    }

                    new PersonalBoostersGUI(player).getGui().open(player);
                });

        gui.setItem(slot, personal);

        String name2 = menuSection.getConfigurationSection("network").getString("name");
        List<String> lore2 = menuSection.getConfigurationSection("network").getStringList("lore");
        int slot2 = menuSection.getConfigurationSection("network").getInt("slot") - 1;
        int id2 = menuSection.getConfigurationSection("network").getInt("id");

        GuiItem network = ItemBuilder.from(new MaterialData(id2).getItemType())
                .name(Component.text(Utils.color(name2)))
                .lore(Utils.formatComponentList(lore2))
                .asGuiItem(event -> {
                    if(instance.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId()) == null || instance.getPlayerManager().getNetworkBoosters(event.getWhoClicked().getUniqueId()).size() < 1) {
                        event.getWhoClicked().sendMessage(Utils.color(instance.getConfig().getString("messages.no-boosters")));
                        gui.close(event.getWhoClicked());
                        return;
                    }

                    new NetworkBoostersGUI(player).getGui().open(player);
                });

        gui.setItem(slot2, network);

        if(!menuSection.getConfigurationSection("back").getBoolean("enabled")) return;

        String backname = menuSection.getConfigurationSection("back").getString("name");
        int backslot = menuSection.getConfigurationSection("back").getInt("slot") - 1;
        int backid = menuSection.getConfigurationSection("back").getInt("id");

        GuiItem backitem = ItemBuilder.from(new MaterialData(backid).getItemType())
                .name(Component.text(Utils.color(backname)))
                .asGuiItem(event -> gui.close(event.getWhoClicked()));

        gui.setItem(backslot, backitem);

    }

    public Gui getGui() {
        return gui;
    }

}
