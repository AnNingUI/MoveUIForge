package org.crychicteam.cibrary.content.armorset.common;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.defaults.DefaultArmorSet;

import java.util.*;

/**
 * Registry for managing armor sets.
 * <p>
 * Currently, the logic is that an armor set has its own special equipment and Curios.
 * <p>
 * If there is a difference in any slot, it will be considered a separate set. When two registered sets are identical, only the first registered set will be loaded.
 * <p>
 * A plan to use a Map to cache the mapping between items and sets to optimize performance is not yet implemented.
 * @author M1hono
 */
public class ArmorSetRegistry {
    protected final List<ArmorSet> armorSets = new ArrayList<>();
    protected final Map<String, ArmorSet> armorSetMap = new HashMap<>();
    protected final Map<Item, Set<ArmorSet>> itemToSetIndex = new Object2ObjectOpenHashMap<>();
    protected static ArmorSet defaultArmorSet;

    /**
     * Initializes the ArmorSetRegistry with a default armor set.
     * <p>
     * Finally initialized in {@link Cibrary#ARMOR_SET_MANAGER}
     */
    protected ArmorSetRegistry() {
        defaultArmorSet = new DefaultArmorSet();
    }

    /**
     * Registers a new armor set if it does not already exist.
     *
     * @param armorSet The armor set to register.
     */
    public void registerArmorSet(ArmorSet armorSet) {
        if (!isExactSetExists(armorSet)) {
            armorSets.add(armorSet);
            armorSetMap.put(armorSet.getIdentifier().toString(), armorSet);
            indexArmorSet(armorSet);
        }
        Cibrary.LOGGER.info("Loaded armor set: " + armorSet.getIdentifier());
    }

    /**
     * Indexes the items of the given armor set.
     *
     * @param armorSet The armor set to index.
     */
    private void indexArmorSet(ArmorSet armorSet) {
        for (Map.Entry<EquipmentSlot, Set<Item>> entry : armorSet.getEquipmentItems().entrySet()) {
            for (Item item : entry.getValue()) {
                itemToSetIndex.computeIfAbsent(item, k -> new HashSet<>()).add(armorSet);
            }
        }
    }

    /**
     * Checks if an identical armor set already exists.
     *
     * @param newSet The new armor set to check.
     * @return True if an identical armor set exists, false otherwise.
     */
    private boolean isExactSetExists(ArmorSet newSet) {
        return armorSets.stream().anyMatch(set -> areSetItemsIdentical(set, newSet));
    }

    /**
     * Compares two armor sets to determine if their items are identical.
     *
     * @param set1 The first armor set.
     * @param set2 The second armor set.
     * @return True if the items in both sets are identical, false otherwise.
     */
    private boolean areSetItemsIdentical(ArmorSet set1, ArmorSet set2) {
        return Objects.equals(set1.getEquipmentItems(), set2.getEquipmentItems());
    }

    /**
     * Retrieves an armor set by its identifier.
     *
     * @param identifier The identifier of the armor set.
     * @return The armor set associated with the identifier, or the default armor set if not found.
     */
    public ArmorSet getArmorSetByIdentifier(String identifier) {
        return armorSetMap.getOrDefault(identifier, defaultArmorSet);
    }

    /**
     * Retrieves the index mapping items to armor sets.
     * <p>
     * Need to be implemented to optimize performance.
     *
     * @return A map of items to sets of armor sets.
     */
    public Map<Item, Set<ArmorSet>> getItemToSetIndex() {
        return itemToSetIndex;
    }

    /**
     * Retrieves the default armor set.
     *
     * @return The default armor set.
     */
    public ArmorSet getDefaultArmorSet() {
        return defaultArmorSet;
    }
}