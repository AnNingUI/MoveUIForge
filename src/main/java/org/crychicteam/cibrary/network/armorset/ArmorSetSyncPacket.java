package org.crychicteam.cibrary.network.armorset;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.common.ArmorSetManager;
import org.crychicteam.cibrary.content.armorset.capability.IArmorSetCapability;

/**
 * Sync the armor set for client players.
 *  @author Mihono
 */
@SerialClass
public class ArmorSetSyncPacket extends SerialPacketBase {
    @SerialClass.SerialField
    public String activeSetIdentifier;

    public ArmorSetSyncPacket() {}

    public ArmorSetSyncPacket(IArmorSetCapability cap) {
        ArmorSet activeSet = cap.getActiveSet();
        this.activeSetIdentifier = activeSet != null ? activeSet.getIdentifier().toString() : "";
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
                if (!activeSetIdentifier.isEmpty()) {
                    ArmorSet activeSet = Cibrary.ARMOR_SET_MANAGER.getArmorSetByIdentifier(activeSetIdentifier);
                    cap.setActiveSet(activeSet);
                } else {
                    cap.setActiveSet(Cibrary.ARMOR_SET_MANAGER.getDefaultArmorSet());
                }
            });
        }
    }
}