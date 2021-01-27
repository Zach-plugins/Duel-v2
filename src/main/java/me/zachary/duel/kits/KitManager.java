package me.zachary.duel.kits;

import me.zachary.duel.Duel;
import me.zachary.duel.arenas.Arena;
import me.zachary.duel.utils.LocationUtils;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitManager {
    private List<Kit> kits = new ArrayList<>();
    private Duel plugin;

    public KitManager(Duel plugin) {
        this.plugin = plugin;
    }

    public void clearKit(){
        this.kits.clear();
    }

    public void loadKit(){
        for(String string : plugin.getConfigurationSectionKit().getKeys(false)) {
            ItemStack helmet = new ItemBuilder(XMaterial.valueOf(plugin.getConfigurationSectionKit().getString(string + ".Helmet.name")).parseMaterial()).build();
            ItemStack chestplate = new ItemBuilder(XMaterial.valueOf(plugin.getConfigurationSectionKit().getString(string + ".Chestplate.name")).parseMaterial()).build();
            ItemStack leggings = new ItemBuilder(XMaterial.valueOf(plugin.getConfigurationSectionKit().getString(string + ".Leggings.name")).parseMaterial()).build();
            ItemStack boots = new ItemBuilder(XMaterial.valueOf(plugin.getConfigurationSectionKit().getString(string + ".Boots.name")).parseMaterial()).build();
            if(plugin.getConfigurationSectionKit().getString(string + ".Helmet.enchantment") != null){
                me.zachary.zachcore.utils.xseries.XEnchantment.addEnchantFromString(helmet, plugin.getConfigurationSectionKit().getString(".Helmet.enchantment.name") + "," + plugin.getConfigurationSectionKit().getString(".Helmet.enchantment.level"));
            }
            if(plugin.getConfigurationSectionKit().getString(string + ".Chestplate.enchantment") != null){
                me.zachary.zachcore.utils.xseries.XEnchantment.addEnchantFromString(chestplate, plugin.getConfigurationSectionKit().getString(".Chestplate.enchantment.name") + "," + plugin.getConfigurationSectionKit().getString(".Chestplate.enchantment.level"));
            }
            if(plugin.getConfigurationSectionKit().getString(string + ".Leggings.enchantment") != null){
                me.zachary.zachcore.utils.xseries.XEnchantment.addEnchantFromString(leggings, plugin.getConfigurationSectionKit().getString(".Leggings.enchantment.name") + "," + plugin.getConfigurationSectionKit().getString(".Leggings.enchantment.level"));
            }
            if(plugin.getConfigurationSectionKit().getString(string + ".Boots.enchantment") != null){
                me.zachary.zachcore.utils.xseries.XEnchantment.addEnchantFromString(boots, plugin.getConfigurationSectionKit().getString(".Boots.enchantment.name") + "," + plugin.getConfigurationSectionKit().getString(".Boots.enchantment.level"));
            }

            ConfigurationSection content = plugin.kitConfig.getConfigurationSection("kits." + string + ".Content");
            Inventory inventory = Bukkit.createInventory(null, 45);
            for (String item : content.getKeys(false)) {
                ItemStack itemStack;
                if(content.getString(item + ".data") != null){
                    itemStack = new ItemStack(Material.valueOf(content.getString(item + ".name")), 1, (short) content.getInt(item + ".data"));
                }else{
                    itemStack = new ItemStack(XMaterial.valueOf(content.getString(item + ".name")).parseMaterial());
                }
                if(content.getString(item + ".enchantment") != null) me.zachary.zachcore.utils.xseries.XEnchantment.addEnchantFromString(itemStack, content.getString(item + ".enchantment.name") + "," + content.getString(item + ".enchantment.level"));
                if(content.getString(item + ".amount") != null) itemStack.setAmount(Integer.parseInt(content.getString(item + ".amount")));
                inventory.addItem(itemStack);
            }
            Kit kit = new Kit(string, XMaterial.valueOf(plugin.getConfigurationSectionKit().getString(string + ".Icon")).parseMaterial(), inventory, helmet, chestplate, leggings, boots);
            this.addKit(kit);
        }
        plugin.getLog().log("Successful load " + kits.size() + " kits.");
    }

    public void reloadKit(){
        clearKit();
        loadKit();
    }

    public void addKit(Kit kit){
        this.kits.add(kit);
    }

    public List<Kit> getKits(){
        return kits;
    }
}
