package org.crychicteam.cibrary.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.checkerframework.checker.units.qual.A;
import org.crychicteam.cibrary.content.event.ItemDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "hurtAndBreak", at = @At("HEAD"), cancellable = true)
    public <T extends LivingEntity> void cibrary$cancelHurtAndBreak(int pAmount, T pEntity, Consumer<T> pOnBroken, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;
        ItemDamageEvent event = new ItemDamageEvent(stack, pAmount,  pEntity, pOnBroken);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "hurtAndBreak",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;damageItem(Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)I"
            ),
            remap = false
    )
    private int cibrary$redirectDamageItem(Item item, ItemStack stack, int pAmount, LivingEntity pEntity, Consumer<LivingEntity> pOnBroken) {
        var event = new ItemDamageEvent(stack, pAmount, pEntity, pOnBroken);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getDamage();
    }
}
