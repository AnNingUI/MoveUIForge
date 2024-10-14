package org.crychicteam.cibrary.content.armorset;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import java.util.function.BiConsumer;

public interface IArmorSetAttackHandler {

    default void onCreateSource(CreateSourceEvent event) {}

    default void onPlayerAttack(PlayerAttackCache cache) {}

    default boolean attackerOnCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
        return false;
    }

    default boolean targetOnCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
        return false;
    }

    default void attackerSetupProfile(AttackCache cache, BiConsumer<LivingEntity, ItemStack> setupProfile) {}

    default void targetSetupProfile(AttackCache cache, BiConsumer<LivingEntity, ItemStack> setupProfile) {}

    default void attackerOnAttack(AttackCache cache, ItemStack weapon) {}

    default void targetOnAttack(AttackCache cache, ItemStack weapon) {}

    default void attackerPostAttack(AttackCache cache, LivingAttackEvent event, ItemStack weapon) {}

    default void targetPostAttack(AttackCache cache, LivingAttackEvent event, ItemStack weapon) {}

    default void attackerOnHurt(AttackCache cache, ItemStack weapon) {}

    default void targetOnHurt(AttackCache cache, ItemStack weapon) {}

    default void attackerOnHurtMaximized(AttackCache cache, ItemStack weapon) {}

    default void targetOnHurtMaximized(AttackCache cache, ItemStack weapon) {}

    default void attackerPostHurt(AttackCache cache, LivingHurtEvent event, ItemStack weapon) {}

    default void targetPostHurt(AttackCache cache, LivingHurtEvent event, ItemStack weapon) {}

    default void attackerOnDamage(AttackCache cache, ItemStack weapon) {}

    default void targetOnDamage(AttackCache cache, ItemStack weapon) {}

    default void attackerOnDamageFinalized(AttackCache cache, ItemStack weapon) {}

    default void targetOnDamageFinalized(AttackCache cache, ItemStack weapon) {}
}