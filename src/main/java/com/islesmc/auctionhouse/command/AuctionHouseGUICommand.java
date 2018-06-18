package com.islesmc.auctionhouse.command;

import com.google.common.collect.Lists;
import com.islesmc.auctionhouse.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xyz.sethy.commands.SubCommand;

public class AuctionHouseGUICommand extends SubCommand {
    public AuctionHouseGUICommand() {
        super("gui", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length != 0) {
            player.sendMessage(ChatColor.RED + "Usage: /ah gui");
            return;
        }
        Inventory inventory = Main.getInstance().getAuctionHouseGUI().createInventory();
        player.openInventory(inventory);
    }
}
