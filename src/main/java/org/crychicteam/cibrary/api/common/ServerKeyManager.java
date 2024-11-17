package org.crychicteam.cibrary.api.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.key.KeyData;

import java.util.Optional;

/**
 * Provides API methods for querying key states of players on the server.
 */
public class ServerKeyManager {

    /**
     * Checks if the specified key for the player was just pressed.
     *
     * @param player The player to check.
     * @param keyId The key identifier.
     * @return True if the key was just pressed, false otherwise.
     */
    public static boolean isJustPressed(ServerPlayer player, ResourceLocation keyId) {
        return getKeyState(player, keyId) == KeyData.KeyState.PRESSED;
    }

    /**
     * Checks if the specified key for the player is in a charging state.
     *
     * @param player The player to check.
     * @param keyId The key identifier.
     * @return True if the key is charging, false otherwise.
     */
    public static boolean isCharging(ServerPlayer player, ResourceLocation keyId) {
        return getKeyState(player, keyId) == KeyData.KeyState.CHARGING;
    }

    /**
     * Checks if the specified key for the player was just released.
     *
     * @param player The player to check.
     * @param keyId The key identifier.
     * @return True if the key was just released, false otherwise.
     */
    public static boolean isJustReleased(ServerPlayer player, ResourceLocation keyId) {
        return getKeyState(player, keyId) == KeyData.KeyState.RELEASED;
    }

    /**
     * Retrieves the power level of a charging key for the player.
     *
     * @param player The player to check.
     * @param keyId The key identifier.
     * @return The current power level of the key, or 0 if not applicable.
     */
    public static float getPower(ServerPlayer player, ResourceLocation keyId) {
        return Optional.ofNullable(Cibrary.KEY_HANDLER.playerKeyStates.get(player.getUUID()))
                .map(keys -> keys.get(keyId))
                .map(data -> data.power)
                .orElse(0.0f);
    }

    /**
     * Retrieves the current state of a specific key for the player.
     *
     * @param player The player to check.
     * @param keyId The key identifier.
     * @return The current state of the key, or IDLE if no state is found.
     */
    public static KeyData.KeyState getKeyState(ServerPlayer player, ResourceLocation keyId) {
        return Optional.ofNullable(Cibrary.KEY_HANDLER.playerKeyStates.get(player.getUUID()))
                .map(keys -> keys.get(keyId))
                .map(data -> data.state)
                .orElse(KeyData.KeyState.IDLE);
    }

    /**
     * Retrieves the KeyData object for a specified key of a player.
     *
     * @param player The player to check.
     * @param keyId The key identifier.
     * @return The KeyData for the key, or null if no data exists.
     */
    public static KeyData getKeyData(ServerPlayer player, ResourceLocation keyId) {
        return Optional.ofNullable(Cibrary.KEY_HANDLER.playerKeyStates.get(player.getUUID()))
                .map(keys -> keys.get(keyId))
                .orElse(null);
    }
}
