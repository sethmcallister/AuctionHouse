package com.islesmc.auctionhouse.command;

import com.google.common.collect.Lists;
import com.islesmc.auctionhouse.Main;
import com.islesmc.auctionhouse.dto.BiddableItem;
import com.islesmc.auctionhouse.dto.GooseItem;
import com.islesmc.auctionhouse.task.BidEndTask;
import com.islesmc.auctionhouse.util.TimeUtil;
import com.islesmc.modules.api.API;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.commands.SubCommand;

public class AuctionHouseSellCommand extends SubCommand {
    public AuctionHouseSellCommand() {
        super("sell", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length != 3) {
            player.sendMessage(ChatColor.RED + "Usage: /ah sell <initialPrice>");
            return;
        }


        ItemStack itemStack = player.getItemInHand();
        if(itemStack == null) {
            player.sendMessage(ChatColor.RED  + "You are not holding anything in your hand to auction.");
            return;
        }

        if (itemStack.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED  + "You are not allowed to auction this item.");
            return;
        }

        if (Main.getInstance().getAuctionHouseGUI().findBiddableByItemStack(itemStack) != null) {
            player.sendMessage(ChatColor.RED + "An item very similar to this on is already being sold.");
            return;
        }

        String initialPriceStr = args[0];
        if (!StringUtils.isNumeric(initialPriceStr)) {
            player.sendMessage(ChatColor.RED + String.format("The argument '%s' is not a number.", initialPriceStr));
            return;
        }

        Integer initialPrice = Integer.parseInt(initialPriceStr);
        GooseItem item = GooseItem.fromItemStack(itemStack);
        BiddableItem biddableItem = new BiddableItem(item, initialPrice, player.getUniqueId());
        Main.getInstance().getAuctionHouseGUI().getItems().add(biddableItem);
        player.getInventory().remove(itemStack);
        player.sendMessage(ChatColor.YELLOW + String.format("You have listed the item %s with:", item.getName()));
        player.sendMessage(ChatColor.GRAY + " \u00BB " + ChatColor.YELLOW + "Initial Price: " + ChatColor.WHITE + biddableItem.getCurrentPrice());
        player.sendMessage(ChatColor.GRAY + " \u00BB " + ChatColor.YELLOW + "Bid Increment: " + ChatColor.WHITE + "$100");
    }
}
