package org.crychicteam.cibrary.content.armorset.example;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.api.registry.armorset.ArmorSetCustomRegistry;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.defaults.DefaultSetEffect;
import org.crychicteam.cibrary.content.armorset.integration.KubeJSSetEffect;

public class ArmorSetRegistryExample {
    public static final RegistryObject<ArmorSet> DEFERRED_REGISTER_EXAMPLE;
    public static final RegistryEntry<ArmorSet> REGISTRATE_EXAMPLE;
    static {
        /**
         * Example for KubeJSSetEffect Registry.
         */
        var effect = new KubeJSSetEffect();
        effect.setApplyEffectConsumer(entity -> {
            entity.setSprinting(true);
        });
        effect.setIdentifier("test_effect");

        /**
         * Example for ArmorSetCustomRegistry.
         */
        DEFERRED_REGISTER_EXAMPLE = ArmorSetCustomRegistry.ARMOR_SETS.register(
                "registry_object",
                () -> new ArmorSetCustomRegistry.Builder()
                        .effect(new DefaultSetEffect())
                        .addEquipment(EquipmentSlot.HEAD, Items.DIAMOND_HELMET)
                        .addEquipment(EquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE)
                        .addEquipment(EquipmentSlot.LEGS, Items.DIAMOND_LEGGINGS)
                        .addEquipment(EquipmentSlot.FEET, Items.DIAMOND_BOOTS)
                        .addEffect(MobEffects.WATER_BREATHING, 0)
                        .setState(ArmorSet.State.NORMAL)
                        .build()
        );
        REGISTRATE_EXAMPLE = Cibrary.CI_REGISTRATE
                .generic("registry_entry", ArmorSetCustomRegistry.ARMOR_SET_REGISTRY_KEY,
                () -> new ArmorSetCustomRegistry.Builder()
                        .effect(new DefaultSetEffect())
                        .addEquipment(EquipmentSlot.HEAD, Items.IRON_HELMET)
                        .addEquipment(EquipmentSlot.CHEST, Items.IRON_CHESTPLATE)
                        .addEquipment(EquipmentSlot.LEGS, Items.IRON_LEGGINGS)
                        .addEquipment(EquipmentSlot.FEET, Items.IRON_BOOTS)
                        .addEffect(MobEffects.DAMAGE_RESISTANCE, 0)
                        .setState(ArmorSet.State.NORMAL)
                        .build()
        ).register();
    }

    /**
     * You should use ArmorSetExample.init() to register your Armor Sets.
     */
    public static void init() {}
}