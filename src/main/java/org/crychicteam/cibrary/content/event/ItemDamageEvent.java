package org.crychicteam.cibrary.content.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

@Cancelable
public class ItemDamageEvent<T extends LivingEntity> extends Event {
    private final ItemStack itemStack;
    private int damage;
    private final T entity;
    private final Consumer<T> onBroken;

    public ItemDamageEvent(ItemStack itemStack, int damage, T entity, Consumer<T> onBroken) {
        this.itemStack = itemStack;
        this.damage = damage;
        this.entity = entity;
        this.onBroken = onBroken;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public T getEntity() {
        return entity;
    }

    public Consumer<T> getOnBroken() {
        return onBroken;
    }
}
