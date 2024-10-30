package org.crychicteam.cibrary.content.armorset;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.defaults.DefaultSetEffect;
import org.crychicteam.cibrary.content.armorset.integration.CuriosIntegration;

import java.util.*;

/**
 * Armor Set Class.
 * <p>
 * The logic of Updates. Damage and Checks are achieved by interfaces' default methods.
 * <p>
 * Only getters and setters are contained in this class except for matches method.
 */
public class ArmorSet implements IArmorSetUpdater, IArmorSetAttackHandler, IArmorSetChecker {
    public static final Item EMPTY_SLOT_MARKER = null;
    public static final ResourceKey<? extends Registry<ArmorSet>> ARMOR_SET = ResourceKey.createRegistryKey(new ResourceLocation("armor_set"));
    private final Map<MobEffect, Integer> effects;
    private final Multimap<Attribute, AttributeModifier> attributes;
    protected ISetEffect effect;
    private final Map<EquipmentSlot, Set<Item>> equipmentItems;
    private final Map<Item, Integer> curioItems;
    protected State state;
    protected String skillState;

    /**
     * Enum representing the state of the armor set.
     */
    public enum State {
        NORMAL,
        ACTIVE,
        INACTIVE,
        CURSED
    }

    /**
     * Constructs an ArmorSet with the specified identifier and effect.
     *
     * @param effect The effect associated with the armor set.
     */
    public ArmorSet(ISetEffect effect) {
        this.effect = effect;
        this.effects = new HashMap<>();
        this.attributes = HashMultimap.create();
        this.equipmentItems = new EnumMap<>(EquipmentSlot.class);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.equipmentItems.put(slot, new HashSet<>());
        }
        this.curioItems = new HashMap<>();
        this.state = State.NORMAL;
        this.skillState = "none";
    }

    /**
     * Constructs an ArmorSet with a default effect.
     *
     */
    public ArmorSet() {
        this(new DefaultSetEffect());
    }

//    public ResourceKey<ArmorSet> getSetKey() {
//        return
//    }

    /**
     * Gets the effects of the armor set.
     *
     * @return An unmodifiable map of effects.
     */
    public Map<MobEffect, Integer> getEffects() {
        return Collections.unmodifiableMap(effects);
    }

    /**
     * Gets the attributes of the armor set.
     *
     * @return A multimap of attributes.
     */
    public Multimap<Attribute, AttributeModifier> getAttributes() {
        return attributes;
    }

    /**
     * Gets the effect of the armor set.
     *
     * @return The effect of the armor set.
     */
    public ISetEffect getEffect() {
        return effect;
    }

    /**
     * Gets the state of the armor set.
     *
     * @return The state of the armor set.
     */
    public State getState() {
        return state;
    }

    /**
     * Gets the skill state of the armor set.
     *
     * @return The skill state of the armor set.
     */
    public String getSkillState() {
        return skillState;
    }

    /**
     * Gets the equipment items of the armor set.
     *
     * @return An unmodifiable map of equipment items.
     */
    public Map<EquipmentSlot, Set<Item>> getEquipmentItems() {
        return Collections.unmodifiableMap(equipmentItems);
    }

    /**
     * Gets the curio items of the armor set.
     *
     * @return An unmodifiable map of curio items.
     */
    public Map<Item, Integer> getCurioItems() {
        return Collections.unmodifiableMap(curioItems);
    }

    // Setters
    /**
     * Sets the state of the armor set.
     *
     * @param state The new state of the armor set.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Sets the skill state of the armor set.
     *
     * @param skillState The new skill state of the armor set.
     */
    public void setSkillState(String skillState) {
        this.skillState = skillState;
    }

    /**
     * Sets the effect of the armor set.
     *
     * @param effect The new effect of the armor set.
     */
    public void setEffect(ISetEffect effect) {
        this.effect = effect;
    }

    // Add methods

    /**
     * Adds an equipment item to the armor set.
     *
     * @param slot The equipment slot.
     * @param item The item to add.
     */
    public void addEquipmentItem(EquipmentSlot slot, Item item) {
        if (item == Items.AIR) {
            equipmentItems.get(slot).clear();
            equipmentItems.get(slot).add(EMPTY_SLOT_MARKER);
        } else {
            equipmentItems.get(slot).add(item);
        }
    }

    /**
     * Adds a curio item to the armor set.
     *
     * @param item The curio item to add.
     * @param count The count of the curio item.
     */
    public void addCurioItem(Item item, int count) {
        curioItems.put(item, count);
    }

    /**
     * Adds an effect to the armor set.
     *
     * @param effect The effect to add.
     * @param amplifier The amplifier of the effect.
     */
    public void addEffect(MobEffect effect, int amplifier) {
        effects.put(effect, amplifier);
    }

    /**
     * Adds an attribute to the armor set.
     *
     * @param attribute The attribute to add.
     * @param name The name of the attribute modifier.
     * @param amount The amount of the attribute modifier.
     * @param operation The operation of the attribute modifier.
     */
    public void addAttribute(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), name, amount, operation);
        attributes.put(attribute, modifier);
    }

    /**
     * Checks if the armor set matches the given entity.
     *
     * @param entity The entity to check.
     * @return True if the armor set matches the entity, false otherwise.
     */
    @Override
    public boolean matches(LivingEntity entity) {
        for (Map.Entry<EquipmentSlot, Set<Item>> entry : equipmentItems.entrySet()) {
            ItemStack equippedItem = entity.getItemBySlot(entry.getKey());
            if (entry.getValue().contains(EMPTY_SLOT_MARKER)) {
                if (!equippedItem.isEmpty()) {
                    return false;
                }
            } else if (!entry.getValue().isEmpty() && (equippedItem.isEmpty() || !entry.getValue().contains(equippedItem.getItem()))) {
                return false;
            }
        }
        return CuriosIntegration.matchesCurioRequirements(entity, curioItems);
    }
}