package com.islesmc.auctionhouse.listener;

import com.islesmc.auctionhouse.Main;
import com.islesmc.auctionhouse.dto.AuctionEndResponse;
import com.islesmc.auctionhouse.dto.BiddableItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Main.getInstance().getAuctionHouseGUI().getAwaitingInventoryPlacement().containsKey(player.getUniqueId())) {
            List<BiddableItem> items = Main.getInstance().getAuctionHouseGUI().getAwaitingInventoryPlacement().get(player.getUniqueId());
            for (BiddableItem item : items) {
                // Here the player is the buyer
                player.getInventory().addItem(item.getGooseItem().toItemStack());
                player.sendMessage(ChatColor.GREEN + String.format("You have successfully won the bid for the %sx %s for $%s.", item.getGooseItem().getAmount(), item.getGooseItem().getName(), item.getCurrentPrice().get()));
            }
            Main.getInstance().getAuctionHouseGUI().getAwaitingInventoryPlacement().put(player.getUniqueId(), new ArrayList<>());
        }

        if (Main.getInstance().getAuctionHouseGUI().getSold().containsKey(player.getUniqueId())) {
            List<BiddableItem> items = Main.getInstance().getAuctionHouseGUI().getSold().get(player.getUniqueId());
            for (BiddableItem item : items) {
                // Here the player is the seller
                if(item.getAuctionEndResponse() == AuctionEndResponse.ERROR || item.getCurrentBidder() == null) {
                    player.getInventory().addItem(item.getGooseItem().toItemStack());
                    player.sendMessage(ChatColor.GREEN + String.format("Your %sx %s was not sold.", item.getGooseItem().getAmount(), item.getGooseItem().getName()));
                    continue;
                }

                OfflinePlayer buyer = Bukkit.getOfflinePlayer(item.getCurrentBidder());

                player.sendMessage(ChatColor.GREEN + String.format("Your %sx %s was sold on auction to %s for $%s.", item.getGooseItem().getAmount(), item.getGooseItem().getName(), buyer.getName(), item.getCurrentPrice()));
            }
            Main.getInstance().getAuctionHouseGUI().getSold().put(player.getUniqueId(), new ArrayList<>());
        }
    }
}
