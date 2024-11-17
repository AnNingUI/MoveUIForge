package org.crychicteam.cibrary.content.key;

import net.minecraft.client.Minecraft;

public class DefaultKey {
    public static final KeyConfig FORWARD_KEY;
    public static final KeyConfig BACKWARD_KEY;
    public static final KeyConfig LEFT_KEY;
    public static final KeyConfig RIGHT_KEY;
    public static final KeyConfig ATTACK_KEY;

    static {
        FORWARD_KEY = KeyRegistry.register(new KeyConfig.Builder("minecraft", "forward", Minecraft.getInstance().options.keyUp)
                        .build());
        BACKWARD_KEY = KeyRegistry.register(new KeyConfig.Builder("minecraft", "backward", Minecraft.getInstance().options.keyDown)
                .build());
        LEFT_KEY = KeyRegistry.register(new KeyConfig.Builder("minecraft", "left", Minecraft.getInstance().options.keyLeft)
                .build());
        RIGHT_KEY = KeyRegistry.register(new KeyConfig.Builder("minecraft", "right", Minecraft.getInstance().options.keyRight)
                .build());
        ATTACK_KEY = KeyRegistry.register(new KeyConfig.Builder("minecraft", "attack", Minecraft.getInstance().options.keyAttack)
                .build());
    }

    public static void register() {}
}