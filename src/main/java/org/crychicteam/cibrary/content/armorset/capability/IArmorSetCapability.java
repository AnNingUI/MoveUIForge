package org.crychicteam.cibrary.content.armorset.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.crychicteam.cibrary.content.armorset.ArmorSet;

import java.util.List;

public interface IArmorSetCapability extends INBTSerializable<CompoundTag> {
    /**
     *
     * @return {@link ArmorSet}
     */
    ArmorSet getActiveSet();
    ArmorSet.State getState();
    String getSkillState();
    void setActiveSet(ArmorSet set);
    void setState(ArmorSet.State state);
    void setSkillState(String state);
    boolean isItemInActiveSet(ItemStack itemStack);
    ArmorSet getSetForItem(ItemStack itemStack);
    List<Component> getAdditionalTooltip(ItemStack itemStack);
    void syncToClient(Player player);
}