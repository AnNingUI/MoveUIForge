package org.crychicteam.cibrary.content.armorset.example;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.SetEffect;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.capability.IArmorSetCapability;
import org.crychicteam.cibrary.content.event.ItemHurtEffectResult;

public class ExampleSetEffect implements SetEffect {
    @Override
    public void applyEffect(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel level) {
            var lighting = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            lighting.setPos(entity.position());
            level.addFreshEntity(lighting);
        }
        if (entity instanceof ServerPlayer player) {
            player.getCapability(ArmorSetCapability.ARMOR_SET_CAPABILITY).ifPresent(cap ->{
                ArmorSet activateSet = cap.getActiveSet();
                activateSet.getEquippedItems(player).forEach((slot,item)->{
                    if (item.is(Items.DIAMOND_HELMET)) {
                        player.getCooldowns().addCooldown(item.getItem(), 20);
                    }
                });
            });
        }
    }

    @Override
    public void landEffect(LivingEntity entity, double distance, BlockState landingBlock, BlockPos pos) {
        if (distance > 5 && entity.isSprinting()){
            entity.level().explode(entity,entity.getX(),entity.getY(),entity.getZ(),10,false, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void onTargetedEffect(LivingEntity entity, LivingChangeTargetEvent changer) {
        changer.setCanceled(true);
    }

    @Override
    public ItemHurtEffectResult itemHurtEffect(LivingEntity entity, ItemStack item, int originalDamage) {
        if (item.getMaxDamage() <= originalDamage + item.getDamageValue()) {
            item.setDamageValue(item.getMaxDamage()-1);
            return ItemHurtEffectResult.cancel();
        }
        return ItemHurtEffectResult.unmodified();
    }

    @Override
    public void removeEffect(LivingEntity entity) {}

    @Override
    public String getIdentifier() {
        return "example_armor_set_effect";
    }
}
