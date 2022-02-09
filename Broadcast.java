package me.mraxetv.beasthubutilities.utils;

import com.sun.org.apache.xerces.internal.xs.StringList;
import me.mraxetv.beasthubutilities.BeastHubUtilitiesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Broadcast extends BukkitRunnable {
    HashMap<Integer, List<String>> messages = new HashMap<>();
    BeastHubUtilitiesPlugin pl;
    private int numberOfMessages=0;
    private int brojac=1;
    public Broadcast(BeastHubUtilitiesPlugin pl) {
        this.pl=pl;
        if(isBroadcastEnabled()) {
            getMessages();
            runTaskTimer(pl, 20 * 20, 20 * pl.getConfig().getInt("Broadcast.Delay"));

        }
    }

    @Override
    public void run() {
        if(brojac<=numberOfMessages){
            for(String s : messages.get(brojac)){
                s=Utils.setColor(s);
                Bukkit.broadcastMessage(s);
            }
            brojac++;
        }else{

            for(String s : messages.get(1)){
                s=Utils.setColor(s);
                Bukkit.broadcastMessage(s);
            }

            brojac=2;
        }

    }

    private void getMessages(){
        ArrayList<String> message=new ArrayList<>();
        for(String mess : pl.getConfig().getConfigurationSection("Broadcast.Messages").getKeys(false)){
            for(String s : pl.getConfig().getStringList("Broadcast.Messages."+mess)){
                message.add(s);
            }
            numberOfMessages++;
            messages.put(numberOfMessages, (List<String>) message.clone());
            message.clear();
        }
    }
    private boolean isBroadcastEnabled(){ return pl.getConfig().getBoolean("Broadcast.Enabled");}

}
