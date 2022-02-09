package me.mraxetv.beasthubutilities;

import me.mraxetv.beasthubutilities.listeners.BowEvent;
import me.mraxetv.beasthubutilities.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BowTime extends BukkitRunnable {
    public BowTime(int sec, UUID uuid) {
        this.sec = sec;
        this.uuid=uuid;
        runTaskTimer(BeastHubUtilitiesPlugin.getInstance(),20,20);
    }

    int sec;
    UUID uuid;

    @Override
    public void run() {
        sec--;
        if (sec<=0){
            BowEvent.NotAllowedToUseBow.remove(uuid);
            Player p = Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid)));
            String mess =ChatColor.translateAlternateColorCodes('&', BeastHubUtilitiesPlugin.getInstance().getMessages().getConfig().getString("Messages.Bow.DelayEnd"));
            Utils.sendMessage(p, mess);
            cancel();
            return;
        }

        // NotAllowedToUseBow.replace(p,sec);
    }

    public int getSec() {
        return sec;
    }
}
