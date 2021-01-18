package me.zachary.duel.kits;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Kit {
    private String kitName;
    private Inventory kit;
    private Material icon;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    public Kit(String kitName, Material icon, Inventory kit, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots){
        this.kitName = kitName;
        this.icon = icon;
        this.kit = kit;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public String getKitName(){
        return kitName;
    }

    public Material getIcon(){
        return icon;
    }

    public Inventory getKit(){
        return kit;
    }

    public ItemStack getHelmet(){
        return helmet;
    }

    public ItemStack getChestplate(){
        return chestplate;
    }

    public ItemStack getLeggings(){
        return leggings;
    }

    public ItemStack getBoots(){
        return boots;
    }
}
