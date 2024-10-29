package org.crychicteam.cibrary.content.armorset.defaults;

import net.minecraft.world.entity.LivingEntity;
import org.crychicteam.cibrary.content.armorset.ISetEffect;

public class DefaultSetEffect implements ISetEffect {
    public DefaultSetEffect() {
    }

    @Override
    public void applyEffect(LivingEntity entity) {

    }

    @Override
    public void removeEffect(LivingEntity entity) {

    }

    @Override
    public String getIdentifier() {
        return "defaultSetEffect";
    }
}
