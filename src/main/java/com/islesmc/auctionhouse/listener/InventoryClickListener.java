package com.islesmc.auctionhouse.listener;

import com.islesmc.auctionhouse.Main;
import com.islesmc.auctionhouse.dto.BiddableItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();

        if (inventory == null || inventory.getTitle() == null)
            return;

        if (!inventory.getTitle().contains("Auction House")) {
            return;
        }

        player.closeInventory();

        ItemStack clicked = inventory.getItem(event.getRawSlot());

        if (clicked == null)
            return;

        ItemStack real = clicked.clone();
        ItemMeta meta = real.getItemMeta();
        meta.setLore(new ArrayList<>());
        real.setItemMeta(meta);

        BiddableItem biddableItem = Main.getInstance().getAuctionHouseGUI().findBiddableByItemStack(real);
        if(biddableItem == null)
            return;

        if (biddableItem.getSeller().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot bid on your own auctions.");
            return;
        }

        OfflinePlayer offlineBidder = Bukkit.getOfflinePlayer(player.getUniqueId());

        biddableItem.setCurrentBidder(player.getUniqueId());
        int nextPrice = biddableItem.getCurrentPrice().get() + 100;
        if(!Main.getInstance().getEconomy().has(offlineBidder, nextPrice)) {
            player.sendMessage(ChatColor.RED + "You do not have enough money to bid on this item!");
            return;
        }
        biddableItem.getCurrentPrice().set(nextPrice);
        player.sendMessage(ChatColor.GREEN + String.format("You have placed a bid of %s for %sx %s.", 100, biddableItem.getGooseItem().getAmount(), biddableItem.getGooseItem().toItemStack().getItemMeta().getDisplayName()));
    }
}
