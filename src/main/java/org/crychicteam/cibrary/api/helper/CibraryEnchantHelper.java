package org.crychicteam.cibrary.api.helper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class CibraryEnchantHelper {

    /**
     * Randomly enchants all of a living entity's equipment.
     * Iterates through each equipment slot and applies enchantments if the slot has an item.
     *
     * @param entity              The living entity whose equipment will be enchanted.
     * @param enchantmentPredicate Filter to determine which enchantments are allowed.
     * @param maxEnchantsPerSession The maximum number of enchantments to apply per item.
     * @param random              The random source used for randomizing enchantments.
     */
    public static void randomEnchantLivingEntityEquipment(LivingEntity entity,
                                                          Predicate<Enchantment> enchantmentPredicate,
                                                          int maxEnchantsPerSession,
                                                          RandomSource random) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack item = entity.getItemBySlot(slot);
            if (!item.isEmpty()) {
                addRandomEnchantments(item, enchantmentPredicate, maxEnchantsPerSession, random);
            }
        }
    }

    /**
     * Adds random enchantments to an ItemStack, considering existing enchantments for compatibility.
     * Filters the available enchantments based on compatibility and applies selected ones up to
     * the specified maximum.
     *
     * @param stack               The item to enchant.
     * @param enchantmentPredicate Predicate to filter enchantments that can be applied.
     * @param maxEnchantsPerSession Maximum number of enchantments to add per session.
     * @param random              The random source for selecting enchantments.
     */
    public static void addRandomEnchantments(ItemStack stack,
                                             Predicate<Enchantment> enchantmentPredicate,
                                             int maxEnchantsPerSession,
                                             RandomSource random) {
        Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(stack);
        List<EnchantmentInstance> availableEnchantments = getAvailableEnchantmentResults(30, stack, true);
        availableEnchantments.removeIf(instance -> !enchantmentPredicate.test(instance.enchantment) ||
                !isCompatibleWithExisting(instance.enchantment, existingEnchantments));

        int enchantmentsAdded = 0;
        while (enchantmentsAdded < maxEnchantsPerSession && !availableEnchantments.isEmpty()) {
            Optional<EnchantmentInstance> selectedEnchantment = WeightedRandom.getRandomItem(random, availableEnchantments);
            if (selectedEnchantment.isPresent()) {
                EnchantmentInstance instance = selectedEnchantment.get();
                int level = random.nextInt(instance.enchantment.getMaxLevel()) + 1;
                addOrUpdateEnchantment(stack, instance.enchantment, level);
                enchantmentsAdded++;
                EnchantmentHelper.filterCompatibleEnchantments(availableEnchantments, instance);
            } else {
                break;
            }
        }
    }

    /**
     * Adds a new enchantment or updates an existing one on an ItemStack, respecting max levels.
     *
     * @param stack      The item stack to enchant.
     * @param enchantment The enchantment to add or update.
     * @param newLevel   The level of the enchantment to add.
     */
    private static void addOrUpdateEnchantment(ItemStack stack, Enchantment enchantment, int newLevel) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        int existingLevel = enchantments.getOrDefault(enchantment, 0);
        int finalLevel = Math.min(enchantment.getMaxLevel(), existingLevel + newLevel);
        enchantments.put(enchantment, finalLevel);
        EnchantmentHelper.setEnchantments(enchantments, stack);
    }

    /**
     * Checks if a given enchantment is compatible with a set of existing enchantments.
     *
     * @param enchantment           The new enchantment to check for compatibility.
     * @param existingEnchantments  Map of existing enchantments on the item.
     * @return True if the new enchantment is compatible with existing ones, false otherwise.
     */
    private static boolean isCompatibleWithExisting(Enchantment enchantment, Map<Enchantment, Integer> existingEnchantments) {
        return EnchantmentHelper.isEnchantmentCompatible(existingEnchantments.keySet(), enchantment);
    }

    /**
     * Retrieves a list of applicable enchantments for a given ItemStack, based on an additional filter predicate.
     *
     * @param stack               The item stack to check for applicable enchantments.
     * @param additionalPredicate Additional condition to apply on enchantments.
     * @return A list of enchantments that can be applied to the item stack.
     */
    public static List<Enchantment> getApplicableEnchantments(ItemStack stack, Predicate<Enchantment> additionalPredicate) {
        List<Enchantment> applicableEnchantments = new ArrayList<>();
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            if (enchantment.canEnchant(stack) && additionalPredicate.test(enchantment)) {
                applicableEnchantments.add(enchantment);
            }
        }
        return applicableEnchantments;
    }

    /**
     * Calculates the total enchantment level for all enchanted items a living entity has equipped.
     *
     * @param entity The living entity whose equipment is checked for enchantments.
     * @return The combined level of all enchantments on the entity's equipment.
     */
    public static int getTotalEnchantmentLevelForEntity(LivingEntity entity) {
        int totalLevel = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack item = entity.getItemBySlot(slot);
            if (!item.isEmpty()) {
                totalLevel += getTotalEnchantmentLevel(item);
            }
        }
        return totalLevel;
    }

    /**
     * Retrieves the sum of enchantment levels on a specific item stack.
     *
     * @param stack The item stack to check for enchantments.
     * @return The total level of all enchantments on the item stack.
     */
    public static int getTotalEnchantmentLevel(ItemStack stack) {
        return EnchantmentHelper.getEnchantments(stack).values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Retrieves a list of potential enchantments that can be applied to an item stack.
     *
     * @param level          The enchanting level to use for generating enchantments.
     * @param stack          The item stack to be enchanted.
     * @param allowTreasure  Whether to include treasure enchantments.
     * @return A list of EnchantmentInstance objects representing possible enchantments.
     */
    private static List<EnchantmentInstance> getAvailableEnchantmentResults(int level, ItemStack stack, boolean allowTreasure) {
        return EnchantmentHelper.getAvailableEnchantmentResults(level, stack, allowTreasure);
    }
}
