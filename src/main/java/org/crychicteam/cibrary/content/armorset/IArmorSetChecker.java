package org.crychicteam.cibrary.content.armorset;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.integration.CuriosIntegration;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface IArmorSetChecker {

    boolean matches(LivingEntity entity);

    default Map<EquipmentSlot, ItemStack> getEquippedItems(ServerPlayer entity) {
        ArmorSet armorSet = Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet(entity);
        return getEquipmentSlotItemStackMap(entity, armorSet.getEquipmentItems());
    }

    @NotNull
    static Map<EquipmentSlot, ItemStack> getEquipmentSlotItemStackMap(ServerPlayer entity, Map<EquipmentSlot, Set<Item>> equipmentItems) {
        Map<EquipmentSlot, ItemStack> equippedItems = new EnumMap<>(EquipmentSlot.class);
        for (Map.Entry<EquipmentSlot, Set<Item>> entry : equipmentItems.entrySet()) {
            ItemStack equippedItem = entity.getItemBySlot(entry.getKey());
            if (entry.getValue().contains(equippedItem.getItem())) {
                equippedItems.put(entry.getKey(), equippedItem);
            }
        }
        return equippedItems;
    }

    default List<ItemStack> getEquippedCurioItems(ServerPlayer entity) {
        ArmorSet armorSet = Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet(entity);
        return getItemStacks(entity, armorSet.getCurioItems());
    }

    @NotNull
    static List<ItemStack> getItemStacks(ServerPlayer entity, Map<Item, Integer> curioItems) {
        List<ItemStack> equippedCurios = new ArrayList<>();
        if (CuriosIntegration.isCuriosLoaded) {
            List<ItemStack> allCurios = CuriosIntegration.getAllItems(entity);
            for (ItemStack curioStack : allCurios) {
                if (curioItems.containsKey(curioStack.getItem())) {
                    equippedCurios.add(curioStack);
                }
            }
        }
        return equippedCurios;
    }

    default Map<EquipmentSlot, ItemStack> getAllEquippedItems(ServerPlayer entity) {
        Map<EquipmentSlot, ItemStack> allEquipped = getEquippedItems(entity);
        List<ItemStack> curios = getEquippedCurioItems(entity);
        if (!curios.isEmpty()) {
            allEquipped.put(EquipmentSlot.OFFHAND, curios.get(0));
        }
        return allEquipped;
    }
}