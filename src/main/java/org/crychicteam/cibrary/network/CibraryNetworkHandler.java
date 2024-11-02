package org.crychicteam.cibrary.network;

import dev.xkmc.l2serial.network.BasePacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.network.key.KeyStatePacket;
import org.crychicteam.cibrary.network.sound.CibrarySoundPacket;
import org.crychicteam.cibrary.network.armorset.ArmorSetSyncPacket;
import org.crychicteam.cibrary.content.armorset.capability.IArmorSetCapability;

public class CibraryNetworkHandler {
    public static final BasePacketHandler HANDLER = new BasePacketHandler(
            new ResourceLocation(Cibrary.MOD_ID, "main"),
            1,
            handler -> handler.create(CibrarySoundPacket.class, NetworkDirection.PLAY_TO_CLIENT),
            handler -> handler.create(ArmorSetSyncPacket.class, NetworkDirection.PLAY_TO_CLIENT),
            handler -> handler.create(KeyStatePacket.class, NetworkDirection.PLAY_TO_SERVER)
    );

    public static void init() {
        HANDLER.registerPackets();
    }

    public static void sendArmorSetSync(ServerPlayer player, IArmorSetCapability cap) {
        HANDLER.toClientPlayer(new ArmorSetSyncPacket(cap), player);
    }
}