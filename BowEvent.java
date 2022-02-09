package me.mraxetv.beasthubutilities.listeners;

import me.mraxetv.beasthubutilities.BeastHubUtilitiesPlugin;
import me.mraxetv.beasthubutilities.BowTime;
import me.mraxetv.beasthubutilities.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class BowEvent implements Listener{
    private final BeastHubUtilitiesPlugin plugin;

    public BowEvent(BeastHubUtilitiesPlugin plugin) {
        this.plugin = plugin;
    }
    public static HashMap<UUID, BowTime> NotAllowedToUseBow = new HashMap();
    @EventHandler
    public void onFire(ProjectileLaunchEvent e){
        if(e.getEntity() == null) return;
        if(!(e.getEntity() instanceof Arrow)) return;
        if(!(e.getEntity().getShooter() instanceof Player)) return;
        Player p = (Player) e.getEntity().getShooter();
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        p.getInventory().setItem(11, arrow);
    }
    @EventHandler

    public void onPlayerShoot(ProjectileHitEvent e){
        if(e.getEntity() == null) return;
        if(!(e.getEntity().getShooter() instanceof Player)) return;
        if(!(e.getEntity() instanceof Arrow)) return;
        Player p = (Player) e.getEntity().getShooter();
        if(NotAllowedToUseBow.containsKey(p.getUniqueId())){
            p.damage(0);
            e.getEntity().remove();
            String s = plugin.getMessages().getConfig().getString("Messages.Bow.BowDelayWait");
            s = s.replaceAll("%seconds%",String.valueOf(NotAllowedToUseBow.get(p.getUniqueId()).getSec()));

            Utils.sendMessage(p, s);

        }else{
            Location loc=e.getHitBlock().getLocation().add(0,1,0);
            p.teleport(loc);
            p.setHealth(20.0);
            p.damage(0);
            e.getEntity().remove();
            NotAllowedToUseBow.put(p.getUniqueId(),new BowTime(BeastHubUtilitiesPlugin.getInstance().Bow_delay_seconds, p.getUniqueId()));
        }

}


    @EventHandler
    public void onHitPlayer(EntityDamageEvent e){
        if(!(e.getEntity() instanceof  Player)) return;
        Player p=  ((Player) e.getEntity()).getPlayer();
        if(e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
            p.damage(0);
            e.setCancelled(true);
        }
    }

}

