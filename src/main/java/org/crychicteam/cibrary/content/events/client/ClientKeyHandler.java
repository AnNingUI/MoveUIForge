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
 * ClientKeyHandler manages custom key bindings in Minecraft's client-side environment.
 * <p>
 * Data Structures:
 * <p>
 * - Uses HashMap for O(1) access to key states and related timing data
 * <p>
 * - KEY_STATES: Maps ResourceLocation to KeyData for current key states
 * <p>
 * - PREVIOUS_STATES: Tracks previous tick's key states for edge detection
 * <p>
 * - PRESS_START_TIMES: Records key press timestamps for duration calculation
 * <p>
 * - LAST_SENT_STATE: Maintains last sent state to minimize network traffic
 * <p>
 * <p>
 * Key Features:
 * <p>
 * 1. State Management
 * <p>
 *    - Implements a finite state machine with states: IDLE, CHARGING, PRESSED, RELEASED
 * <p>
 *    - Uses timestamp-based state transitions for accurate timing
 * <p>
 * <p>
 * 2. Input Detection
 * <p>
 *    - Basic press: Standard key press and release detection
 * <p>
 *    - Charging: Hold detection after CHARGE_THRESHOLD duration
 * <p>
 * <p>
 * 3. Network Synchronization
 * <p>
 *    - Client-server state sync via KeyStatePacket
 * <p>
 *    - Optimized to send updates only on state changes
 * <p>
 * <p>
 * Performance Considerations:
 * <p>
 * - O(1) lookup time for all key states and timings
 * <p>
 * - O(n) processing time per tick where n is number of registered keys
 * <p>
 * - Memory usage scales linearly with number of registered keys
 * <p>
 * @author M1hono
 */
@EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ClientKeyHandler {
    private static final Map<ResourceLocation, KeyData> KEY_STATES = new HashMap<>();
    private static final Map<ResourceLocation, Boolean> PREVIOUS_STATES = new HashMap<>();
    private static final Map<ResourceLocation, Long> PRESS_START_TIMES = new HashMap<>();
    private static final Map<ResourceLocation, KeyData.KeyState> LAST_SENT_STATE = new HashMap<>();

    /** Minimum hold duration (ms) to trigger charging state */
    public static final long CHARGE_THRESHOLD = 100;

    /** Minimum power level required for release action */
    public static final float LEAST_RELEASE_TIME = 0.5f;

    /**
     * Registers a new key binding in the handler's state management system.
     * <p>
     * Implementation Details:
     * <p>
     * 1. Performs duplicate registration check (O(1))
     * <p>
     * 2. Initializes all state maps with default values
     * <p>
     * Memory Impact:
     * <p>
     * - Adds entries to tracking maps
     * <p>
     *
     * @param config Configuration object containing key binding parameters
     */
    public static void registerKey(KeyConfig config) {
        if (KEY_STATES.containsKey(config.id)) {
            Cibrary.LOGGER.warn("Duplicate key registration attempt: {}", config.id);
            return;
        }
        KEY_STATES.put(config.id, new KeyData(config.id));
        PREVIOUS_STATES.put(config.id, false);
        PRESS_START_TIMES.put(config.id, 0L);
        LAST_SENT_STATE.put(config.id, KeyData.KeyState.IDLE);
        Cibrary.LOGGER.debug("Registered key: {}", config.id);
    }

    /**
     * Core tick processing loop for key state management.
     * <p>
     * Algorithm Overview:
     * <p>
     * For each registered key:
     * <p>
     *    - Retrieve current and previous states
     * <p>
     *    - Calculate time deltas
     * <p>
     *    - Update state based on timing conditions
     * <p>
     *    - Trigger network sync if state changed
     * <p>
     * Time Complexity: O(n) where n is number of registered keys
     * <p>
     * Space Complexity: O(1) additional memory per tick
     * <p>
     * State Transition Logic:
     * - IDLE -> CHARGING: Hold duration > CHARGE_THRESHOLD
     * <p>
     * - CHARGING -> RELEASED: Release with sufficient power
     * <p>
     * - CHARGING -> PRESSED: Release with insufficient power
     * <p>
     * - IDLE -> PRESSED: Standard key press detected
     *
     * @param event Client tick event from Forge event bus
     */
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        if (event.phase != Phase.END || Minecraft.getInstance().player == null) return;

        long currentTime = System.currentTimeMillis();

        for (KeyConfig config : KeyRegistry.getAllConfigs()) {
            ResourceLocation keyId = config.id;
            KeyData keyData = KEY_STATES.get(keyId);
            Boolean prevPressed = PREVIOUS_STATES.get(keyId);

            if (keyData == null || prevPressed == null) {
                Cibrary.LOGGER.error("Missing key data for registered key: {}", keyId);
                continue;
            }

            boolean isPressed = config.isDown();
            updateKeyState(keyId, config, keyData, isPressed, prevPressed, currentTime);

            KeyData.KeyState lastSent = LAST_SENT_STATE.get(keyId);
            if (lastSent != keyData.state) {
                sendKeyState(keyData);
                LAST_SENT_STATE.put(keyId, keyData.state);
            }

            PREVIOUS_STATES.put(keyId, isPressed);

            if (keyData.state != KeyData.KeyState.IDLE && keyData.state != KeyData.KeyState.CHARGING) {
                keyData.state = KeyData.KeyState.IDLE;
            }
        }
    }

    /**
     * Updates key state based on current conditions and timing.
     *
     * State Machine Implementation:
     * 1. Press Detection -> handleKeyPress()
     * 2. Release Detection -> handleKeyRelease()
     *
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     */
    private static void updateKeyState(ResourceLocation keyId, KeyConfig config, KeyData keyData,
                                       boolean isPressed, boolean prevPressed, long currentTime) {
        if (isPressed) {
            handleKeyPress(keyId, config, keyData, currentTime, prevPressed);
        } else if (prevPressed) {
            handleKeyRelease(keyId, config, keyData, currentTime);
        }
    }

    /**
     * Processes key press events and manages charging mechanics.
     *
     * Algorithm:
     * 1. Initial press detection and timestamp recording
     * 2. Hold duration calculation
     * 3. Charge state transition check
     * 4. Power level update for charging keys
     *
     * Time Complexity: O(1)
     * Memory Usage: Constant
     */
    private static void handleKeyPress(ResourceLocation keyId, KeyConfig config, KeyData keyData,
                                       long currentTime, boolean prevPressed) {
        if (!prevPressed) {
            PRESS_START_TIMES.put(keyId, currentTime);
            return;
        }

        long holdDuration = currentTime - PRESS_START_TIMES.get(keyId);

        if (holdDuration > CHARGE_THRESHOLD && config.enableCharging && keyData.state == KeyData.KeyState.IDLE) {
            keyData.state = KeyData.KeyState.CHARGING;
            showDebugMessage("Start Charging: " + keyId.getPath(), config);
        }

        if (keyData.state == KeyData.KeyState.CHARGING) {
            updateChargePower(keyId, config, keyData, currentTime);
        }
    }

    /**
     * Handles key release events and determines resulting state.
     *
     * State Transition Logic:
     * 1. Charging Release:
     *    - Power < LEAST_RELEASE_TIME -> PRESSED
     *    - Power >= LEAST_RELEASE_TIME -> RELEASED
     * 2. Normal Release:
     *    - Transitions to PRESSED state
     *
     * Time Complexity: O(1)
     * Memory Impact: Updates timestamp storage
     */
    private static void handleKeyRelease(ResourceLocation keyId, KeyConfig config, KeyData keyData, long currentTime) {
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

        keyData.state = KeyData.KeyState.PRESSED;
        showDebugMessage("Key Pressed: " + keyId.getPath(), config);
        PRESS_START_TIMES.put(keyId, 0L);
    }

    /**
     * Updates power level for charging keys.
     *
     * Power Calculation:
     * - Linear scaling based on hold duration
     * - Capped by config.maxPower
     * - Granularity: 0.1 units for network optimization
     *
     * Time Complexity: O(1)
     * Network Impact: Updates sent only on 0.1 unit changes
     */
    private static void updateChargePower(ResourceLocation keyId, KeyConfig config, KeyData keyData, long currentTime) {
        long holdDuration = currentTime - PRESS_START_TIMES.get(keyId);
        float oldPower = keyData.power;
        keyData.power = Math.min((float) holdDuration / 1000.0f, config.maxPower);

        if ((int)(oldPower * 10) != (int)(keyData.power * 10)) {
            showDebugMessage(String.format("Charging %s: %.1f", keyId.getPath(), keyData.power), config);
            sendKeyState(keyData);
        }
    }

    /**
     * Sends key state updates to server.
     *
     * Network Optimization:
     * - Only sends on state changes
     * - Batches power updates by 0.1 increments
     *
     * @param keyData Current key state to synchronize
     */
    private static void sendKeyState(KeyData keyData) {
        CibraryNetworkHandler.HANDLER.toServer(new KeyStatePacket(keyData));
    }

    /**
     * Displays debug information in client chat.
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
     * Retrieves current key state.
     *
     * @param keyId Key identifier
     * @return Optional containing KeyData if key exists
     */
    public static Optional<KeyData> getKeyState(ResourceLocation keyId) {
        return Optional.ofNullable(KEY_STATES.get(keyId));
    }

    /**
     * Resets key state to default values.
     * @param keyId The unique identifier of the key to reset
     */
    public static void resetKeyState(ResourceLocation keyId) {
        KEY_STATES.computeIfPresent(keyId, (id, data) -> {
            data.reset();
            return data;
        });
        PRESS_START_TIMES.put(keyId, 0L);
        LAST_SENT_STATE.put(keyId, KeyData.KeyState.IDLE);
    }

    /**
     * Clears all key states and related data.
     */
    public static void clearAllStates() {
        KEY_STATES.clear();
        PREVIOUS_STATES.clear();
        PRESS_START_TIMES.clear();
        LAST_SENT_STATE.clear();
    }
}