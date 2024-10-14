package org.crychicteam.cibrary.content.armorset.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.SetEffect;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.network.CibraryNetworkHandler;

import java.util.HashSet;
import java.util.Set;

public class ArmorSetUpdater extends ArmorSetRegistry {
    protected ArmorSetUpdater() {
        super();
    }

    public void updateEntitySetEffect(LivingEntity entity) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            player.getCapability(ArmorSetCapability.ARMOR_SET_CAPABILITY).ifPresent(armorSetCap -> {
                ArmorSet currentSet = armorSetCap.getActiveSet();
                ArmorSet newMatchedSet = findMatchingArmorSet(player);

                if (currentSet != newMatchedSet) {
                    if (currentSet != null) {
                        removeArmorSetEffect(player, currentSet);
                    }
                    armorSetCap.setActiveSet(newMatchedSet);
                    if (newMatchedSet != null) {
                        applyArmorSetEffect(player, newMatchedSet);
                    }
                    CibraryNetworkHandler.sendArmorSetSync((ServerPlayer) player, armorSetCap);
                }
            });
        }
    }

    private ArmorSet findMatchingArmorSet(LivingEntity entity) {
        Set<ArmorSet> potentialSets = new HashSet<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Item item = entity.getItemBySlot(slot).getItem();
            Set<ArmorSet> setsForItem = itemToSetIndex.get(item);
            if (setsForItem != null) {
                if (potentialSets.isEmpty()) {
                    potentialSets.addAll(setsForItem);
                } else {
                    potentialSets.retainAll(setsForItem);
                }
            }
        }

        return potentialSets.stream()
                .filter(set -> set.matches(entity))
                .findFirst()
                .orElse(defaultArmorSet);
    }

    private void applyArmorSetEffect(LivingEntity entity, ArmorSet armorSet) {
        armorSet.applyEffectsAndAttributes(entity);
        SetEffect effect = armorSet.getEffect();
        if (effect != null) {
            effect.applyEffect(entity);
        }
    }

    private void removeArmorSetEffect(LivingEntity entity, ArmorSet armorSet) {
        armorSet.removeEffectsAndAttributes(entity);
        SetEffect effect = armorSet.getEffect();
        if (effect != null) {
            effect.removeEffect(entity);
        }
    }
}