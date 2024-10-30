package org.crychicteam.cibrary.api.registry.armorset;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.ISetEffect;
import org.crychicteam.cibrary.content.armorset.defaults.DefaultArmorSet;

import java.util.*;
import java.util.function.Supplier;

public class ArmorSetCustomRegistry {
    public static final ResourceKey<Registry<ArmorSet>> ARMOR_SET_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(Cibrary.MOD_ID, "armor_set"));

    public static final ResourceLocation EMPTY = new ResourceLocation(Cibrary.MOD_ID, "empty");

    public static final DeferredRegister<ArmorSet> ARMOR_SETS =
            DeferredRegister.create(ARMOR_SET_REGISTRY_KEY, Cibrary.MOD_ID);

    private static final Map<Item, Set<ArmorSet>> itemToSetIndex = new Object2ObjectOpenHashMap<>();

    private static boolean isExactSetExists(ArmorSet newSet) {
        if (getRegistry() == null) return false;
        return getRegistry().getValues().stream()
                .filter(set -> set != newSet)
                .anyMatch(set -> areSetItemsIdentical(set, newSet));
    }

    private static boolean areSetItemsIdentical(ArmorSet set1, ArmorSet set2) {
        Map<EquipmentSlot, Set<Item>> items1 = set1.getEquipmentItems();
        Map<EquipmentSlot, Set<Item>> items2 = set2.getEquipmentItems();

        if (items1.isEmpty() || items2.isEmpty()) {
            return false;
        }

        if (!items1.keySet().equals(items2.keySet())) {
            return false;
        }

        for (EquipmentSlot slot : items1.keySet()) {
            Set<Item> set1Items = items1.get(slot);
            Set<Item> set2Items = items2.get(slot);

            if (set1Items == null || set2Items == null ||
                    set1Items.isEmpty() || set2Items.isEmpty() ||
                    set1Items.size() != set2Items.size()) {
                return false;
            }

            if (!set1Items.equals(set2Items)) {
                return false;
            }
        }

        Map<Item, Integer> curios1 = set1.getCurioItems();
        Map<Item, Integer> curios2 = set2.getCurioItems();

        return curios1.equals(curios2);
    }

    private static final Supplier<IForgeRegistry<ArmorSet>> REGISTRY = ARMOR_SETS.makeRegistry(() -> new RegistryBuilder<ArmorSet>()
            .setName(ARMOR_SET_REGISTRY_KEY.location())
            .setMaxID(2048)
            .setDefaultKey(EMPTY)
            .add((owner, id, key, resourceLocation, obj) -> {
                if (obj.getEffect() == null) {
                    throw new IllegalStateException("ArmorSet " + key + " must have an effect!");
                }

                if (resourceLocation == null && isExactSetExists(obj)) {
                    throw new IllegalStateException("ArmorSet with identical equipment already exists: " + key);
                }
                indexArmorSet(obj);
            })
    );

    public static final RegistryObject<ArmorSet> EMPTY_SET = ARMOR_SETS.register("empty",
            DefaultArmorSet::new);

    public static void register(IEventBus modEventBus) {
        ARMOR_SETS.register(modEventBus);
    }

    public static IForgeRegistry<ArmorSet> getRegistry() {
        return REGISTRY.get();
    }

    public static Map<Item, Set<ArmorSet>> getItemToSetIndex() {
        return Collections.unmodifiableMap(itemToSetIndex);
    }

    private static void indexArmorSet(ArmorSet armorSet) {
        for (Map.Entry<EquipmentSlot, Set<Item>> entry : armorSet.getEquipmentItems().entrySet()) {
            for (Item item : entry.getValue()) {
                itemToSetIndex.computeIfAbsent(item, k -> new HashSet<>()).add(armorSet);
            }
        }
        for (Map.Entry<Item, Integer> entry : armorSet.getCurioItems().entrySet()) {
            itemToSetIndex.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).add(armorSet);
        }
    }

    public static class Builder {
        private final ArmorSet armorSet;

        public Builder() {
            this.armorSet = new ArmorSet();
        }

        public Builder effect(ISetEffect effect) {
            armorSet.setEffect(effect);
            return this;
        }

        public Builder addEquipment(EquipmentSlot slot, Item item) {
            armorSet.addEquipmentItem(slot, item);
            return this;
        }

        public Builder addCurio(Item item, int count) {
            armorSet.addCurioItem(item, count);
            return this;
        }

        public Builder addEffect(MobEffect effect, int amplifier) {
            armorSet.addEffect(effect, amplifier);
            return this;
        }

        public Builder addAttribute(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
            armorSet.addAttribute(attribute, name, amount, operation);
            return this;
        }

        public Builder setState(ArmorSet.State state) {
            armorSet.setState(state);
            return this;
        }

        public Builder setSkillState(String skillState) {
            armorSet.setSkillState(skillState);
            return this;
        }

        public ArmorSet build() {
            return armorSet;
        }
    }
}