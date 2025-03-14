package org.anningui.moveui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.anningui.moveui.MoveUI.kubejsIsOk;

@OnlyIn(Dist.CLIENT)
public class MoveUIStone {
    public enum MoveUIKeys {
        HOT_BAR_X,
        HOT_BAR_Y,
        TEXT_X,
        TEXT_Y,
        EXP_X ,
        EXP_Y ,
        EXP_DX,
        EXP_DY,
        HEALTH_X,
        HEALTH_Y,
        HEALTH_SPACING,
        HUNGER_X,
        HUNGER_Y,
        HUNGER_SPACING
    }

    // ConfigToKJSProxyMap
    public record CKPMMap(ForgeConfigSpec.IntValue key,Optional<Integer> value) {
        public static CKPMMap of(ForgeConfigSpec.IntValue key, Optional<Integer> value) {
            return new CKPMMap(key, value);
        }
    }

    private static final Map<MoveUIKeys, CKPMMap> kjs$configProxy = new HashMap<>() {{
        put(MoveUIKeys.HOT_BAR_X     , CKPMMap.of(Config.HOT_BAR_X     , Optional.empty()));
        put(MoveUIKeys.HOT_BAR_Y     , CKPMMap.of(Config.HOT_BAR_Y     , Optional.empty()));
        put(MoveUIKeys.TEXT_X        , CKPMMap.of(Config.TEXT_X        , Optional.empty()));
        put(MoveUIKeys.TEXT_Y        , CKPMMap.of(Config.TEXT_Y        , Optional.empty()));
        put(MoveUIKeys.EXP_X         , CKPMMap.of(Config.EXP_X         , Optional.empty()));
        put(MoveUIKeys.EXP_Y         , CKPMMap.of(Config.EXP_Y         , Optional.empty()));
        put(MoveUIKeys.EXP_DX        , CKPMMap.of(Config.EXP_DX        , Optional.empty()));
        put(MoveUIKeys.EXP_DY        , CKPMMap.of(Config.EXP_DY        , Optional.empty()));
        put(MoveUIKeys.HEALTH_X      , CKPMMap.of(Config.HEALTH_X      , Optional.empty()));
        put(MoveUIKeys.HEALTH_Y      , CKPMMap.of(Config.HEALTH_Y      , Optional.empty()));
        put(MoveUIKeys.HEALTH_SPACING, CKPMMap.of(Config.HEALTH_SPACING, Optional.empty()));
        put(MoveUIKeys.HUNGER_X      , CKPMMap.of(Config.HUNGER_X      , Optional.empty()));
        put(MoveUIKeys.HUNGER_Y      , CKPMMap.of(Config.HUNGER_Y      , Optional.empty()));
        put(MoveUIKeys.HUNGER_SPACING, CKPMMap.of(Config.HUNGER_SPACING, Optional.empty()));
    }};

    public static int get(MoveUIKeys key) {
        var value = kjs$configProxy.get(key);
        if (!isNull(value) && value.value().isPresent() && kubejsIsOk) {
            return value.value().get();
        } else {
            return value.key().get();
        }
    }

    public static void set(MoveUIKeys key, Integer value) {
        kjs$configProxy.put(key, CKPMMap.of(kjs$configProxy.get(key).key(), Optional.of(value)));
    }

    public static int getHotBarX() {
        return get(MoveUIKeys.HOT_BAR_X);
    }

    public static int getHotBarY() {
        return get(MoveUIKeys.HOT_BAR_Y);
    }

    public static int getTextX() {
        return get(MoveUIKeys.TEXT_X);
    }

    public static int getTextY() {
        return get(MoveUIKeys.TEXT_Y);
    }

    public static int getExpX() {
        return get(MoveUIKeys.EXP_X);
    }

    public static int getExpY() {
        return get(MoveUIKeys.EXP_Y);
    }

    public static int getExpDX() {
        return get(MoveUIKeys.EXP_DX);
    }

    public static int getExpDY() {
        return get(MoveUIKeys.EXP_DY);
    }

    public static int getHealthX() {
        return get(MoveUIKeys.HEALTH_X);
    }

    public static int getHealthY() {
        return get(MoveUIKeys.HEALTH_Y);
    }
    public static int getHealthSpacing() {
        return get(MoveUIKeys.HEALTH_SPACING);
    }

    public static int getHungerX() {
        return get(MoveUIKeys.HUNGER_X);
    }

    public static int getHungerY() {
        return get(MoveUIKeys.HUNGER_Y);
    }

    public static int getHungerSpacing() {
        return get(MoveUIKeys.HUNGER_SPACING);
    }

    public static void kjs$reload() {
        if (kubejsIsOk) {
            kjs$configProxy.replaceAll((k, v) -> CKPMMap.of(v.key(), Optional.empty()));
        }
    }
}
