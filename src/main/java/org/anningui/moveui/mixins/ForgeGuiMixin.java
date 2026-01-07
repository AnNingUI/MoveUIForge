package org.anningui.moveui.mixins;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.anningui.moveui.MoveUIStore;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public abstract class ForgeGuiMixin extends Gui {
    protected ForgeGuiMixin(Minecraft pMinecraft, ItemRenderer pItemRenderer) {
        super(pMinecraft, pItemRenderer);
    }

    @ModifyArg(
            method = "renderHealth",
            at = @At(value = "INVOKE",target = "Lnet/minecraftforge/client/gui/overlay/ForgeGui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"),
            index = 2
    )
    private int modifyHealthX(int x) {
        return x + MoveUIStore.getHealthX();
    }

    @ModifyArg(
            method = "renderHealth",
            at = @At(value = "INVOKE",target = "Lnet/minecraftforge/client/gui/overlay/ForgeGui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"),
            index = 3
    )
    private int modifyHealthY(int y) {
        return y - MoveUIStore.getHealthY();
    }

    @Inject(method = "renderHealth", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onRenderHealth(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (MoveUIStore.getHealthNoRender()) {
             ci.cancel();
        }
    }

    @ModifyArg(
            method = "renderFood",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 1
    )
    private int modifyHungerX(int x) {
        return x + MoveUIStore.getHungerX();
    }

    @ModifyArg(
            method = "renderFood",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 2
    )
    private int modifyHungerY(int y) {
        return y - MoveUIStore.getHungerY();
    }

    @Inject(method = "renderFood", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onRenderFood(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (MoveUIStore.getHungerNoRender()) {
            ci.cancel();
        }
    }

    @ModifyArg(
            method = "renderArmor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 1
    )
    private int modifyArmorX(int x) {
        return x + MoveUIStore.getArmorX();
    }

    @ModifyArg(
            method = "renderArmor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 2
    )
    private int modifyArmorY(int y) {
        return y - MoveUIStore.getArmorY();
    }

    @Inject(method = "renderArmor", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onRenderArmor(GuiGraphics guiGraphics, int width, int height, CallbackInfo ci) {
        if (MoveUIStore.getArmorNoRender()) {
            ci.cancel();
        }
    }

    @ModifyArg(
            method = "renderAir",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 1
    )
    private int modifyAirX(int x) {
        return x + MoveUIStore.getAirX();
    }

    @ModifyArg(
            method = "renderAir",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 2
    )
    private int modifyAirY(int y) {
        return y - MoveUIStore.getAirY();
    }

    @Inject(method = "renderAir", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onRenderAir(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (MoveUIStore.getAirNoRender()) {
            ci.cancel();
        }
    }

    @Override
    public void renderSelectedItemName(@NotNull GuiGraphics pGuiGraphics, int yShift) {
        this.minecraft.getProfiler().push("selectedItemName");
        if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
            MutableComponent mutablecomponent = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().getStyleModifier());
            if (this.lastToolHighlight.hasCustomHoverName()) {
                mutablecomponent.withStyle(ChatFormatting.ITALIC);
            }

            Component highlightTip = this.lastToolHighlight.getHighlightTip(mutablecomponent);
            int i = this.getFont().width(highlightTip);
            int j = (this.screenWidth - i) / 2;
            int k = this.screenHeight - Math.max(yShift, 59);
            if (this.minecraft.gameMode != null && !this.minecraft.gameMode.canHurtPlayer()) {
                k += 14;
            }

            int l = (int)((float)this.toolHighlightTimer * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }

            if (l > 0) {
                if (!MoveUIStore.getTextNoRender()) {
                    pGuiGraphics.fill(j - 2 + MoveUIStore.getTextX(), k - 2 - MoveUIStore.getTextY(), j + i + 2 + MoveUIStore.getTextX(), k + 9 + 2 - MoveUIStore.getTextY(), this.minecraft.options.getBackgroundColor(0));
                }
                Font font = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(lastToolHighlight).getFont(lastToolHighlight, net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext.SELECTED_ITEM_NAME);
                if (font == null) {
                    if (!MoveUIStore.getTextNoRender()) {
                        pGuiGraphics.drawString(this.getFont(), highlightTip, j + MoveUIStore.getTextX(), k - MoveUIStore.getTextY(), 16777215 + (l << 24));
                    }
                } else {
                    j = (this.screenWidth - font.width(highlightTip)) / 2;
                    if (!MoveUIStore.getTextNoRender()) {
                        pGuiGraphics.drawString(font, highlightTip, j + MoveUIStore.getTextX(), k - MoveUIStore.getTextY(), 16777215 + (l << 24));
                    }
                }
            }
        }

        this.minecraft.getProfiler().pop();
    }



    @ModifyArg(
            method = "renderHealthMount",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 1
    )
    private int modifyMountHealthX(int x) {
        return x + MoveUIStore.getMountHealthX();
    }

    @ModifyArg(
            method = "renderHealthMount",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 2
    )
    private int modifyMountHealthY(int y) {
        return y - MoveUIStore.getMountHealthY();
    }

    @Inject(method = "renderHealthMount", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onRenderMountHealth(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (MoveUIStore.getMountHealthNoRender()) {
            ci.cancel();
        }
    }
}
