package org.crychicteam.cibrary.content.events.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.key.KeyConfig;
import org.crychicteam.cibrary.content.key.KeyData;
import org.crychicteam.cibrary.content.key.KeyRegistry;
import org.crychicteam.cibrary.network.CibraryNetworkHandler;
import org.crychicteam.cibrary.network.key.KeyStatePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ClientKeyHandler manages the state and behavior of custom key bindings in the client.
 * <p>
 * It supports single and double-click detection, charging mechanics, and client-server
 * <p>
 * synchronization of key states. This handler listens to the client tick events and
 * <p>
 * updates key states each tick to ensure accurate real-time tracking of key interactions.
 * @author M1hono
 */
@EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ClientKeyHandler {
    private static final Map<ResourceLocation, KeyData> KEY_STATES = new HashMap<>();
    private static final Map<ResourceLocation, Boolean> PREVIOUS_STATES = new HashMap<>();
    private static final Map<ResourceLocation, Long> PRESS_START_TIMES = new HashMap<>();
    private static final Map<ResourceLocation, Long> RELEASE_TIMES = new HashMap<>();
    private static final Map<ResourceLocation, KeyData.KeyState> LAST_SENT_STATE = new HashMap<>();
    /** Threshold for key hold duration to enter charging mode (in milliseconds). */
    public static final long CHARGE_THRESHOLD = 100;
    /** Maximum allowable time between clicks for double-click detection (in milliseconds). */
    public static final long DOUBLE_CLICK_TIME = 200;
    /** Minimum allowable time between releases to enter release mode (in milliseconds). */
    public static final float LEAST_RELEASE_TIME = 0.5f;

    /**
     * Registers a new key in the handler. Initializes state-tracking maps for the key
     * <p>
     * to enable custom interactions.
     *
     * @param config The configuration of the key to register.
     */
    public static void registerKey(KeyConfig config) {
        if (KEY_STATES.containsKey(config.id)) {
            Cibrary.LOGGER.warn("Duplicate key registration attempt: {}", config.id);
            return;
        }
        // Initialize states for the new key
        KEY_STATES.put(config.id, new KeyData(config.id));
        PREVIOUS_STATES.put(config.id, false);
        PRESS_START_TIMES.put(config.id, 0L);
        RELEASE_TIMES.put(config.id, 0L);
        LAST_SENT_STATE.put(config.id, KeyData.KeyState.IDLE);
        Cibrary.LOGGER.debug("Registered key: {}", config.id);
    }

    /**
     * Event listener that updates all key states each client tick. Processes each
     * <p>
     * registered key to determine if it has been pressed, released, charged, or clicked.
     * <p>
     * Updates server if a key state changes.
     *
     * @param event The client tick event.
     */
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        if (event.phase != Phase.END || Minecraft.getInstance().player == null) return;

        long currentTime = System.currentTimeMillis();

        /*
          The main logic of this tracking is:
          It iterates through all registered key configurations, retrieves their current state,
          and updates their status based on whether they are pressed or released.
          If the key state changes, it sends the updated state to the server.
          It also handles charging, single-click, and double-click detection.

          - Charging: If the key is held down for longer than the CHARGE_THRESHOLD, and charging is enabled,
            the key state is set to CHARGING. The power level is updated based on the hold duration.

          - Single Click: If the key is released and the click count is 1, and the time since release exceeds
            the DOUBLE_CLICK_TIME, the key state set to PRESSED.

          - Double Click: If the key release and the click count is 2, and the time since release exceeds
            the DOUBLE_CLICK_TIME, the key state is set to DOUBLE_CLICKED.

         */
        for (KeyConfig config : KeyRegistry.getAllConfigs()) {
            ResourceLocation keyId = config.id;
            KeyData keyData = KEY_STATES.get(keyId);
            Boolean prevPressed = PREVIOUS_STATES.get(keyId);

            if (keyData == null || prevPressed == null) {
                Cibrary.LOGGER.error("Missing key data for registered key: {}", keyId);
                continue;
            }

            boolean isPressed = config.isDown();

            // Update the key state based on the current press status
            updateKeyState(keyId, config, keyData, isPressed, prevPressed, currentTime);

            // Send key state if it has changed
            KeyData.KeyState lastSent = LAST_SENT_STATE.get(keyId);
            if (lastSent != keyData.state) {
                sendKeyState(keyData);
                LAST_SENT_STATE.put(keyId, keyData.state);
            }

            PREVIOUS_STATES.put(keyId, isPressed);

            // Reset the key state if necessary
            if (keyData.state != KeyData.KeyState.IDLE && keyData.state != KeyData.KeyState.CHARGING) {
                keyData.state = KeyData.KeyState.IDLE;
            }
        }
    }

    /**
     * Updates the state of a key based on its current and previous press status.
     *
     * @param keyId       The unique identifier of the key.
     * @param config      The configuration data for the key.
     * @param keyData     The key's current data and state.
     * @param isPressed   True if the key is currently pressed.
     * @param prevPressed True if the key was pressed in the previous tick.
     * @param currentTime The current system time in milliseconds.
     */
    private static void updateKeyState(ResourceLocation keyId, KeyConfig config, KeyData keyData,
                                       boolean isPressed, boolean prevPressed, long currentTime) {
        if (isPressed) {
            handleKeyPress(keyId, config, keyData, currentTime, prevPressed);
        } else if (prevPressed) {
            handleKeyRelease(keyId, config, keyData, currentTime);
        } else {
            handleIdleState(keyId, config, keyData, currentTime);
        }
    }

    private static void handleKeyPress(ResourceLocation keyId, KeyConfig config, KeyData keyData,
                                       long currentTime, boolean prevPressed) {
        if (!prevPressed) {
            PRESS_START_TIMES.put(keyId, currentTime);
            return;
        }

        long holdDuration = currentTime - PRESS_START_TIMES.get(keyId);

        if (holdDuration > DOUBLE_CLICK_TIME && config.enableCharging && keyData.state == KeyData.KeyState.IDLE) {
            keyData.state = KeyData.KeyState.CHARGING;
            keyData.clickCount = 0;
            showDebugMessage("Start Charging: " + keyId.getPath(), config);
        }

        if (keyData.state == KeyData.KeyState.CHARGING) {
            updateChargePower(keyId, config, keyData, currentTime);
        }
    }


    /**
     * Processes actions when a key is released. Determines if the key should
     * be set to a released state or if it should register a click.
     *
     * @param keyId       The unique identifier of the key.
     * @param config      The configuration data for the key.
     * @param keyData     The key's current data and state.
     * @param currentTime The current system time in milliseconds.
     */
    private static void handleKeyRelease(ResourceLocation keyId, KeyConfig config, KeyData keyData, long currentTime) {
        long holdDuration = currentTime - PRESS_START_TIMES.get(keyId);
        RELEASE_TIMES.put(keyId, currentTime);

        if (keyData.state == KeyData.KeyState.CHARGING) {
            if (keyData.power < LEAST_RELEASE_TIME) {
                keyData.state = KeyData.KeyState.PRESSED;
                showDebugMessage("Key Pressed (Low Power): " + keyId.getPath(), config);
            } else {
                keyData.state = KeyData.KeyState.RELEASED;
                showDebugMessage(String.format("Released %s with power: %.1f", keyId.getPath(), keyData.power), config);
            }
            return;
        }

        // Track click count for handling double clicks
        keyData.clickCount++;
        keyData.lastClickTime = currentTime;
        PRESS_START_TIMES.put(keyId, 0L);
    }


    /**
     * Handles idle state detection, identifying single and double clicks.
     *
     * @param keyId       The unique identifier of the key.
     * @param config      The configuration data for the key.
     * @param keyData     The key's current data and state.
     * @param currentTime The current system time in milliseconds.
     */
    private static void handleIdleState(ResourceLocation keyId, KeyConfig config, KeyData keyData, long currentTime) {
        if (keyData.clickCount == 0 || keyData.state != KeyData.KeyState.IDLE) return;

        long timeSinceRelease = currentTime - RELEASE_TIMES.get(keyId);
        if (timeSinceRelease > Math.min(config.doubleClickTime, DOUBLE_CLICK_TIME)) {
            if (keyData.clickCount == 1) {
                keyData.state = KeyData.KeyState.PRESSED;
                showDebugMessage("Key Pressed: " + keyId.getPath(), config);
            } else if (keyData.clickCount == 2) {
                keyData.state = KeyData.KeyState.DOUBLE_CLICKED;
                showDebugMessage("Double Clicked: " + keyId.getPath(), config);
            }
            keyData.clickCount = 0;
        }
    }

    /**
     * Updates the power level of a key while it is being charged.
     *
     * @param keyId       The unique identifier of the key.
     * @param config      The configuration data for the key.
     * @param keyData     The key's current data and state.
     * @param currentTime The current system time in milliseconds.
     */
    private static void updateChargePower(ResourceLocation keyId, KeyConfig config, KeyData keyData, long currentTime) {
        long holdDuration = currentTime - PRESS_START_TIMES.get(keyId);
        float oldPower = keyData.power;
        keyData.power = Math.min((float) holdDuration / 1000.0f, config.maxPower);

        // Send updated power state if it has changed
        if ((int)(oldPower * 10) != (int)(keyData.power * 10)) {
            showDebugMessage(String.format("Charging %s: %.1f", keyId.getPath(), keyData.power), config);
            sendKeyState(keyData);
        }
    }

    /**
     * Sends the current state of a key to the server to keep client and server synchronized.
     *
     * @param keyData The key data to send.
     */
    private static void sendKeyState(KeyData keyData) {
        CibraryNetworkHandler.HANDLER.toServer(new KeyStatePacket(keyData));
    }

    /**
     * Displays debug messages on the client, if enabled in the key configuration.
     *
     * @param message The debug message to show.
     * @param config  The key configuration specifying whether to display messages.
     */
    private static void showDebugMessage(String message, KeyConfig config) {
        if (config.showDebugMessage) {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.displayClientMessage(Component.literal(message), true);
            }
        }
    }

    /**
     * Retrieves the current state of a specified key.
     *
     * @param keyId The unique identifier of the key.
     * @return The current state of the key, if present.
     */
    public static Optional<KeyData> getKeyState(ResourceLocation keyId) {
        return Optional.ofNullable(KEY_STATES.get(keyId));
    }

    /**
     * Resets the state of a specified key, clearing press/release times and resetting to IDLE.
     *
     * @param keyId The unique identifier of the key.
     */
    public static void resetKeyState(ResourceLocation keyId) {
        KEY_STATES.computeIfPresent(keyId, (id, data) -> {
            data.reset();
            return data;
        });
        PRESS_START_TIMES.put(keyId, 0L);
        RELEASE_TIMES.put(keyId, 0L);
        LAST_SENT_STATE.put(keyId, KeyData.KeyState.IDLE);
    }

    /**
     * Clears all stored states for all keys, resetting the handler.
     */
    public static void clearAllStates() {
        KEY_STATES.clear();
        PREVIOUS_STATES.clear();
        PRESS_START_TIMES.clear();
        RELEASE_TIMES.clear();
        LAST_SENT_STATE.clear();
    }
}
