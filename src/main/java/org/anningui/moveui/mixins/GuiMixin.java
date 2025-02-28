package org.anningui.moveui.mixins;

import net.minecraft.client.gui.Gui;
import org.anningui.moveui.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Gui.class, priority = -1)
public class GuiMixin {
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyHotbarX(int value) {
        return value + Config.HOT_BAR_X.get();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyHotbarY(int value) {
        return value + Config.HOT_BAR_Y.get();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"), index = 1)
    private int modifyItemX(int value) {
        return value + Config.HOT_BAR_X.get();
    }
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"), index = 2)
    private int modifyItemY(int value) {
        return value + Config.HOT_BAR_X.get();
    }
    @ModifyVariable(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At(value = "STORE"), ordinal = 1)
    private int modifyHeldItemTooltipX(int value) {
        return value + Config.TEXT_X.get();
    }
    @ModifyVariable(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At(value = "STORE"), ordinal = 2)
    private int modifyHeldItemTooltipY(int value) {
        return value + Config.TEXT_Y.get();
    }
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyExperienceBarX(int value) {
        return value + Config.EXP_X.get();
    }
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyExperienceBarY(int value) {
        return value - Config.EXP_Y.get();
    }
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"), index = 2)
    private int modifyXpDX(int value) {
        return value + Config.EXP_DX.get();
    }
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"), index = 3)
    private int modifyXpDY(int value) {
        return value - Config.EXP_DY.get();
    }
    @ModifyArg(method = "renderJumpMeter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyJumpX(int value) {
        return value + Config.EXP_X.get();
    }
    @ModifyArg(method = "renderJumpMeter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyJumpY(int value) {
        return value - Config.EXP_Y.get();
    }
    @ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index = 2)
    private int modifyHealthBarX(int value) {
        return value + Config.HEALTH_X.get();
    }
    @ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index = 3)
    private int modifyHealthBarY(int value) {
        return value - Config.HEALTH_Y.get();
    }
    @ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index = 4)
    private int modifyHealthSpacing(int value) {
        return value + Config.HEALTH_SPACING.get();
    }
    @ModifyArg(method = "renderVehicleHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyMountHealthX(int value) {
        return value + Config.HUNGER_X.get();
    }
    @ModifyArg(method = "renderVehicleHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyMountHealthY(int value) {
        return value - Config.HUNGER_Y.get();
    }
    @ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 1)
    private int modifyStatusBarsX(int value) {
        return value + Config.HUNGER_X.get();
    }
    @ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyStatusBarsY(int value) {
        return value - Config.HUNGER_Y.get();
    }
    @ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), index = 2)
    private int modifyStatusBarsSpacing(int value) {
        return value + Config.HUNGER_SPACING.get();
    }
}
