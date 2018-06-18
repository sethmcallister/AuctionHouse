package com.islesmc.auctionhouse.dto;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GooseItem {
    private final Material material;
    private final Integer amount;
    private final String name;
    private final List<String> lore;
    private final Map<String, Integer> enchantments;

    private GooseItem(final Material material, final Integer amount, final String name, final List<String> lore, final Map<String, Integer> enchantments) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.enchantments = enchantments;
    }

    public static GooseItem fromItemStack(final ItemStack itemStack) {
        Map<String, Integer> enchantments = new HashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
            enchantments.put(entry.getKey().getName(), entry.getValue());
        }
        String name = itemStack.getType().name();
        if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null && !itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("null"))
            name = itemStack.getItemMeta().getDisplayName();

        System.out.println(name);

        return new GooseItem(itemStack.getType(), itemStack.getAmount(), name, itemStack.getItemMeta().getLore(), enchantments);
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(this.material);
        itemStack.setAmount(this.amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(this.lore);
        if (!this.name.equalsIgnoreCase(this.material.name()))
            meta.setDisplayName(this.name);
        for (Map.Entry<String, Integer> entry : this.enchantments.entrySet()) {
            meta.addEnchant(Enchantment.getByName(entry.getKey()), entry.getValue(), true);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public Map<String, Integer> getEnchantments() {
        return enchantments;
    }
}
