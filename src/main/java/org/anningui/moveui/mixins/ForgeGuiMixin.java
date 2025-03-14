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
import org.anningui.moveui.MoveUIStone;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
        return x + MoveUIStone.getHealthX();
    }

    @ModifyArg(
            method = "renderHealth",
            at = @At(value = "INVOKE",target = "Lnet/minecraftforge/client/gui/overlay/ForgeGui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"),
            index = 3
    )
    private int modifyHealthY(int y) {
        return y - MoveUIStone.getHealthY();
    }

    @ModifyArg(
            method = "renderFood",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 1
    )
    private int modifyHungerX(int x) {
        return x + MoveUIStone.getHungerX();
    }

    @ModifyArg(
            method = "renderFood",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"),
            index = 2
    )
    private int modifyHungerY(int y) {
        return y - MoveUIStone.getHungerY();
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
            if (!this.minecraft.gameMode.canHurtPlayer()) {
                k += 14;
            }

            int l = (int)((float)this.toolHighlightTimer * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }

            if (l > 0) {
                pGuiGraphics.fill(j - 2 + MoveUIStone.getTextX(), k - 2 - MoveUIStone.getTextY(), j + i + 2 + MoveUIStone.getTextX(), k + 9 + 2 - MoveUIStone.getTextY(), this.minecraft.options.getBackgroundColor(0));
                Font font = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(lastToolHighlight).getFont(lastToolHighlight, net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext.SELECTED_ITEM_NAME);
                if (font == null) {
                    pGuiGraphics.drawString(this.getFont(), highlightTip, j + MoveUIStone.getTextX(), k - MoveUIStone.getTextY(), 16777215 + (l << 24));
                } else {
                    j = (this.screenWidth - font.width(highlightTip)) / 2;
                    pGuiGraphics.drawString(font, highlightTip, j + MoveUIStone.getTextX(), k - MoveUIStone.getTextY(), 16777215 + (l << 24));
                }
            }
        }

        this.minecraft.getProfiler().pop();
    }
}
