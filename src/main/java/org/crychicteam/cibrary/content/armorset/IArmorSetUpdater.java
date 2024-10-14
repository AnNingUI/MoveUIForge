package org.crychicteam.cibrary.content.armorset;

import com.google.common.collect.Multimap;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.crychicteam.cibrary.Cibrary;

import java.util.Map;
import java.util.Objects;

public interface IArmorSetUpdater extends IArmorSetChecker {

    default boolean shouldUpdateMobEffects(LivingEntity entity) {
        if (!(entity instanceof Player player)) return false;
        ArmorSet armorSet = Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet(player);
        Map<MobEffect, Integer> effects = armorSet.getEffects();
        for (Map.Entry<MobEffect, Integer> entry : effects.entrySet()) {
            MobEffectInstance currentEffect = entity.getEffect(entry.getKey());
            if (currentEffect == null || currentEffect.getAmplifier() < entry.getValue()) {
                return true;
            }
        }
        return false;
    }

    default void applyMobEffects(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        if (shouldUpdateMobEffects(player)) {
            ArmorSet armorSet = Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet(player);
            if (armorSet == null) return;

            Map<MobEffect, Integer> effects = armorSet.getEffects();
            for (Map.Entry<MobEffect, Integer> entry : effects.entrySet()) {
                player.addEffect(new MobEffectInstance(entry.getKey(), 600, entry.getValue(), false, false));
            }
        }
    }

    default void applyAttributes(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        ArmorSet armorSet = Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet(player);
        Multimap<Attribute, AttributeModifier> attributes = armorSet.getAttributes();
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Objects.requireNonNull(player.getAttribute(entry.getKey())).addTransientModifier(entry.getValue());
        }
    }

    default void removeEffects(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        ArmorSet armorSet = Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet(player);
        Map<MobEffect, Integer> effects = armorSet.getEffects();
        for (MobEffect effect : effects.keySet()) {
            player.removeEffect(effect);
        }
    }

    default void removeAttributes(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        ArmorSet armorSet = Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet(player);
        Multimap<Attribute, AttributeModifier> attributes = armorSet.getAttributes();
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Objects.requireNonNull(player.getAttribute(entry.getKey())).removeModifier(entry.getValue());
        }
    }

    default void applyEffectsAndAttributes(LivingEntity entity) {
        applyMobEffects(entity);
        applyAttributes(entity);
    }

    default void removeEffectsAndAttributes(LivingEntity entity) {
        removeEffects(entity);
        removeAttributes(entity);
    }
}