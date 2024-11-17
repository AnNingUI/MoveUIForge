package org.crychicteam.cibrary.api.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
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

/**
 * Registry system for ArmorSet management with optimized lookup capabilities.
 * <p>
 * Data Structures:
 * <p>
 * - Uses FastUtil's Object2ObjectOpenHashMap for performance-critical item-to-set mapping
 * <p>
 * - Maintains bidirectional mapping between items and their associated armor sets
 * <p>
 * - Registry implementation based on Forge's Registry system with custom validation
 * <p>
 * Key Features:
 * <p>
 * - O(1) lookup for item to set associations
 * <p>
 * - Duplicate set detection with deep equality checking
 * <p>
 * - Builder pattern for set construction
 */
public class ArmorSetRegistry {
    public static final ResourceKey<Registry<ArmorSet>> ARMOR_SET_REGISTRY_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(Cibrary.MOD_ID, "armor_set"));

    public static final ResourceLocation EMPTY = new ResourceLocation(Cibrary.MOD_ID, "empty");

    public static final DeferredRegister<ArmorSet> ARMOR_SETS =
            DeferredRegister.create(ARMOR_SET_REGISTRY_KEY, Cibrary.MOD_ID);

    /** Fast lookup map from items to their associated armor sets. Uses FastUtil for better performance. */
    private static final Map<Item, Set<ArmorSet>> itemToSetIndex = new Object2ObjectOpenHashMap<>();

    /**
     * Validates that no identical set exists in registry.
     * <p>
     * Time Complexity: O(n) where n is number of registered sets
     * @param newSet Set to validate
     */
    private static boolean isExactSetExists(ArmorSet newSet) {
        if (getRegistry() == null) return false;
        return getRegistry().getValues().stream()
                .filter(set -> set != newSet)
                .anyMatch(set -> areSetItemsIdentical(set, newSet));
    }

    /**
     * Deep equality check for armor set contents.
     * <p>
     * Validates equipment slots, items, and curio items.
     * <p>
     * Time Complexity: O(m) where m is total number of items in both sets
     */
    private static boolean areSetItemsIdentical(ArmorSet set1, ArmorSet set2) {
        Map<EquipmentSlot, Set<Item>> items1 = set1.getEquipmentItems();
        Map<EquipmentSlot, Set<Item>> items2 = set2.getEquipmentItems();

        if (items1.isEmpty() || items2.isEmpty()) return false;
        if (!items1.keySet().equals(items2.keySet())) return false;

        for (EquipmentSlot slot : items1.keySet()) {
            Set<Item> set1Items = items1.get(slot);
            Set<Item> set2Items = items2.get(slot);

            if (set1Items == null || set2Items == null ||
                    set1Items.isEmpty() || set2Items.isEmpty() ||
                    set1Items.size() != set2Items.size() ||
                    !set1Items.equals(set2Items)) {
                return false;
            }
        }

        return set1.getCurioItems().equals(set2.getCurioItems());
    }

    /**
     * Registry builder with validation and indexing hooks.
     * <p>
     * Implements error checking for:
     * <p>
     * - Null effects
     * <p>
     * - Duplicate sets
     * <p>
     * - Registry constraints
     */
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

    /**
     * Returns an unmodifiable view of the item-to-set index.
     * <p>
     * Prevents external modification while allowing read access.
     * <p>
     */
    public static Map<Item, Set<ArmorSet>> getItemToSetIndex() {
        return Collections.unmodifiableMap(itemToSetIndex);
    }

    /**
     * Indexes an armor set for O(1) lookup by item.
     * <p>
     * Updates both equipment and curio indices.
     * <p>
     * Time Complexity: O(i) where i is total items in set
     * <p>
     */
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

    public static TagKey<ArmorSet> create(ResourceLocation name) {
        return TagKey.create(ARMOR_SET_REGISTRY_KEY, name);
    }

    /**
     * Builder class for ArmorSet construction.
     * <p>
     * Supports:
     * <p>
     * - Equipment slot mapping
     * <p>
     * - Curio item registration
     * <p>
     * - Effect and attribute configuration
     * <p>
     * - State management
     */
    public static class Builder {
        private final ArmorSet armorSet;

        public Builder() {
            this.armorSet = new ArmorSet();
        }

        public Builder(ArmorSet armorSet) {
            this.armorSet = armorSet;
        }

        public static Builder of(ArmorSet armorSet) {
            return new Builder(armorSet);
        }

        public static <T extends ArmorSet> Builder of(Supplier<T> factory) {
            return new Builder(factory.get());
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