package me.mraxetv.beasthubutilities;

import me.mraxetv.beasthubutilities.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class InfoBook implements Listener {
    BeastHubUtilitiesPlugin pl;

    public InfoBook(BeastHubUtilitiesPlugin pl) {
        this.pl = pl;
    }
    public boolean isEnabled(){
        return pl.getConfig().getBoolean("InfoBook.Enabled");
    }
    public int getSlot(){
        return pl.getConfig().getInt("InfoBook.Slot");
    }
    public String getName(){
        return pl.getConfig().getString("InfoBook.Name");
    }
    public List<String> getLore(){
        List<String> lore = new ArrayList<>();
        for(String s : pl.getConfig().getStringList("InfoBook.Lore")){lore.add(Utils.setColor(s));}
        return lore;
    }
    public int getSize(){
        return pl.getInfoBook().getConfig().getInt("Size");
    }
    public String getTitle(){
        return pl.getInfoBook().getConfig().getString("Title");
    }
}
