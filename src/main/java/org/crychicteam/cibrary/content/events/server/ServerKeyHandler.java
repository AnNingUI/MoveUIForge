package org.crychicteam.cibrary.content.events.server;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.crychicteam.cibrary.content.key.KeyData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles server-side key state management for players.
 */
@Mod.EventBusSubscriber
public class ServerKeyHandler {
    private static ServerKeyHandler INSTANCE;

    /** Stores key states for each player, organized by UUID. */
    public final Map<UUID, Map<ResourceLocation, KeyData>> playerKeyStates = new ConcurrentHashMap<>();

    /**
     * Retrieves the singleton instance of ServerKeyHandler.
     *
     * @return the ServerKeyHandler instance.
     */
    public static ServerKeyHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerKeyHandler();
        }
        return INSTANCE;
    }

    /**
     * Updates the key state for a specific player and key.
     *
     * @param player The player whose key state is being updated.
     * @param keyData The key data to be stored.
     */
    public void updateKeyState(ServerPlayer player, KeyData keyData) {
        playerKeyStates
                .computeIfAbsent(player.getUUID(), k -> new ConcurrentHashMap<>())
                .put(keyData.keyId, keyData);
    }

    /**
     * Clears all stored key states for a player when they log out.
     *
     * @param player The player whose states should be cleared.
     */
    public void clearPlayerStates(ServerPlayer player) {
        playerKeyStates.remove(player.getUUID());
    }

    /**
     * Handles player logout events, clearing key states upon logout.
     *
     * @param event The player logout event.
     */
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            getInstance().clearPlayerStates(player);
        }
    }
}
