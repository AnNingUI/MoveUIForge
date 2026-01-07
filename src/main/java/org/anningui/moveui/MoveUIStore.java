package org.anningui.moveui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.anningui.moveui.MoveUI.kubejsIsOk;

@OnlyIn(Dist.CLIENT)
public class MoveUIStore {
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
        HUNGER_SPACING,
        ARMOR_X,
        ARMOR_Y,
        AIR_X,
        AIR_Y,
        MOUNT_HEALTH_X,
        MOUNT_HEALTH_Y,
        BOSS_HEALTH_BAR_X,
        BOSS_HEALTH_BAR_Y,
        BOSS_HEALTH_TEXT_X,
        BOSS_HEALTH_TEXT_Y,

        HOT_BAR_NO_RENDER,
        TEXT_NO_RENDER,
        EXP_NO_RENDER,
        EXP_D_NO_RENDER,
        HEALTH_NO_RENDER,
        HUNGER_NO_RENDER,
        ARMOR_NO_RENDER,
        AIR_NO_RENDER,
        MOUNT_HEALTH_NO_RENDER,
        BOSS_HEALTH_BAR_NO_RENDER,
        BOSS_HEALTH_TEXT_NO_RENDER
    }

    // ConfigToKJSProxyMap
    public record CKPMMap<T>(ForgeConfigSpec.ConfigValue<T> key, Optional<T> value) {
        public static <T> CKPMMap<T> of(ForgeConfigSpec.ConfigValue<T> key, Optional<T> value) {
            return new CKPMMap<T>(key, value);
        }
    }

    private static final Map<MoveUIKeys, CKPMMap> kjs$configProxy = new HashMap<>() {{
        put(MoveUIKeys.HOT_BAR_X                 , CKPMMap.of(Config.HOT_BAR_X                   , Optional.empty()));
        put(MoveUIKeys.HOT_BAR_Y                 , CKPMMap.of(Config.HOT_BAR_Y                   , Optional.empty()));
        put(MoveUIKeys.TEXT_X                    , CKPMMap.of(Config.TEXT_X                      , Optional.empty()));
        put(MoveUIKeys.TEXT_Y                    , CKPMMap.of(Config.TEXT_Y                      , Optional.empty()));
        put(MoveUIKeys.EXP_X                     , CKPMMap.of(Config.EXP_X                       , Optional.empty()));
        put(MoveUIKeys.EXP_Y                     , CKPMMap.of(Config.EXP_Y                       , Optional.empty()));
        put(MoveUIKeys.EXP_DX                    , CKPMMap.of(Config.EXP_DX                      , Optional.empty()));
        put(MoveUIKeys.EXP_DY                    , CKPMMap.of(Config.EXP_DY                      , Optional.empty()));
        put(MoveUIKeys.HEALTH_X                  , CKPMMap.of(Config.HEALTH_X                    , Optional.empty()));
        put(MoveUIKeys.HEALTH_Y                  , CKPMMap.of(Config.HEALTH_Y                    , Optional.empty()));
        put(MoveUIKeys.HEALTH_SPACING            , CKPMMap.of(Config.HEALTH_SPACING              , Optional.empty()));
        put(MoveUIKeys.HUNGER_X                  , CKPMMap.of(Config.HUNGER_X                    , Optional.empty()));
        put(MoveUIKeys.HUNGER_Y                  , CKPMMap.of(Config.HUNGER_Y                    , Optional.empty()));
        put(MoveUIKeys.HUNGER_SPACING            , CKPMMap.of(Config.HUNGER_SPACING              , Optional.empty()));
        put(MoveUIKeys.ARMOR_X                   , CKPMMap.of(Config.ARMOR_X                     , Optional.empty()));
        put(MoveUIKeys.ARMOR_Y                   , CKPMMap.of(Config.ARMOR_Y                     , Optional.empty()));
        put(MoveUIKeys.AIR_X                     , CKPMMap.of(Config.AIR_X                       , Optional.empty()));
        put(MoveUIKeys.AIR_Y                     , CKPMMap.of(Config.AIR_Y                       , Optional.empty()));
        put(MoveUIKeys.MOUNT_HEALTH_X            , CKPMMap.of(Config.MOUNT_HEALTH_X              , Optional.empty()));
        put(MoveUIKeys.MOUNT_HEALTH_Y            , CKPMMap.of(Config.MOUNT_HEALTH_Y              , Optional.empty()));
        put(MoveUIKeys.BOSS_HEALTH_BAR_X         , CKPMMap.of(Config.BOSS_HEALTH_BAR_X           , Optional.empty()));
        put(MoveUIKeys.BOSS_HEALTH_BAR_Y         , CKPMMap.of(Config.BOSS_HEALTH_BAR_Y           , Optional.empty()));
        put(MoveUIKeys.BOSS_HEALTH_TEXT_X        , CKPMMap.of(Config.BOSS_HEALTH_TEXT_X          , Optional.empty()));
        put(MoveUIKeys.BOSS_HEALTH_TEXT_Y        , CKPMMap.of(Config.BOSS_HEALTH_TEXT_Y          , Optional.empty()));
        put(MoveUIKeys.HOT_BAR_NO_RENDER         , CKPMMap.of(Config.NO_RENDERER.HOT_BAR         , Optional.empty()));
        put(MoveUIKeys.TEXT_NO_RENDER            , CKPMMap.of(Config.NO_RENDERER.TEXT            , Optional.empty()));
        put(MoveUIKeys.EXP_NO_RENDER             , CKPMMap.of(Config.NO_RENDERER.EXP             , Optional.empty()));
        put(MoveUIKeys.EXP_D_NO_RENDER           , CKPMMap.of(Config.NO_RENDERER.EXP_D           , Optional.empty()));
        put(MoveUIKeys.HEALTH_NO_RENDER          , CKPMMap.of(Config.NO_RENDERER.HEALTH          , Optional.empty()));
        put(MoveUIKeys.HUNGER_NO_RENDER          , CKPMMap.of(Config.NO_RENDERER.HUNGER          , Optional.empty()));
        put(MoveUIKeys.ARMOR_NO_RENDER           , CKPMMap.of(Config.NO_RENDERER.ARMOR           , Optional.empty()));
        put(MoveUIKeys.AIR_NO_RENDER             , CKPMMap.of(Config.NO_RENDERER.AIR             , Optional.empty()));
        put(MoveUIKeys.MOUNT_HEALTH_NO_RENDER    , CKPMMap.of(Config.NO_RENDERER.MOUNT_HEALTH    , Optional.empty()));
        put(MoveUIKeys.BOSS_HEALTH_BAR_NO_RENDER , CKPMMap.of(Config.NO_RENDERER.BOSS_HEALTH_BAR , Optional.empty()));
        put(MoveUIKeys.BOSS_HEALTH_TEXT_NO_RENDER, CKPMMap.of(Config.NO_RENDERER.BOSS_HEALTH_TEXT, Optional.empty()));
    }};

    public static Object get(MoveUIKeys key) {
        var value = kjs$configProxy.get(key);
        if (!isNull(value) && value.value().isPresent() && kubejsIsOk) {
            return value.value().get();
        } else {
            return value.key().get();
        }
    }

    public static <T> T get(MoveUIKeys key, Class<T> type) {
        Object value = get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            throw new IllegalArgumentException("Value for key " + key + " is not of type " + type);
        }
    }

    public static int getInt(MoveUIKeys key) {
        return get(key, Integer.class);
    }

    public static boolean getBoolean(MoveUIKeys key) {
        return get(key, Boolean.class);
    }

    public static <T> void set(MoveUIKeys key, T value) {
        kjs$configProxy.put(key, CKPMMap.of(kjs$configProxy.get(key).key(), Optional.of(value)));
    }

    public static int getHotBarX() {
        return getInt(MoveUIKeys.HOT_BAR_X);
    }

    public static int getHotBarY() {
        return getInt(MoveUIKeys.HOT_BAR_Y);
    }

    public static int getTextX() {
        return getInt(MoveUIKeys.TEXT_X);
    }

    public static int getTextY() {
        return getInt(MoveUIKeys.TEXT_Y);
    }

    public static int getExpX() {
        return getInt(MoveUIKeys.EXP_X);
    }

    public static int getExpY() {
        return getInt(MoveUIKeys.EXP_Y);
    }

    public static int getExpDX() {
        return getInt(MoveUIKeys.EXP_DX);
    }

    public static int getExpDY() {
        return getInt(MoveUIKeys.EXP_DY);
    }

    public static int getHealthX() {
        return getInt(MoveUIKeys.HEALTH_X);
    }

    public static int getHealthY() {
        return getInt(MoveUIKeys.HEALTH_Y);
    }
    public static int getHealthSpacing() {
        return getInt(MoveUIKeys.HEALTH_SPACING);
    }

    public static int getHungerX() {
        return getInt(MoveUIKeys.HUNGER_X);
    }

    public static int getHungerY() {
        return getInt(MoveUIKeys.HUNGER_Y);
    }

    public static int getHungerSpacing() {
        return getInt(MoveUIKeys.HUNGER_SPACING);
    }

    public static int getArmorX() {
        return getInt(MoveUIKeys.ARMOR_X);
    }

    public static int getArmorY() {
        return getInt(MoveUIKeys.ARMOR_Y);
    }

    public static int getAirX() {
        return getInt(MoveUIKeys.AIR_X);
    }

    public static int getAirY() {
        return getInt(MoveUIKeys.AIR_Y);
    }

    public static int getMountHealthX() { return getInt(MoveUIKeys.MOUNT_HEALTH_X); }

    public static int getMountHealthY() { return getInt(MoveUIKeys.MOUNT_HEALTH_Y); }

    public static int getBossHealthBarX() {
        return getInt(MoveUIKeys.BOSS_HEALTH_BAR_X);
    }

    public static int getBossHealthBarY() {
        return getInt(MoveUIKeys.BOSS_HEALTH_BAR_Y);
    }

    public static int getBossHealthTextX() {
        return getInt(MoveUIKeys.BOSS_HEALTH_TEXT_X);
    }

    public static int getBossHealthTextY() {
        return getInt(MoveUIKeys.BOSS_HEALTH_TEXT_Y);
    }

    public static boolean getHotBarNoRender() {
        return getBoolean(MoveUIKeys.HOT_BAR_NO_RENDER);
    }

    public static boolean getTextNoRender() {
        return getBoolean(MoveUIKeys.TEXT_NO_RENDER);
    }

    public static boolean getExpNoRender() {
        return getBoolean(MoveUIKeys.EXP_NO_RENDER);
    }

    public static boolean getExpDNoRender() {
        return getBoolean(MoveUIKeys.EXP_D_NO_RENDER);
    }

    public static boolean getHealthNoRender() {
        return getBoolean(MoveUIKeys.HEALTH_NO_RENDER);
    }

    public static boolean getHungerNoRender() {
        return getBoolean(MoveUIKeys.HUNGER_NO_RENDER);
    }

    public static boolean getArmorNoRender() {
        return getBoolean(MoveUIKeys.ARMOR_NO_RENDER);
    }

    public static boolean getAirNoRender() {
        return getBoolean(MoveUIKeys.AIR_NO_RENDER);
    }

    public static boolean getMountHealthNoRender() { return getBoolean(MoveUIKeys.MOUNT_HEALTH_NO_RENDER); }

    public static boolean getBossHealthBarNoRender() {
        return getBoolean(MoveUIKeys.BOSS_HEALTH_BAR_NO_RENDER);
    }

    public static boolean getBossHealthTextNoRender() {
        return getBoolean(MoveUIKeys.BOSS_HEALTH_TEXT_NO_RENDER);
    }

    public static void kjs$reload() {
        if (kubejsIsOk && Minecraft.getInstance().level != null) {
            kjs$configProxy.replaceAll((k, v) -> CKPMMap.of(v.key(), Optional.empty()));
        }
    }
}
