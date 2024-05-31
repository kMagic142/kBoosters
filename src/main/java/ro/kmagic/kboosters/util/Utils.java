package ro.kmagic.kboosters.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ro.kmagic.kboosters.Boosters;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Utils {

    public static void log(Level level, String msg) {
        Bukkit.getLogger().log(level, "[Boosters] " + msg);
    }

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static List<Component> formatComponentList(List<String> list) {
        List<Component> complist = new ArrayList<>();
        for(String str : list) {
            complist.add(Component.text(Utils.color(str)));
        }

        return complist;
    }

    public static List<String> colorList(List<String> list) {
        List<String> formatted = new ArrayList<>();
        for(String str : list) {
            formatted.add(color(str));
        }

        return formatted;
    }

    public static int parseShortTime(String timestr) {
        timestr = timestr.toLowerCase();
        int multiplier = 1;

        if(!timestr.matches("\\d[dwhms]?"))
            return -1;

        switch(timestr.charAt(timestr.length()-1)) {
            case 'w':
                multiplier *= 7;
            case 'd':
                multiplier *= 24;
            case 'h':
                multiplier *= 60;
            case 'm':
                multiplier *= 60;
            case 's':
                timestr = timestr.substring(0, timestr.length()-1);
                break;
            default:
                multiplier *= 60 * 60 * 24;
        }

        return multiplier * Integer.parseInt(timestr);
    }

    public static String secondsToTime(long timeseconds) {
        StringBuilder builder = new StringBuilder();
        int years = (int)(timeseconds / (60*60*24*365));
        ConfigurationSection config = Boosters.getInstance().getConfig().getConfigurationSection("time-format");

        if(years > 10) {
            return "Forever";
        }

        if(years>0){
            builder.append(years).append(" ").append(pluralise(years, config.getString("year"), config.getString("years"))).append(", ");
            timeseconds = timeseconds % (60*60*24*365);
        }

        int weeks = (int)(timeseconds / (60*60*24*7));
        if(weeks>0){
            builder.append(weeks).append(" ").append(pluralise(weeks, config.getString("week"), config.getString("weeks"))).append(", ");
            timeseconds = timeseconds % (60*60*24*7);
        }

        int days = (int)(timeseconds / (60*60*24));
        if(days>0){
            builder.append(days).append(" ").append(pluralise(days, config.getString("day"), config.getString("days"))).append(", ");
            timeseconds = timeseconds % (60*60*24);
        }

        int hours = (int)(timeseconds / (60*60));
        if(hours>0){
            builder.append(hours).append(" ").append(pluralise(hours, config.getString("hour"), config.getString("hours"))).append(", ");
            timeseconds = timeseconds % (60*60);
        }

        int minutes = (int)(timeseconds / (60));
        if(minutes>0){
            builder.append(minutes).append(" ").append(pluralise(minutes, config.getString("minute"), config.getString("minutes"))).append(", ");
            timeseconds = timeseconds % (60);
        }

        if(timeseconds>0) {
            builder.append(timeseconds).append(" ").append(pluralise(timeseconds, config.getString("second"), config.getString("seconds")));
        }

        String str = builder.toString();

        if(str.endsWith(", "))
            str = str.substring(0,str.length()-2);
        if(str.equals(""))
            str="Error";

        return str;
    }

    public static String pluralise(long x, String singular, String plural) {
        return x == 1 ? singular : plural;
    }
}
