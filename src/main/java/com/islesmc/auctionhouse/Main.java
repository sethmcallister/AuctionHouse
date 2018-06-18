package com.islesmc.auctionhouse;

import com.google.common.collect.Lists;
import com.islesmc.auctionhouse.command.AuctionHouseGUICommand;
import com.islesmc.auctionhouse.command.AuctionHouseSellCommand;
import com.islesmc.auctionhouse.gui.AuctionHouseGUI;
import com.islesmc.auctionhouse.listener.InventoryClickListener;
import com.islesmc.auctionhouse.listener.PlayerJoinListener;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.module.PluginModule;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.sethy.commands.CommandHandler;

public class Main extends PluginModule {
    private static Main instance;
    private AuctionHouseGUI auctionHouseGUI;
    private Economy economy;

    @Override
    public void onEnable() {
        getModuleDir().toFile().mkdir();
        setInstance(this);
        setupEconomy();
        this.auctionHouseGUI = new AuctionHouseGUI();
        this.auctionHouseGUI.load();

        CommandHandler auctionCommand = new CommandHandler("ah");
        auctionCommand.setAliases(Lists.newArrayList("auctionhouse"));
        auctionCommand.setHelpPage(new AuctionHouseGUICommand());
        auctionCommand.addSubCommand(new AuctionHouseGUICommand());
        auctionCommand.addSubCommand(new AuctionHouseSellCommand());
        registerCommand("ah", auctionCommand);

        registerEvent(new PlayerJoinListener());
        registerEvent(new InventoryClickListener());
    }

    @Override
    public void onDisable() {
        this.auctionHouseGUI.save();
    }

    private boolean setupEconomy() {
        if (API.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            System.out.println("no vault");
            return false;
        }
        RegisteredServiceProvider<Economy> economyProvider = API.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider == null) {
            System.out.println("rsp is null");
            return false;
        }
        economy = economyProvider.getProvider();
        return economy != null;
    }

    public static synchronized Main getInstance() {
        return instance;
    }

    public static synchronized void setInstance(Main instance) {
        Main.instance = instance;
    }

    public AuctionHouseGUI getAuctionHouseGUI() {
        return auctionHouseGUI;
    }

    public Economy getEconomy() {
        return economy;
    }
}
