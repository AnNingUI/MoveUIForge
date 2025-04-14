package org.anningui.moveui;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // Position settings
    public static final ForgeConfigSpec.IntValue HOT_BAR_X = BUILDER.comment("X position of the hotbar").defineInRange("hotbarX", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue HOT_BAR_Y = BUILDER.comment("Y position of the hotbar").defineInRange("hotbarY", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue TEXT_X = BUILDER.comment("X position of the text").defineInRange("textX", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue TEXT_Y = BUILDER.comment("Y position of the text").defineInRange("textY", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue EXP_X = BUILDER.comment("X position of the experience bar").defineInRange("expX", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue EXP_Y = BUILDER.comment("Y position of the experience bar").defineInRange("expY", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue EXP_DX = BUILDER.comment("X offset of the experience bar").defineInRange("expDX", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue EXP_DY = BUILDER.comment("Y offset of the experience bar").defineInRange("expDY", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue HEALTH_X = BUILDER.comment("X position of the health bar").defineInRange("healthX", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue HEALTH_Y = BUILDER.comment("Y position of the health bar").defineInRange("healthY", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue HEALTH_SPACING = BUILDER.comment("Spacing between health icons").defineInRange("healthSpacing", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue HUNGER_X = BUILDER.comment("X position of the hunger bar").defineInRange("hungerX", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue HUNGER_Y = BUILDER.comment("Y position of the hunger bar").defineInRange("hungerY", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue HUNGER_SPACING = BUILDER.comment("Spacing between hunger icons").defineInRange("hungerSpacing", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue ARMOR_X = BUILDER.comment("X position of the armor bar").defineInRange("armorX", 0, -5000, 5000);
    public static final ForgeConfigSpec.IntValue ARMOR_Y = BUILDER.comment("Y position of the armor bar").defineInRange("armorY", 0, -5000, 5000);

    // NoRenderer settings
    public static final NoRenderer NO_RENDERER = new NoRenderer(BUILDER);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static class NoRenderer {
        public final ForgeConfigSpec.BooleanValue HOT_BAR;
        public final ForgeConfigSpec.BooleanValue TEXT;
        public final ForgeConfigSpec.BooleanValue EXP;
        public final ForgeConfigSpec.BooleanValue EXP_D;
        public final ForgeConfigSpec.BooleanValue HEALTH;
        public final ForgeConfigSpec.BooleanValue HUNGER;
        public final ForgeConfigSpec.BooleanValue ARMOR;

        public NoRenderer(ForgeConfigSpec.Builder builder) {
            builder.push("noRenderer");
            HOT_BAR = builder.comment("Disable rendering of the hotbar").define("hotbarNoRenderer", false);
            TEXT = builder.comment("Disable rendering of the text").define("textNoRenderer", false);
            EXP = builder.comment("Disable rendering of the experience bar").define("expNoRenderer", false);
            EXP_D = builder.comment("Disable rendering of the experience text bar").define("expDNoRenderer", false);
            HEALTH = builder.comment("Disable rendering of the health bar").define("healthNoRenderer", false);
            HUNGER = builder.comment("Disable rendering of the hunger bar").define("hungerNoRenderer", false);
            ARMOR = builder.comment("Disable rendering of the armor bar").define("armorNoRenderer", false);
            builder.pop();
        }
    }
}
