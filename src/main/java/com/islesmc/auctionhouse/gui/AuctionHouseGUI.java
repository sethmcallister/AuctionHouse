package com.islesmc.auctionhouse.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.islesmc.auctionhouse.Main;
import com.islesmc.auctionhouse.dto.BiddableItem;
import com.islesmc.auctionhouse.dto.GooseItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AuctionHouseGUI {
    private transient Gson gson;
    private transient String fileName;
    private List<BiddableItem> items;
    private Map<UUID, List<BiddableItem>> awaitingInventoryPlacement;
    private Map<UUID, List<BiddableItem>> sold;

    public AuctionHouseGUI() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.fileName = Main.getInstance().getModuleDir() + File.separator + "auction.json";
        this.items = new ArrayList<>();
        this.awaitingInventoryPlacement = new HashMap<>();
        this.sold = new HashMap<>();
    }

    public Inventory createInventory() {
        int size = getInventorySize(this.items.size());
        Inventory inventory = Bukkit.createInventory(null, size * 3, ChatColor.translateAlternateColorCodes('&', "&fAuction House"));
        for (int i = 0; i < 9; i++)
            inventory.setItem(i, getGrayGlass());

        for (int i = 0; i < items.size(); i++) {
            BiddableItem item = items.get(i);

            ItemStack itemStack = item.getGooseItem().toItemStack();
            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eCurrent Price: &f" + item.getCurrentPrice()));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eBid Increment: &f" + "$100"));
            lore.add(" ");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&fClick to bid."));
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            inventory.addItem(itemStack);
        }

        for (int i = (inventory.getSize() - 9); i < inventory.getSize(); i++)
            inventory.setItem(i, getGrayGlass());

        return inventory;
    }

    public List<BiddableItem> getItems() {
        return this.items;
    }

    public BiddableItem findBiddableByItemStack(final ItemStack itemStack) {
        return this.items.stream().filter(biddableItem -> biddableItem.getGooseItem().toItemStack().isSimilar(itemStack)).findFirst().orElse(null);
    }

    public void save() {
        String json = this.gson.toJson(this);
        System.out.println(json);
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JsonParser parser = new JsonParser();

            try (FileReader fileReader = new FileReader(this.fileName)) {
                JsonElement element = parser.parse(fileReader);
                AuctionHouseGUI auctionHouseGUI = this.gson.fromJson(element, AuctionHouseGUI.class);
                if (auctionHouseGUI == null) {
                    save();
                    return;
                }
                this.items = auctionHouseGUI.items;
                this.sold = auctionHouseGUI.sold;
                this.awaitingInventoryPlacement = auctionHouseGUI.awaitingInventoryPlacement;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ItemStack getGrayGlass() {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
    }

    private int getInventorySize(int max) {
        if (max < 1)
            return 9;
        if (max > 54)
            return 54;
        max += 8;
        return max - (max % 9);
    }

    public Map<UUID, List<BiddableItem>> getAwaitingInventoryPlacement() {
        return awaitingInventoryPlacement;
    }

    public Map<UUID, List<BiddableItem>> getSold() {
        return sold;
    }
}
