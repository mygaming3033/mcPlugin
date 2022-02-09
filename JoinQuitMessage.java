package me.mraxetv.beasthubutilities.listeners;

import me.mraxetv.beasthubutilities.BeastHubUtilitiesPlugin;
import me.mraxetv.beasthubutilities.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class JoinQuitMessage implements Listener {
    private final BeastHubUtilitiesPlugin pl;
    public JoinQuitMessage(BeastHubUtilitiesPlugin pl) {

        this.pl = pl;
        pl.getServer().getPluginManager().registerEvents(this,pl);
    }
    @EventHandler (priority = EventPriority.HIGHEST)
    public void OnJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        if(isJoinQuitEnabled()) {
            sendJoinMessage(p);
            e.setJoinMessage(null);
        }
        if(isTitleEnabled()){
            sendTitle(p);
        }
        if(isWelcomeMessageEnabled()) sendWelcomeMessage(p);
    }

    @EventHandler(priority = EventPriority.HIGHEST)

    public void OnQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(isJoinQuitEnabled()) {
            sendQuitMessage(p);
            e.setQuitMessage(null);
        }
    }

    public void sendTitle(Player p){
        String message1 = pl.getConfig().getString("JoinTitle.Title");
        String message2 = pl.getConfig().getString("JoinTitle.SubTitle");
       pl.getUtils().sendTitleSubtitleMessage(p,message1,message2);
    }


    public void sendWelcomeMessage(Player p){
        for(String s : pl.getConfig().getStringList("WelcomeMessage.Message")){
            Utils.sendMessage(p,s);
        }
    }
    public void sendJoinMessage(Player p) {
        String message = pl.getConfig().getString("JoinQuitMessage.JoinMessage");
        message = Utils.setPlaceholders(p, message);
        Bukkit.broadcastMessage(message);
    }
    public void sendQuitMessage(Player p){
        String message = pl.getConfig().getString("JoinQuitMessage.QuitMessage");
        message = Utils.setPlaceholders(p,message);
        Bukkit.broadcastMessage(message);
    }




    private boolean isTitleEnabled(){ return pl.getConfig().getBoolean("JoinTitle.Enabled");}
    private boolean isWelcomeMessageEnabled(){ return pl.getConfig().getBoolean("JoinTitle.Enabled");}
    private boolean isJoinQuitEnabled(){ return pl.getConfig().getBoolean("JoinQuitMessage.Enabled");}
}
