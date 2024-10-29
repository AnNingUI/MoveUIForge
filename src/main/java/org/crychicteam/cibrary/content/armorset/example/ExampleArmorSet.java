package org.crychicteam.cibrary.content.armorset.example;

import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.ISetEffect;

public class ExampleArmorSet extends ArmorSet {
    public ExampleArmorSet(String identifier, ISetEffect effect) {
        super(identifier, effect);
    }

    @Override
    public boolean attackerOnCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
        event.setResult(CriticalHitEvent.Result.DENY);
        return super.attackerOnCriticalHit(cache, event);
    }

    @Override
    public void onPlayerAttack(PlayerAttackCache cache) {
        if (cache.getPlayerAttackEntityEvent().getTarget() != null) {
            cache.getPlayerAttackEntityEvent().getTarget().kill();
        }
    }
}
