package org.crychicteam.cibrary.content.armorset.defaults;

import net.minecraft.world.entity.LivingEntity;
import org.crychicteam.cibrary.content.armorset.ArmorSet;

public class DefaultArmorSet extends ArmorSet {

    public DefaultArmorSet() {
        super(new DefaultSetEffect());
    }

    @Override
    public boolean matches(LivingEntity entity) {
        return false;
    }
}