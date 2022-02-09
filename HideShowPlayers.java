package me.mraxetv.beasthubutilities.listeners;

import me.mraxetv.beasthubutilities.BeastHubUtilitiesPlugin;
import me.mraxetv.beasthubutilities.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HideShowPlayers implements Listener {
    private final BeastHubUtilitiesPlugin pl;
    ItemStack hidden;
    ItemStack notHidden;
    public HideShowPlayers(BeastHubUtilitiesPlugin pl) {
        this.pl = pl;
        hidden= new ItemStack(Material.matchMaterial(pl.getConfig().getString("Hotbar.HidePlayers.Hidden.Item")),1);
        ItemMeta metaHidden=hidden.getItemMeta();
        metaHidden.setDisplayName(ChatColor.translateAlternateColorCodes('&',pl.getConfig().getString("Hotbar.HidePlayers.Hidden.Name")));
        hidden.setItemMeta(metaHidden);
        notHidden= new ItemStack(Material.matchMaterial(pl.getConfig().getString("Hotbar.HidePlayers.NotHidden.Item")),1);
        ItemMeta metaNotHidden=notHidden.getItemMeta();
        metaNotHidden.setDisplayName(ChatColor.translateAlternateColorCodes('&',pl.getConfig().getString("Hotbar.HidePlayers.NotHidden.Name")));
        notHidden.setItemMeta(metaNotHidden);
    }

    @EventHandler
    public void HideShowPlayers(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        //if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) || !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        if (!p.hasPermission("beasthubutilities.hide")) {
            Utils.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "%prefix% &cYou do not have permission to hide Players"));
            return;
        }
        ItemStack item = p.getInventory().getItemInMainHand();

        if(item == null) return;
        if (item.getType() == notHidden.getType() && notHidden.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
            setNewItem(p);
            pl.getBhuManager().getPlayer(p.getUniqueId()).setInvisibility(true);
        } else if (item.getType() == hidden.getType() && hidden.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {

            setOldItem(p);
            pl.getBhuManager().getPlayer(p.getUniqueId()).setInvisibility(false);
        }


        e.setCancelled(true);
    }

    public void setNewItem(Player p) {
        ItemStack pearl = new ItemStack(Material.matchMaterial(pl.getConfig().getString("Hotbar.HidePlayers.Hidden.Item")), 1);
        ItemMeta pearlm = pearl.getItemMeta();
        if (pl.getConfig().getBoolean("Hotbar.HidePlayers.Hidden.Glow")) {
            pearlm.addEnchant(Enchantment.DURABILITY, 1, true);
            pearlm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        pearlm.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Hotbar.HidePlayers.Hidden.Name")));
        List<String> lore = new ArrayList<>();
        for (String s : pl.getConfig().getStringList("Hotbar.HidePlayers.Hidden.Lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        pearlm.setLore(lore);
        pearl.setItemMeta(pearlm);

        p.getInventory().setItem(pl.getConfig().getInt("Hotbar.HidePlayers.Hidden.Slot") - 1, pearl);
    }


    public void setOldItem(Player p) {
        ItemStack eyeOfEnder = new ItemStack(Material.matchMaterial(pl.getConfig().getString("Hotbar.HidePlayers.NotHidden.Item")), 1);
        ItemMeta eyeOfEnderm = eyeOfEnder.getItemMeta();
        if (pl.getConfig().getBoolean("Hotbar.HidePlayers.NotHidden.Glow")) {
            eyeOfEnderm.addEnchant(Enchantment.DURABILITY, 1, true);
            eyeOfEnderm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        eyeOfEnderm.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Hotbar.HidePlayers.NotHidden.Name")));
        List<String> lore = new ArrayList<>();
        for (String s : pl.getConfig().getStringList("Hotbar.HidePlayers.NotHidden.Lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        eyeOfEnderm.setLore(lore);
        eyeOfEnder.setItemMeta(eyeOfEnderm);

        p.getInventory().setItem(pl.getConfig().getInt("Hotbar.HidePlayers.NotHidden.Slot") - 1, eyeOfEnder);
    }
}
