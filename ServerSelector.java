package me.mraxetv.beasthubutilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public class ServerSelector {
    BeastHubUtilitiesPlugin pl;

    public ServerSelector(BeastHubUtilitiesPlugin pl){
        this.pl=pl;
    }

    public boolean isEnabled(){
        return pl.getConfig().getBoolean("Hotbar.ServerSelector.Enabled");
    }
    public boolean isGlow(){
        return pl.getConfig().getBoolean("Hotbar.ServerSelector.Glow");
    }
    public Material getMaterial(){
        return Material.matchMaterial(pl.getConfig().getString("Hotbar.ServerSelector.Item"));
    }
    public int getSlot(){
        return pl.getConfig().getInt("Hotbar.ServerSelector.Slot")-1;
    }
    public String getName(){
        return ChatColor.translateAlternateColorCodes('&',pl.getConfig().getString("Hotbar.ServerSelector.Name"));
    }

    public ArrayList getLore(){
        ArrayList list = new ArrayList();
        for(String s : pl.getConfig().getStringList("Hotbar.ServerSelector.Lore")){
            list.add(ChatColor.translateAlternateColorCodes('&',s));
        }
        return list;
    }
    public int getSize(){
        return pl.getServerSelectorConfig().getConfig().getInt("Size");
    }
    public String getTitle(){
        return ChatColor.translateAlternateColorCodes('&',pl.getServerSelectorConfig().getConfig().getString("Title"));
    }
}
