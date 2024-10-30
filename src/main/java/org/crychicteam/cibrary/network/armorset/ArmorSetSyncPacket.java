package org.crychicteam.cibrary.network.armorset;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.api.registry.armorset.ArmorSetCustomRegistry;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.capability.IArmorSetCapability;

/**
 * Sync the armor set for client players.
 * @author Mihono
 */
@SerialClass
public class ArmorSetSyncPacket extends SerialPacketBase {
    @SerialClass.SerialField
    public String activeSetIdentifier;

    public ArmorSetSyncPacket() {}

    public ArmorSetSyncPacket(IArmorSetCapability cap) {
        ArmorSet activeSet = cap.getActiveSet();
        ResourceLocation id = ArmorSetCustomRegistry.getRegistry().getKey(activeSet);
        this.activeSetIdentifier = id != null ? id.toString() : ArmorSetCustomRegistry.EMPTY.toString();
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                handleClientSide();
            }
        });
        context.setPacketHandled(true);
    }

    private void handleClientSide() {
        Player clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null) {
            clientPlayer.getCapability(ArmorSetCapability.ARMOR_SET_CAPABILITY).ifPresent(cap -> {
                try {
                    ResourceLocation id = new ResourceLocation(activeSetIdentifier);
                    ArmorSet activeSet = ArmorSetCustomRegistry.getRegistry().getValue(id);
                    cap.setActiveSet(activeSet != null ? activeSet : ArmorSetCustomRegistry.EMPTY_SET.get());
                } catch (Exception e) {
                    cap.setActiveSet(ArmorSetCustomRegistry.EMPTY_SET.get());
                }
            });
        }
    }
}