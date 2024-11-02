package org.crychicteam.cibrary.api.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.crychicteam.cibrary.content.sound.SoundData;
import org.crychicteam.cibrary.network.CibraryNetworkHandler;
import org.crychicteam.cibrary.network.sound.CibrarySoundPacket;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages sound playback on the server side for multiple players, sending appropriate packets to clients.
 * This class is responsible for initiating sound effects on clients from the server,
 * including playing, stopping, and managing fading and looping sounds for individual players or groups of players.
 */
public class GlobalCibrarySoundManager {
    private static GlobalCibrarySoundManager instance;
    private final Map<ServerPlayer, PlayerSoundManager> playerManagers = new ConcurrentHashMap<>();

    private GlobalCibrarySoundManager() {}

    /**
     * Gets the singleton instance of GlobalCibrarySoundManager.
     *
     * @return The singleton instance of GlobalCibrarySoundManager.
     */
    public static GlobalCibrarySoundManager getInstance() {
        if (instance == null) {
            instance = new GlobalCibrarySoundManager();
        }
        return instance;
    }

    /**
     * Gets or creates a PlayerSoundManager for a specific player.
     *
     * @param player The player to get the sound manager for.
     * @return The PlayerSoundManager for the specified player.
     */
    public PlayerSoundManager getManagerForPlayer(ServerPlayer player) {
        return playerManagers.computeIfAbsent(player, PlayerSoundManager::new);
    }

    /**
     * Removes the PlayerSoundManager for a specific player.
     *
     * @param player The player whose sound manager should be removed.
     */
    public void removeManager(ServerPlayer player) {
        playerManagers.remove(player);
    }

    /**
     * Plays a sound for a specific player.
     *
     * @param player The player for whom the sound will be played.
     * @param sound The sound event to be played.
     * @param soundType The type of the sound (e.g., PLAYER, MUSIC, AMBIENT).
     * @param volume The volume of the sound.
     * @param pitch The pitch/frequency of the sound.
     * @param fadeInTime The time in seconds for the sound to fade in.
     */
    public void playSound(ServerPlayer player, SoundEvent sound, SoundSource soundType, float volume, float pitch, float fadeInTime) {
        getManagerForPlayer(player).playSound(sound, soundType, volume, pitch, fadeInTime);
    }

    /**
     * Plays a sound for multiple players.
     *
     * @param players The collection of players for whom the sound will be played.
     * @param sound The sound event to be played.
     * @param soundType The type of the sound (e.g., PLAYER, MUSIC, AMBIENT).
     * @param volume The volume of the sound.
     * @param pitch The pitch/frequency of the sound.
     * @param fadeInTime The time in seconds for the sound to fade in.
     */
    public void playSoundToPlayers(Collection<ServerPlayer> players, SoundEvent sound, SoundSource soundType, float volume, float pitch, float fadeInTime) {
        for (ServerPlayer player : players) {
            playSound(player, sound, soundType, volume, pitch, fadeInTime);
        }
    }

    /**
     * Stops a sound for a specific player.
     *
     * @param player The player for whom the sound will be stopped.
     * @param sound The sound event to be stopped.
     * @param fadeOutTime The time in seconds for the sound to fade out.
     */
    public void stopSound(ServerPlayer player, SoundEvent sound, float fadeOutTime) {
        getManagerForPlayer(player).stopSound(sound, fadeOutTime);
    }

    /**
     * Stops a sound for multiple players.
     *
     * @param players The collection of players for whom the sound will be stopped.
     * @param sound The sound event to be stopped.
     * @param fadeOutTime The time in seconds for the sound to fade out.
     */
    public void stopSoundForPlayers(Collection<ServerPlayer> players, SoundEvent sound, float fadeOutTime) {
        for (ServerPlayer player : players) {
            stopSound(player, sound, fadeOutTime);
        }
    }

    /**
     * Stops all sounds for a specific player.
     *
     * @param player The player for whom all sounds will be stopped.
     * @param fadeOutTime The time in seconds for the sounds to fade out.
     */
    public void stopAllSounds(ServerPlayer player, float fadeOutTime) {
        getManagerForPlayer(player).stopAllSounds(fadeOutTime);
    }

