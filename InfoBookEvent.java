package me.mraxetv.beasthubutilities.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.mraxetv.beasthubutilities.BeastHubUtilitiesPlugin;
import me.mraxetv.beasthubutilities.InfoBook;
import me.mraxetv.beasthubutilities.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoBookEvent implements Listener {
    BeastHubUtilitiesPlugin pl;
    int slot;
    int amount;
    boolean autofill;
    Material m;
    String name;
    List<String> lore;
    ArrayList<Integer> takenSlots = new ArrayList<Integer>();
    boolean glow;

    Material autofill_m;
    String autofill_name;
    List<String> autofill_lore;
    boolean autofill_glow;
    int autofill_amount;

    public InfoBookEvent(BeastHubUtilitiesPlugin pl) {
        this.pl = pl;
    }
    @EventHandler
    public void OnBookClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Inventory inv;
        if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
        if(p.getInventory().getItemInMainHand().getType() != Material.BOOK) return;
        if(!p.hasPermission("beasthubutilities.infobook.use")) {
            Utils.sendMessage(p,"%prefix% &cYou do not have permission to use InfoBook");
            return;
        }
        inv = Bukkit.createInventory(null, pl.getInfoBookClass().getSize(),pl.getInfoBookClass().getTitle());
        initializeInv(inv);
        p.openInventory(inv);
    }

    public void initializeInv(Inventory inv) {



        for(String item : pl.getInfoBook().getConfig().getConfigurationSection("Items").getKeys(false)){
            for(String s : pl.getInfoBook().getConfig().getConfigurationSection("Items."+item).getKeys(false)){
                slot = pl.getInfoBook().getConfig().getInt("Items."+item+".Slot")-1;
                if(slot == -1){
                    autofill=true;
                    autofill_m = Material.matchMaterial(pl.getInfoBook().getConfig().getString("Items." + item + ".Item"));
                    autofill_amount = pl.getInfoBook().getConfig().getInt("Items." + item + ".Amount");
                    autofill_name = Utils.setColor(pl.getInfoBook().getConfig().getString("Items." + item + ".Name"));
                    autofill_glow = pl.getInfoBook().getConfig().getBoolean("Items." + item + ".Glow");
                    autofill_lore = new ArrayList<>();
                    for (String l : pl.getInfoBook().getConfig().getStringList("Items." + item + ".Lore")) {

                        autofill_lore.add(Utils.setColor(l));

                    }
                    continue;
                }else {
                    m = Material.matchMaterial(pl.getInfoBook().getConfig().getString("Items." + item + ".Item"));
                    amount = pl.getInfoBook().getConfig().getInt("Items." + item + ".Amount");
                    name = Utils.setColor(pl.getInfoBook().getConfig().getString("Items." + item + ".Name"));
                    glow = pl.getInfoBook().getConfig().getBoolean("Items." + item + ".Glow");
                    lore = new ArrayList<>();
                    for (String l : pl.getInfoBook().getConfig().getStringList("Items." + item + ".Lore")) {

                        lore.add(Utils.setColor(l));

                    }
                }

                inv.setItem(slot,createGuiItem(m, name,lore,amount,glow));
                takenSlots.add(slot);
            }
            if(autofill){
                for(int i = 1; i<pl.getInfoBookClass().getSize();i++){
                    if(!takenSlots.contains(i)){
                        inv.setItem(i,createGuiItem(autofill_m, autofill_name,autofill_lore,autofill_amount,autofill_glow));
                    }
                }
            }

        }}

    protected ItemStack createGuiItem(final Material material, final String name, List<String> lore, final int amount, boolean glow) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        if(glow){
            meta.addEnchant(Enchantment.DURABILITY,1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.CHEST) return;
        if (!e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(Utils.setColor(pl.getInfoBookClass().getTitle())))return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(e.getViewers().get(0).getInventory())) {
            e.setCancelled(true);
        }
    }
}
