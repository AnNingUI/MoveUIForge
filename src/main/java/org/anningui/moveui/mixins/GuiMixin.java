package org.anningui.moveui.mixins;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.anningui.moveui.MoveUIStore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = Gui.class)
public class GuiMixin {
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyHotbarX(int value) {
        return value + MoveUIStore.getHotBarX();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyHotbarY(int value) {
        return value - MoveUIStore.getHotBarY();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"), index = 1)
    private int modifyItemX(int value) {
        return value + MoveUIStore.getHotBarX();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"), index = 2)
    private int modifyItemY(int value) {
        return value - MoveUIStore.getHotBarY();
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void onRenderHotbar(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo ci) {
        if (MoveUIStore.getHotBarNoRender()) {
            ci.cancel();
        }
    }

    @Redirect(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"))
    private void onRenderExpBar(GuiGraphics instance, ResourceLocation pAtlasLocation, int pX, int pY, int pUOffset, int pVOffset, int pUWidth, int pVHeight) {
        if (MoveUIStore.getExpNoRender()) {
            return;
        }
        instance.blit(pAtlasLocation, pX + MoveUIStore.getExpX(), pY - MoveUIStore.getExpY(), pUOffset, pVOffset, pUWidth, pVHeight);
    }

    @Redirect(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
    private int onRenderExpLevel(GuiGraphics instance, Font pFont, String pText, int pX, int pY, int pColor, boolean pDropShadow) {
        if (MoveUIStore.getExpDNoRender()) {
            return 0;
        }
        return instance.drawString(pFont, pText, pX + MoveUIStore.getExpX(), pY - MoveUIStore.getExpY(), pColor, pDropShadow);
    }

    @ModifyArg(method = "renderJumpMeter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyJumpX(int value) {
        return value + MoveUIStore.getExpX();
    }
    @ModifyArg(method = "renderJumpMeter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyJumpY(int value) {
        return value - MoveUIStore.getExpY();
    }

    @Inject(method = "renderJumpMeter", at = @At("HEAD"), cancellable = true)
    private void onRenderExpBar(PlayerRideableJumping pRideable, GuiGraphics pGuiGraphics, int pX, CallbackInfo ci) {
        if (MoveUIStore.getExpNoRender()) {
            ci.cancel();
        }
    }
}
