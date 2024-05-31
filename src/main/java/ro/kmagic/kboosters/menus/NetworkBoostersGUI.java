package ro.kmagic.kboosters.menus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import ro.kmagic.kboosters.Boosters;
import ro.kmagic.kboosters.data.types.boosters.Booster;
import ro.kmagic.kboosters.data.types.boosters.BoosterType;
import ro.kmagic.kboosters.util.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkBoostersGUI {

    private final PaginatedGui gui;

    public NetworkBoostersGUI(Player player) {
        Boosters instance = Boosters.getInstance();
        ConfigurationSection menuSection = instance.getConfig().getConfigurationSection("network-boosters-menu");

        gui = Gui.paginated()
                .title(Component.text(Utils.color(menuSection.getString("title"))))
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        String name = menuSection.getString("items-name");
        List<String> lore = menuSection.getStringList("items-lore");
        int id = menuSection.getInt("items-id");
        boolean arrowEnabled = menuSection.getConfigurationSection("back").getBoolean("enabled");

        if(arrowEnabled) {
            String backname = menuSection.getConfigurationSection("back").getString("name");
            List<String> backlore = menuSection.getConfigurationSection("back").getStringList("lore");
            int backid = menuSection.getConfigurationSection("back").getInt("id");


            GuiItem item = ItemBuilder.from(new MaterialData(backid).getItemType())
                    .name(Component.text(Utils.color(backname)))
                    .lore(Utils.formatComponentList((backlore)))
                    .asGuiItem(event -> {
                        new MainGUI(player).getGui().open(event.getWhoClicked());
                    });
            gui.setItem(6, 5, item);
        }

        for(Booster booster : instance.getPlayerManager().getNetworkBoosters(player.getUniqueId()).stream().sorted((a, b) -> {
            if(b.isActive() == a.isActive()) {
                return 0;
            } else if(b.isActive()) {
                return 1;
            }

            return -1;
        }).collect(Collectors.toList())) {
            if(booster.isActive()) {
                String baname = menuSection.getString("booster-active-name")
                        .replace("{name}", instance.getConfigBoosterManager().getBooster(booster.getID()).getName())
                        .replace("{multiplier}", "" + instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier());

                List<String> balore = new LinkedList<>();

                for(String str : menuSection.getStringList("booster-active-lore")) {
                    balore.add(str.replace("{duration}", Utils.secondsToTime(((System.currentTimeMillis() / 1000L) - booster.getDuration()))));
                }

                int baid = menuSection.getInt("booster-active-id");

                GuiItem item = ItemBuilder.from(new MaterialData(baid).getItemType())
                        .name(Component.text(Utils.color(baname)))
                        .lore(Utils.formatComponentList((balore)))
                        .asGuiItem();

                gui.addItem(item);
                continue;
            }

            GuiItem item = ItemBuilder.from(new MaterialData(id).getItemType())
                    .name(Component.text(Utils.color(name.replace("{name}", instance.getConfigBoosterManager().getBooster(booster.getID()).getName())
                            .replace("{multiplier}", "" + instance.getConfigBoosterManager().getBooster(booster.getID()).getMultiplier()))))
                    .lore(Utils.formatComponentList(lore))
                    .asGuiItem(event -> {
                        getGui().close(event.getWhoClicked());
                        if(instance.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId()).getAffectedBy().stream().anyMatch(b -> instance.getConfigBoosterManager().getBooster(b.getID()).getType() == BoosterType.NETWORK)
                                && instance.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId()).getActiveBoosters().stream().anyMatch(b -> instance.getConfigBoosterManager().getBooster(b.getID()).getType() == BoosterType.NETWORK
                                && instance.getConfigBoosterManager().getBooster(b.getID()).getServer().equalsIgnoreCase(instance.getConfig().getString("server")))) {
                            event.getWhoClicked().sendMessage(Utils.color(instance.getConfig().getString("messages.network-booster-already-active")));
                            gui.close(event.getWhoClicked());
                            return;
                        }

                        instance.getPlayerManager().getPlayer(booster.getOwner()).setBoosterActive(booster);
                        instance.getPlayerManager().refreshActiveBoosters();
                        Bukkit.getPlayer(booster.getOwner()).sendMessage(Utils.color(instance.getConfig().getString("messages.booster-activated")));
                        gui.close(event.getWhoClicked());
                    });

            gui.addItem(item);
        }

        String previousname = menuSection.getConfigurationSection("previous").getString("name");
        int previousid = menuSection.getConfigurationSection("previous").getInt("id");

        GuiItem previous = ItemBuilder.from(new MaterialData(previousid).getItemType())
                .name(Component.text(Utils.color(previousname)))
                .asGuiItem(event -> gui.previous());

        String nextname = menuSection.getConfigurationSection("next").getString("name");
        int nextid = menuSection.getConfigurationSection("next").getInt("id");

        GuiItem next = ItemBuilder.from(new MaterialData(nextid).getItemType())
                .name(Component.text(Utils.color(nextname)))
                .asGuiItem(event -> gui.next());


        gui.setItem(6, 3, previous);
        gui.setItem(6, 7, next);
        gui.getFiller().fillBottom(ItemBuilder.from(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 15).toItemStack(1)).asGuiItem());
    }

    public PaginatedGui getGui() {
        return gui;
    }

}