    /**
     * Stops all sounds for multiple players.
     *
     * @param players The collection of players for whom all sounds will be stopped.
     * @param fadeOutTime The time in seconds for the sounds to fade out.
     */
    public void stopAllSoundsForPlayers(Collection<ServerPlayer> players, float fadeOutTime) {
        for (ServerPlayer player : players) {
            stopAllSounds(player, fadeOutTime);
        }
    }

    /**
     * Cross-fades between two sounds for a specific player.
     *
     * @param player The player for whom the sounds will be cross-faded.
     * @param oldSound The sound event to be faded out.
     * @param newSound The sound event to be faded in.
     * @param soundType The type of the new sound (e.g., PLAYER, MUSIC, AMBIENT).
     * @param volume The volume of the new sound.
     * @param pitch The pitch/frequency of the new sound.
     * @param fadeTime The time in seconds for the cross-fade to occur.
     */
    public void crossFade(ServerPlayer player, SoundEvent oldSound, SoundEvent newSound, SoundSource soundType, float volume, float pitch, float fadeTime) {
        getManagerForPlayer(player).crossFade(oldSound, newSound, soundType, volume, pitch, fadeTime);
    }

    /**
     * Cross-fades between two sounds for multiple players.
     *
     * @param players The collection of players for whom the sounds will be cross-faded.
     * @param oldSound The sound event to be faded out.
     * @param newSound The sound event to be faded in.
     * @param soundType The type of the new sound (e.g., PLAYER, MUSIC, AMBIENT).
     * @param volume The volume of the new sound.
     * @param pitch The pitch/frequency of the new sound.
     * @param fadeTime The time in seconds for the cross-fade to occur.
     */
    public void crossFadeForPlayers(Collection<ServerPlayer> players, SoundEvent oldSound, SoundEvent newSound, SoundSource soundType, float volume, float pitch, float fadeTime) {
        for (ServerPlayer player : players) {
            crossFade(player, oldSound, newSound, soundType, volume, pitch, fadeTime);
        }
    }

    /**
     * Manages sounds for an individual player.
     */
    private static class PlayerSoundManager {
        private final ServerPlayer player;

        PlayerSoundManager(ServerPlayer player) {
            this.player = player;
        }

        void playSound(SoundEvent sound, SoundSource soundType, float volume, float pitch, float fadeInTime) {
            SoundData soundData = new SoundData(sound.getLocation(), soundType, volume, pitch, fadeInTime, 0);
            CibrarySoundPacket packet = new CibrarySoundPacket(soundData, CibrarySoundPacket.PacketType.PLAY);
            CibraryNetworkHandler.HANDLER.toClientPlayer(packet, player);
        }

        void stopSound(SoundEvent sound, float fadeOutTime) {
            SoundData soundData = new SoundData(sound.getLocation(), null, 0, 0, fadeOutTime, 0);
            CibrarySoundPacket packet = new CibrarySoundPacket(soundData, CibrarySoundPacket.PacketType.STOP);
            CibraryNetworkHandler.HANDLER.toClientPlayer(packet, player);
        }

        void stopAllSounds(float fadeOutTime) {
            SoundData soundData = new SoundData(null, null, 0, 0, fadeOutTime, 0);
            CibrarySoundPacket packet = new CibrarySoundPacket(soundData, CibrarySoundPacket.PacketType.STOP_ALL);
            CibraryNetworkHandler.HANDLER.toClientPlayer(packet, player);
        }

        void crossFade(SoundEvent oldSound, SoundEvent newSound, SoundSource soundType, float volume, float pitch, float fadeTime) {
            SoundData oldSoundData = new SoundData(oldSound.getLocation(), null, 0, 0, fadeTime, 0);
            SoundData newSoundData = new SoundData(newSound.getLocation(), soundType, volume, pitch, fadeTime, 0);
            CibrarySoundPacket packet = new CibrarySoundPacket(oldSoundData, newSoundData, CibrarySoundPacket.PacketType.CROSS_FADE);
            CibraryNetworkHandler.HANDLER.toClientPlayer(packet, player);
        }
    }
}