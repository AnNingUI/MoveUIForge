package org.crychicteam.cibrary.content.armorset.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.network.CibraryNetworkHandler;

import java.util.*;

public class ArmorSetCapabilityHandler implements IArmorSetCapability, INBTSerializable<CompoundTag> {
    private ArmorSet activeSet;
    private Map<Item, ArmorSet> itemSetMap;
    private Map<Item, ArmorSet> curioSetMap;

    public ArmorSetCapabilityHandler() {
        this.activeSet = Cibrary.ARMOR_SET_MANAGER.getDefaultArmorSet();
        this.itemSetMap = new HashMap<>();
        this.curioSetMap = new HashMap<>();
        updateItemSetMaps();
    }

    @Override
    public ArmorSet getActiveSet() {
        return activeSet != null ? activeSet : Cibrary.ARMOR_SET_MANAGER.getDefaultArmorSet();
    }

    @Override
    public ArmorSet.State getState() {
        return activeSet.getState();
    }

    @Override
    public String getSkillState() {
        return activeSet.getSkillState();
    }

    @Override
    public void setActiveSet(ArmorSet set) {
        this.activeSet = set;
        updateItemSetMaps();
    }

    @Override
    public void setState(ArmorSet.State state) {
        activeSet.setState(state);
    }

    @Override
    public void setSkillState(String state) {
        activeSet.setSkillState(state);
    }

    @Override
    public boolean isItemInActiveSet(ItemStack itemStack) {
        return itemSetMap.containsKey(itemStack.getItem()) || curioSetMap.containsKey(itemStack.getItem());
    }

    @Override
    public ArmorSet getSetForItem(ItemStack itemStack) {
        ArmorSet set = itemSetMap.get(itemStack.getItem());
        if (set == null) {
            set = curioSetMap.get(itemStack.getItem());
        }
        return set != null ? set : Cibrary.ARMOR_SET_MANAGER.getDefaultArmorSet();
    }

    private void updateItemSetMaps() {
        itemSetMap.clear();
        curioSetMap.clear();
        if (activeSet != null) {
            for (Map.Entry<EquipmentSlot, Set<Item>> entry : activeSet.getEquipmentItems().entrySet()) {
                for (Item item : entry.getValue()) {
                    if (item != null) {
                        itemSetMap.put(item, activeSet);
                    }
                }
            }
            for (Map.Entry<Item, Integer> entry : activeSet.getCurioItems().entrySet()) {
                curioSetMap.put(entry.getKey(), activeSet);
            }
        }
    }

    @Override
    public List<Component> getAdditionalTooltip(ItemStack itemStack) {
        List<Component> tooltip = new ArrayList<>();
        if (activeSet != null && isItemInActiveSet(itemStack)) {
            addEffectsToTooltip(tooltip);
            addAttributesToTooltip(tooltip);
        }
        return tooltip;
    }

    private void addEffectsToTooltip(List<Component> tooltip) {
        for (Map.Entry<MobEffect, Integer> entry : activeSet.getEffects().entrySet()) {
            tooltip.add(Component.translatable("tooltip.armorset.effect",
                    Component.translatable(entry.getKey().getDescriptionId()),
                    entry.getValue() + 1));
        }
    }

    private void addAttributesToTooltip(List<Component> tooltip) {
        for (Map.Entry<Attribute, AttributeModifier> entry : activeSet.getAttributes().entries()) {
            AttributeModifier modifier = entry.getValue();
            double amount = modifier.getAmount();
            String operation = modifier.getOperation() == AttributeModifier.Operation.ADDITION ? "+" : "×";
            tooltip.add(Component.translatable("tooltip.armorset.attribute",
                    Component.translatable(entry.getKey().getDescriptionId()),
                    operation + String.format("%.2f", amount)));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (activeSet != null) {
            nbt.putString("activeSet", activeSet.getIdentifier().toString());
            nbt.putString("state", activeSet.getState().name());
            nbt.putString("skillState", activeSet.getSkillState());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        String identifier = nbt.getString("activeSet");
        this.activeSet = Cibrary.ARMOR_SET_MANAGER.getArmorSetByIdentifier(identifier);
        if (this.activeSet != null) {
            try {
                this.activeSet.setState(ArmorSet.State.valueOf(nbt.getString("state")));
            } catch (IllegalArgumentException e) {
                this.activeSet.setState(ArmorSet.State.NORMAL);
            }
            this.activeSet.setSkillState(nbt.getString("skillState"));
        }
        updateItemSetMaps();
    }

    @Override
    public void syncToClient(Player player) {
        if (!player.level().isClientSide() && player instanceof ServerPlayer) {
            CibraryNetworkHandler.sendArmorSetSync((ServerPlayer) player, this);
        }
    }
}