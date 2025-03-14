package org.anningui.moveui.mixins;

import net.minecraft.client.gui.Gui;
import org.anningui.moveui.MoveUIStone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(value = Gui.class)
public class GuiMixin {
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyHotbarX(int value) {
        return value + MoveUIStone.getHotBarX();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyHotbarY(int value) {
        return value - MoveUIStone.getHotBarY();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"), index = 1)
    private int modifyItemX(int value) {
        return value + MoveUIStone.getHotBarX();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"), index = 2)
    private int modifyItemY(int value) {
        return value - MoveUIStone.getHotBarY();
    }
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyExperienceBarX(int value) {
        return value + MoveUIStone.getExpX();
    }
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyExperienceBarY(int value) {
        return value - MoveUIStone.getExpY();
    }
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"), index = 2)
    private int modifyXpDX(int value) {
        return value + MoveUIStone.getExpDX();
    }
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"), index = 3)
    private int modifyXpDY(int value) {
        return value - MoveUIStone.getExpDY();
    }
    @ModifyArg(method = "renderJumpMeter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyJumpX(int value) {
        return value + MoveUIStone.getExpX();
    }
    @ModifyArg(method = "renderJumpMeter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyJumpY(int value) {
        return value - MoveUIStone.getExpY();
    }
}
