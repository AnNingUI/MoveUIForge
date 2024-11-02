package org.crychicteam.cibrary.content.key;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.settings.KeyConflictContext;
import com.mojang.blaze3d.platform.InputConstants;
import org.crychicteam.cibrary.content.events.client.ClientKeyHandler;

public class KeyConfig {
    public final ResourceLocation id;
    public final KeyMapping keyMapping;
    public final float maxPower;
    public final long doubleClickTime;
    public final boolean enableCharging;
    public final boolean enableDoubleClick;
    public final boolean showDebugMessage;

    private KeyConfig(Builder builder) {
        this.id = builder.id;
        this.keyMapping = new KeyMapping(
                "key." + builder.id.getNamespace() + "." + builder.id.getPath(),
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                builder.defaultKey,
                "key.category." + builder.id.getNamespace() + "." + builder.category
        );
        this.maxPower = builder.maxPower;
        this.doubleClickTime = builder.doubleClickTime;
        this.enableCharging = builder.enableCharging;
        this.enableDoubleClick = builder.enableDoubleClick;
        this.showDebugMessage = builder.showDebugMessage;
    }

    public static class Builder {
        private final ResourceLocation id;
        private final int defaultKey;
        private String category = "default";
        private float maxPower = 2.0f;
        private long doubleClickTime = 500;
        private boolean enableCharging = false;
        private boolean enableDoubleClick = false;
        private boolean showDebugMessage = false;

        public Builder(String modid, String keyId, int defaultKey) {
            this.id = new ResourceLocation(modid, keyId);
            this.defaultKey = defaultKey;
        }

        /**
         * set the category of this key.
         * <p/>
         * Showed on Vanilla Key Editor.
         * <p/>
         * default: "default"
         * @param category the category
         * @return {@link Builder}
         */
        public Builder category(String category) {
            this.category = category;
            return this;
        }

        /**
         * set the max power of charging.
         * @param maxPower >= 1.0f
         * @return {@link Builder}
         */
        public Builder maxPower(float maxPower) {
            this.maxPower = maxPower;
            return this;
        }

        /**
         * set the pending time to handle key press.
         * <p/>
         * Normally don't need to change.
         * <p/>
         * Allow you to set a number lower than {@link ClientKeyHandler#DOUBLE_CLICK_TIME}
         * @param doubleClickTime <= 200
         * @return {@link Builder}
         */
        public Builder doubleClickTime(long doubleClickTime) {
            this.doubleClickTime = doubleClickTime;
            return this;
        }

        /**
         * Whether to track charging for this key.
         * @return {@link Builder}
         */
        public Builder enableCharging() {
            this.enableCharging = true;
            return this;
        }

        /**
         * Whether to handle double click for this key.
         * @return {@link Builder}
         */
        public Builder enableDoubleClick() {
            this.enableDoubleClick = true;
            return this;
        }

        /**
         * Whether to show debug message for this key.
         * </p>
         * Usually used in debugging when developing this system.
         * @return {@link Builder}
         */
        public Builder showDebugMessage() {
            this.showDebugMessage = true;
            return this;
        }

        /**
         * Build {@link KeyConfig} instance.
         * @return {@link KeyConfig}
         */
        public KeyConfig build() {
            return new KeyConfig(this);
        }
    }

    public boolean isDown() {
        return this.keyMapping.isDown();
    }
}