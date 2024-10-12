package org.crychicteam.cibrary.content.events.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.common.ArmorSetManager;
import org.crychicteam.cibrary.content.armorset.integration.CuriosIntegration;
import org.crychicteam.cibrary.content.event.ItemDamageEvent;
import org.crychicteam.cibrary.content.event.ItemHurtEffectResult;
import org.crychicteam.cibrary.content.event.StandOnFluidEvent;

import java.util.Map;
import java.util.Set;

import static org.crychicteam.cibrary.content.events.common.ArmorSetHandler.syncArmorSet;

public class SetEffectHandler {
    private final ArmorSetManager armorSetManager = ArmorSetManager.getInstance();

    @SubscribeEvent
    public void onEntityFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet(player);
            activeSet.getEffect().releaseEffect(player);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer player) {
            ArmorSet set = armorSetManager.getActiveArmorSet(player);
            if (player.isSprinting()) {
                set.getEffect().sprintingEffect(player);
            } else {
                set.getEffect().normalTickingEffect(player);
            }
            if (!player.isBlocking()) {
                set.getEffect().blockingEffect(player);
            }
            if (!player.isCrouching()) {
                set.getEffect().crouchingEffect(player);
            }
        }
    }

    @SubscribeEvent
    public void onItemHurt(ItemDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet(player);
            ItemStack damagedItem = event.getItemStack();
            boolean isSetItem = false;
            for (Map.Entry<EquipmentSlot, Set<Item>> entry : activeSet.getEquipmentItems().entrySet()) {
                if (entry.getValue().contains(damagedItem.getItem())) {
                    isSetItem = true;
                    break;
                }
            }
            if (!isSetItem && CuriosIntegration.isCuriosLoaded) {
                isSetItem = activeSet.getCurioItems().containsKey(damagedItem.getItem());
            }
            if (!isSetItem) {
                isSetItem = damagedItem == player.getMainHandItem() || damagedItem == player.getOffhandItem();
            }
            if (isSetItem) {
                ItemHurtEffectResult result = activeSet.getEffect().itemHurtEffect(player, damagedItem, event.getDamage());
                if (result.cancelled()) {
                    event.setCanceled(true);
                } else if (result.damage() != event.getDamage()) {
                    event.setDamage(result.damage());
                }
                if (result.destroyed()) {
                    damagedItem.setCount(0);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    syncArmorSet(serverPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingTargetChange(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewTarget();
        if (target instanceof Player player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet(player);
            activeSet.getEffect().onTargetedEffect(player, event);
        }
    }

    @SubscribeEvent
    public void onStandOnLiquid(StandOnFluidEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet(player);
            activeSet.getEffect().onStandOnFluidEffect(player, event);
        }
    }

    @SubscribeEvent
    public void stackedOnOther(ItemStackedOnOtherEvent event) {
        ArmorSet activeSet = armorSetManager.getActiveArmorSet(event.getPlayer());
        activeSet.getEffect().stackedOnOther(event.getPlayer(), event);
    }
}