package me.mraxetv.beasthubutilities.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.mraxetv.beasthubutilities.BeastHubUtilitiesPlugin;
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

public class OnPlayerInteractEvent implements Listener {
    private final BeastHubUtilitiesPlugin pl;

    int slot;
    int amount;
    boolean autofill;
    Material m;
    String name;
    String srw;
    List<String> lore;
    ArrayList<Integer> takenSlots = new ArrayList<Integer>();
    boolean glow;
    HashMap<String, String> serverCommands = new HashMap<String, String>();

    Material autofill_m;
    String autofill_name;
    List<String> autofill_lore;
    boolean autofill_glow;
    int autofill_amount;
    public OnPlayerInteractEvent(BeastHubUtilitiesPlugin pl) {

        this.pl = pl;
    }
    @EventHandler
    public void OnPlayerInteractEvent(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Inventory inv;
        if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
        if(p.getInventory().getItemInMainHand().getType() != pl.getServerSelector().getMaterial()) return;
        if(!p.hasPermission("beasthubutilities.serverselector.use")) {
            Utils.sendMessage(p,ChatColor.translateAlternateColorCodes('&',"%prefix% &cYou do not have permission to use ServerSelector"));
            return;
        }
         inv = Bukkit.createInventory(null, pl.getServerSelector().getSize(),pl.getServerSelector().getTitle()); // B)
        initializeInv(inv);
        p.openInventory(inv);
    }

    public void initializeInv(Inventory inv) {



        for(String server : pl.getServerSelectorConfig().getConfig().getConfigurationSection("Servers").getKeys(false)){
            for(String s : pl.getServerSelectorConfig().getConfig().getConfigurationSection("Servers."+server).getKeys(false)){
                slot = pl.getServerSelectorConfig().getConfig().getInt("Servers."+server+".Slot")-1;
                if(slot == -1){
                    autofill=true;
                    autofill_m = Material.matchMaterial(pl.getServerSelectorConfig().getConfig().getString("Servers." + server + ".Item"));
                    autofill_amount = pl.getServerSelectorConfig().getConfig().getInt("Servers." + server + ".Amount");
                    autofill_name = ChatColor.translateAlternateColorCodes('&', pl.getServerSelectorConfig().getConfig().getString("Servers." + server + ".Name"));
                    autofill_glow = pl.getServerSelectorConfig().getConfig().getBoolean("Servers." + server + ".Glow");
                    autofill_lore = new ArrayList<>();
                    for (String l : pl.getServerSelectorConfig().getConfig().getStringList("Servers." + server + ".Lore")) {

                        autofill_lore.add(ChatColor.translateAlternateColorCodes('&', l));

                    }
                    continue;
                }else {
                    m = Material.matchMaterial(pl.getServerSelectorConfig().getConfig().getString("Servers." + server + ".Item"));
                    amount = pl.getServerSelectorConfig().getConfig().getInt("Servers." + server + ".Amount");
                    name = ChatColor.translateAlternateColorCodes('&', pl.getServerSelectorConfig().getConfig().getString("Servers." + server + ".Name"));
                    glow = pl.getServerSelectorConfig().getConfig().getBoolean("Servers." + server + ".Glow");
                    lore = new ArrayList<>();
                    srw = ChatColor.translateAlternateColorCodes('&', pl.getServerSelectorConfig().getConfig().getString("Servers." + server + ".Server"));
                    for (String l : pl.getServerSelectorConfig().getConfig().getStringList("Servers." + server + ".Lore")) {

                        lore.add(ChatColor.translateAlternateColorCodes('&', l));

                    }
                }

                inv.setItem(slot,createGuiItem(m, name,lore,amount,glow));
                takenSlots.add(slot);
                serverCommands.put(name,srw);
            }
           /* inv.setItem(slot,createGuiItem(m, name,lore,amount,glow));
            takenSlots.add(slot);
            serverCommands.put(name,srw);*/
            if(autofill){
                for(int i = 1; i<pl.getServerSelector().getSize();i++){
                    if(!takenSlots.contains(i)){
                        inv.setItem(i,createGuiItem(autofill_m, autofill_name,autofill_lore,autofill_amount,autofill_glow));
                    }
                }
            }

    }}

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, List<String> lore, final int amount, boolean glow) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);
        if(glow){
            meta.addEnchant(Enchantment.DURABILITY,1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        // Set the lore of the item
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.CHEST) return;
        if (!e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes
                ('&',pl.getServerSelectorConfig().getConfig().getString("Title")))) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;
        if(!takenSlots.contains(e.getSlot())) return;
        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        String name = clickedItem.getItemMeta().getDisplayName();
        if(serverCommands.get(name).isEmpty()) return;
        if(!p.hasPermission("beasthubutilities.serverselector."+serverCommands.get(name))){
            Utils.sendMessage(p,ChatColor.translateAlternateColorCodes('&',"%prefix% &cYou do not have permission to go on: "+serverCommands.get(name)));
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverCommands.get(name));
        p.sendPluginMessage(pl, "BungeeCord", out.toByteArray());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(e.getViewers().get(0).getInventory())) {
            e.setCancelled(true);
        }
    }
}
