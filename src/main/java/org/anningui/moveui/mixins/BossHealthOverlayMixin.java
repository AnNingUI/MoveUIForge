package org.anningui.moveui.mixins;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import org.anningui.moveui.MoveUIStore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V"
            ),
            index = 1
    )
    private int modifyBossHealthBarX(int x) {
        return x + MoveUIStore.getBossHealthBarX();
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V"
            ),
            index = 2
    )
    private int modifyBossHealthBarY(int y) {
        return y - MoveUIStore.getBossHealthBarY();
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"
            ),
            index = 1
    )
    private Component modifyBossHealthText(Component text) {
        if (MoveUIStore.getBossHealthTextNoRender()) {
            return Component.empty();
        } else {
            return text;
        }
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"
            ),
            index = 2
    )
    private int modifyBossHealthTextX(int x) {
        return x + MoveUIStore.getBossHealthTextX();
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"
            ),
            index = 3
    )
    private int modifyBossHealthTextY(int y) {
        return y - MoveUIStore.getBossHealthTextY();
    }

    @Inject(
            method = "drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V",
            at = @At(value = "HEAD"), cancellable = true, remap = false
    )
    private void onDrawBar(GuiGraphics g, int x, int y, BossEvent bossEvent, CallbackInfo ci) {
        if (MoveUIStore.getBossHealthBarNoRender()) {
            ci.cancel();
        }
    }
}
