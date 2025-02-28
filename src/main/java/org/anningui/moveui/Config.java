package org.anningui.moveui;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.IntValue HOT_BAR_X = BUILDER.comment("X position of the hotbar").defineInRange("hotbarX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue HOT_BAR_Y = BUILDER.comment("Y position of the hotbar").defineInRange("hotbarY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue TEXT_X = BUILDER.comment("X position of the text").defineInRange("textX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue TEXT_Y = BUILDER.comment("Y position of the text").defineInRange("textY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue EXP_X = BUILDER.comment("X position of the experience bar").defineInRange("expX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue EXP_Y = BUILDER.comment("Y position of the experience bar").defineInRange("expY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue EXP_DX = BUILDER.comment("X offset of the experience bar").defineInRange("expDX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue EXP_DY = BUILDER.comment("Y offset of the experience bar").defineInRange("expDY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue HEALTH_X = BUILDER.comment("X position of the health bar").defineInRange("healthX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue HEALTH_Y = BUILDER.comment("Y position of the health bar").defineInRange("healthY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue HEALTH_SPACING = BUILDER.comment("Spacing between health icons").defineInRange("healthSpacing", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue HUNGER_X = BUILDER.comment("X position of the hunger bar").defineInRange("hungerX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue HUNGER_Y = BUILDER.comment("Y position of the hunger bar").defineInRange("hungerY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue HUNGER_SPACING = BUILDER.comment("Spacing between hunger icons").defineInRange("hungerSpacing", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
