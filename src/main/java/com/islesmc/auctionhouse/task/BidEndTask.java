package com.islesmc.auctionhouse.task;

import com.islesmc.auctionhouse.Main;
import com.islesmc.auctionhouse.dto.AuctionEndResponse;
import com.islesmc.auctionhouse.dto.BiddableItem;
import com.islesmc.auctionhouse.dto.GooseItem;
import com.islesmc.modules.api.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BidEndTask extends Task {
    private final BiddableItem biddableItem;

    public BidEndTask(final BiddableItem biddableItem) {
        this.biddableItem = biddableItem;
        System.out.println("starting bid task");
    }

    @Override
    public void run() {
        System.out.println("Selling starting");
        Main.getInstance().getAuctionHouseGUI().getItems().remove(biddableItem);

        OfflinePlayer seller = Bukkit.getOfflinePlayer(biddableItem.getSeller());

        if(biddableItem.getCurrentBidder() == null) {
            this.biddableItem.setAuctionEndResponse(AuctionEndResponse.ERROR);
            System.out.println("error case current bidder = null");
            doNotSold(seller);
        }
        biddableItem.setAuctionEndResponse(AuctionEndResponse.SUCCESS);
        OfflinePlayer buyer = Bukkit.getOfflinePlayer(biddableItem.getCurrentBidder());

        if(!Main.getInstance().getEconomy().has(buyer, biddableItem.getCurrentPrice().get())) {
            biddableItem.setAuctionEndResponse(AuctionEndResponse.ERROR);
            System.out.println("error bidder doesn't have enough money");
            doNotSold(seller);
            return;
        }

        Main.getInstance().getEconomy().withdrawPlayer(buyer, biddableItem.getCurrentPrice().get());
        Main.getInstance().getEconomy().depositPlayer(seller, biddableItem.getCurrentPrice().get());

        GooseItem item = biddableItem.getGooseItem();
        if(seller.isOnline()) {
            ((Player) seller).sendMessage(ChatColor.GREEN + String.format("Your %sx %s was sold on auction to %s for $%s.", item.getAmount(), item.getName(), buyer.getName(), biddableItem.getCurrentPrice()));
        } else {
            List<BiddableItem> biddableItems = Main.getInstance().getAuctionHouseGUI().getSold().get(seller.getUniqueId());
            if(biddableItems == null)
                biddableItems = new ArrayList<>();


            biddableItems.add(biddableItem);

            Main.getInstance().getAuctionHouseGUI().getSold().put(seller.getUniqueId(), biddableItems);
        }

        if (buyer.isOnline()) {
            ((Player) buyer).getInventory().addItem(item.toItemStack());
            ((Player) buyer).sendMessage(ChatColor.GREEN + String.format("You have successfully won the bid for the %sx %s for $%s.", item.getAmount(), item.getName(), biddableItem.getCurrentPrice().get()));
        } else {
            List<BiddableItem> biddableItems = Main.getInstance().getAuctionHouseGUI().getAwaitingInventoryPlacement().get(buyer.getUniqueId());
            if(biddableItems == null)
                biddableItems = new ArrayList<>();


            biddableItems.add(biddableItem);

            Main.getInstance().getAuctionHouseGUI().getAwaitingInventoryPlacement().put(buyer.getUniqueId(), biddableItems);
        }

    }

    private void doNotSold(final OfflinePlayer seller) {
        if (seller.isOnline()) {
            ((Player) seller).getInventory().addItem(biddableItem.getGooseItem().toItemStack());
            ((Player) seller).sendMessage(ChatColor.GREEN + String.format("Your %sx %s was not sold.", biddableItem.getGooseItem().getAmount(), biddableItem.getGooseItem().getName()));
            return;
        }
        List<BiddableItem> biddableItems = Main.getInstance().getAuctionHouseGUI().getSold().get(seller.getUniqueId());
        if(biddableItems == null)
            biddableItems = new ArrayList<>();


        biddableItems.add(biddableItem);

        Main.getInstance().getAuctionHouseGUI().getSold().put(seller.getUniqueId(), biddableItems);
    }
}
