package org.crychicteam.cibrary.content.armorset.common;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.crychicteam.cibrary.content.armorset.ArmorSet;

import java.util.function.BiConsumer;

/**
 * This class handle the Damage logic for armor sets.
 * @author M1hono
 */
public class ArmorSetAttackListener implements AttackListener {

    private final ArmorSetManager armorSetManager;

    public ArmorSetAttackListener(ArmorSetManager armorSetManager) {
        this.armorSetManager = armorSetManager;
    }

    @Override
    public void onCreateSource(CreateSourceEvent event) {
        if (event.getAttacker() instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) event.getAttacker());
            if (activeSet != null) {
                activeSet.onCreateSource(event);
            }
        }
    }

    @Override
    public void onPlayerAttack(PlayerAttackCache cache) {
        cache.getAttacker();
        if (!(cache.getAttacker() instanceof Player player)) {
            return;
        }
        ArmorSet activeSet = armorSetManager.getActiveArmorSet(player);
        activeSet.onPlayerAttack(cache);
    }

    @Override
    public boolean onCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
        LivingEntity attacker = cache.getAttacker();
        Entity target = event.getTarget();
        if (attacker instanceof Player player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet(player);
            return activeSet.attackerOnCriticalHit(cache, event);
        } else if (target instanceof  Player player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet(player);
            return activeSet.targetOnCriticalHit(cache, event);
        }
        return false;
    }

    @Override
    public void setupProfile(AttackCache cache, BiConsumer<LivingEntity, ItemStack> setupProfile) {
        LivingEntity attacker = cache.getAttacker();
        LivingEntity target = cache.getAttackTarget();
        if (attacker instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) attacker);
            activeSet.attackerSetupProfile(cache, setupProfile);
        } else if (target instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) target);
            activeSet.targetSetupProfile(cache, setupProfile);
        }
    }

    @Override
    public void onAttack(AttackCache cache, ItemStack weapon) {
        LivingEntity attacker = cache.getAttacker();
        LivingEntity target = cache.getAttackTarget();
        if (attacker instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) attacker);
            activeSet.attackerOnAttack(cache, weapon);
        } else if (target instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) target);
            if (activeSet != null) {
                activeSet.targetOnAttack(cache, weapon);
            }
        }
    }

    @Override
    public void postAttack(AttackCache cache, LivingAttackEvent event, ItemStack weapon) {
        LivingEntity attacker = cache.getAttacker();
        LivingEntity target = cache.getAttackTarget();
        if (attacker instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) attacker);
            activeSet.attackerPostAttack(cache, event, weapon);
        } else if (target instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) target);
            activeSet.targetPostAttack(cache, event, weapon);
        }
    }

    @Override
    public void onHurt(AttackCache cache, ItemStack weapon) {
        LivingEntity attacker = cache.getAttacker();
        LivingEntity target = cache.getAttackTarget();
        if (attacker instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) attacker);
            activeSet.attackerOnHurt(cache, weapon);
        } else if (target instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) target);
            activeSet.targetOnHurt(cache, weapon);
        }
    }

    @Override
    public void onHurtMaximized(AttackCache cache, ItemStack weapon) {
        LivingEntity attacker = cache.getAttacker();
        LivingEntity target = cache.getAttackTarget();
        if (attacker instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) attacker);
            activeSet.attackerOnHurtMaximized(cache, weapon);
        } else if (target instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) target);
            activeSet.targetOnHurtMaximized(cache, weapon);
        }
    }

    @Override
    public void postHurt(AttackCache cache, LivingHurtEvent event, ItemStack weapon) {
        LivingEntity attacker = cache.getAttacker();
        LivingEntity target = cache.getAttackTarget();
        if (attacker instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) attacker);
            activeSet.attackerPostHurt(cache, event, weapon);
        } else if (target instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) target);
            activeSet.targetPostHurt(cache, event, weapon);
        }
    }

    @Override
    public void onDamage(AttackCache cache, ItemStack weapon) {
        LivingEntity attacker = cache.getAttacker();
        LivingEntity target = cache.getAttackTarget();
        if (attacker instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) attacker);
            activeSet.attackerOnDamage(cache, weapon);
        } else if (target instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) target);
            activeSet.targetOnDamage(cache, weapon);
        }
    }

    @Override
    public void onDamageFinalized(AttackCache cache, ItemStack weapon) {
        LivingEntity attacker = cache.getAttacker();
        LivingEntity target = cache.getAttackTarget();
        if (attacker instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) attacker);
            activeSet.attackerOnDamageFinalized(cache, weapon);
        } else if (target instanceof Player) {
            ArmorSet activeSet = armorSetManager.getActiveArmorSet((Player) target);
            activeSet.targetOnDamageFinalized(cache, weapon);
        }
    }
}