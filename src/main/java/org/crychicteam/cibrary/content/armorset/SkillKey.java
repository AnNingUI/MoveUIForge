package org.crychicteam.cibrary.content.armorset;

import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.key.KeyConfig;
import org.crychicteam.cibrary.content.key.KeyRegistry;
import org.lwjgl.glfw.GLFW;

public class SkillKey {
    public static KeyConfig SKILL_KEY;
    public static void init() {
        SKILL_KEY = KeyRegistry.register(new KeyConfig.Builder(Cibrary.MOD_ID, "armor_set_skill", GLFW.GLFW_KEY_G)
                .category("armor_set")
                .enableCharging()
                .maxPower(4.0f)
                .build()
        );
    }
}