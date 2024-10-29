package org.crychicteam.cibrary.content.armorset.integration;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.ISetEffect;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class KubeJSArmorSet extends ArmorSet {

    private Consumer<CreateSourceEvent> createSourceEventConsumer = event -> {};
    private Consumer<PlayerAttackCache> playerAttackCacheConsumer = cache -> {};
    private BiConsumer<PlayerAttackCache, CriticalHitEvent> attackerCriticalHitConsumer = (cache, event) -> {};
    private BiConsumer<PlayerAttackCache, CriticalHitEvent> targetCriticalHitConsumer = (cache, event) -> {};
    private BiConsumer<AttackCache, BiConsumer<LivingEntity, ItemStack>> attackerSetupProfileConsumer = (cache, setupProfile) -> {};
    private BiConsumer<AttackCache, BiConsumer<LivingEntity, ItemStack>> targetSetupProfileConsumer = (cache, setupProfile) -> {};
    private BiConsumer<AttackCache, ItemStack> attackerAttackConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, ItemStack> targetAttackConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, LivingAttackEvent> attackerPostAttackConsumer = (cache, event) -> {};
    private BiConsumer<AttackCache, LivingAttackEvent> targetPostAttackConsumer = (cache, event) -> {};
    private BiConsumer<AttackCache, ItemStack> attackerHurtConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, ItemStack> targetHurtConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, ItemStack> attackerHurtMaximizedConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, ItemStack> targetHurtMaximizedConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, LivingHurtEvent> attackerPostHurtConsumer = (cache, event) -> {};
    private BiConsumer<AttackCache, LivingHurtEvent> targetPostHurtConsumer = (cache, event) -> {};
    private BiConsumer<AttackCache, ItemStack> attackerDamageConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, ItemStack> targetDamageConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, ItemStack> attackerDamageFinalizedConsumer = (cache, weapon) -> {};
    private BiConsumer<AttackCache, ItemStack> targetDamageFinalizedConsumer = (cache, weapon) -> {};

    public void setIdentifier(String identifier) {
        this.identifier = ResourceKey.create(ARMOR_SET, new ResourceLocation(Cibrary.MOD_ID, identifier));
    }

    public void setEffect(ISetEffect effect) {
        this.effect = effect;
    }

    public void setCreateSourceEventConsumer(Consumer<CreateSourceEvent> consumer) {
        this.createSourceEventConsumer = consumer;
    }

    public void setPlayerAttackCacheConsumer(Consumer<PlayerAttackCache> consumer) {
        this.playerAttackCacheConsumer = consumer;
    }

    public void setAttackerCriticalHitConsumer(BiConsumer<PlayerAttackCache, CriticalHitEvent> consumer) {
        this.attackerCriticalHitConsumer = consumer;
    }

    public void setTargetCriticalHitConsumer(BiConsumer<PlayerAttackCache, CriticalHitEvent> consumer) {
        this.targetCriticalHitConsumer = consumer;
    }

    public void setAttackerSetupProfileConsumer(BiConsumer<AttackCache, BiConsumer<LivingEntity, ItemStack>> consumer) {
        this.attackerSetupProfileConsumer = consumer;
    }

    public void setTargetSetupProfileConsumer(BiConsumer<AttackCache, BiConsumer<LivingEntity, ItemStack>> consumer) {
        this.targetSetupProfileConsumer = consumer;
    }

    public void setAttackerAttackConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.attackerAttackConsumer = consumer;
    }

    public void setTargetAttackConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.targetAttackConsumer = consumer;
    }

    public void setAttackerPostAttackConsumer(BiConsumer<AttackCache, LivingAttackEvent> consumer) {
        this.attackerPostAttackConsumer = consumer;
    }

    public void setTargetPostAttackConsumer(BiConsumer<AttackCache, LivingAttackEvent> consumer) {
        this.targetPostAttackConsumer = consumer;
    }

    public void setAttackerHurtConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.attackerHurtConsumer = consumer;
    }

    public void setTargetHurtConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.targetHurtConsumer = consumer;
    }

    public void setAttackerHurtMaximizedConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.attackerHurtMaximizedConsumer = consumer;
    }

    public void setTargetHurtMaximizedConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.targetHurtMaximizedConsumer = consumer;
    }

    public void setAttackerPostHurtConsumer(BiConsumer<AttackCache, LivingHurtEvent> consumer) {
        this.attackerPostHurtConsumer = consumer;
    }

    public void setTargetPostHurtConsumer(BiConsumer<AttackCache, LivingHurtEvent> consumer) {
        this.targetPostHurtConsumer = consumer;
    }

    public void setAttackerDamageConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.attackerDamageConsumer = consumer;
    }

    public void setTargetDamageConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.targetDamageConsumer = consumer;
    }

    public void setAttackerDamageFinalizedConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.attackerDamageFinalizedConsumer = consumer;
    }

    public void setTargetDamageFinalizedConsumer(BiConsumer<AttackCache, ItemStack> consumer) {
        this.targetDamageFinalizedConsumer = consumer;
    }

    @Override
    public void onCreateSource(CreateSourceEvent event) {
        createSourceEventConsumer.accept(event);
    }

    @Override
    public void onPlayerAttack(PlayerAttackCache cache) {
        playerAttackCacheConsumer.accept(cache);
    }

    @Override
    public boolean attackerOnCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
        attackerCriticalHitConsumer.accept(cache, event);
        return false;
    }

    @Override
    public boolean targetOnCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
        targetCriticalHitConsumer.accept(cache, event);
        return false;
    }

    @Override
    public void attackerSetupProfile(AttackCache cache, BiConsumer<LivingEntity, ItemStack> setupProfile) {
        attackerSetupProfileConsumer.accept(cache, setupProfile);
    }

    @Override
    public void targetSetupProfile(AttackCache cache, BiConsumer<LivingEntity, ItemStack> setupProfile) {
        targetSetupProfileConsumer.accept(cache, setupProfile);
    }

    @Override
    public void attackerOnAttack(AttackCache cache, ItemStack weapon) {
        attackerAttackConsumer.accept(cache, weapon);
    }

    @Override
    public void targetOnAttack(AttackCache cache, ItemStack weapon) {
        targetAttackConsumer.accept(cache, weapon);
    }

    @Override
    public void attackerPostAttack(AttackCache cache, LivingAttackEvent event, ItemStack weapon) {
        attackerPostAttackConsumer.accept(cache, event);
    }

    @Override
    public void targetPostAttack(AttackCache cache, LivingAttackEvent event, ItemStack weapon) {
        targetPostAttackConsumer.accept(cache, event);
    }

    @Override
    public void attackerOnHurt(AttackCache cache, ItemStack weapon) {
        attackerHurtConsumer.accept(cache, weapon);
    }

    @Override
    public void targetOnHurt(AttackCache cache, ItemStack weapon) {
        targetHurtConsumer.accept(cache, weapon);
    }

    @Override
    public void attackerOnHurtMaximized(AttackCache cache, ItemStack weapon) {
        attackerHurtMaximizedConsumer.accept(cache, weapon);
    }

    @Override
    public void targetOnHurtMaximized(AttackCache cache, ItemStack weapon) {
        targetHurtMaximizedConsumer.accept(cache, weapon);
    }

    @Override
    public void attackerPostHurt(AttackCache cache, LivingHurtEvent event, ItemStack weapon) {
        attackerPostHurtConsumer.accept(cache, event);
    }

    @Override
    public void targetPostHurt(AttackCache cache, LivingHurtEvent event, ItemStack weapon) {
        targetPostHurtConsumer.accept(cache, event);
    }

    @Override
    public void attackerOnDamage(AttackCache cache, ItemStack weapon) {
        attackerDamageConsumer.accept(cache, weapon);
    }

    @Override
    public void targetOnDamage(AttackCache cache, ItemStack weapon) {
        targetDamageConsumer.accept(cache, weapon);
    }

    @Override
    public void attackerOnDamageFinalized(AttackCache cache, ItemStack weapon) {
        attackerDamageFinalizedConsumer.accept(cache, weapon);
    }

    @Override
    public void targetOnDamageFinalized(AttackCache cache, ItemStack weapon) {
        targetDamageFinalizedConsumer.accept(cache, weapon);
    }
}
