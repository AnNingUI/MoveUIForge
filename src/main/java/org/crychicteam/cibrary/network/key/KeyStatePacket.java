package org.crychicteam.cibrary.network.key;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.key.KeyData;

@SerialClass
public class KeyStatePacket extends SerialPacketBase {
    @SerialClass.SerialField
    public KeyData keyData;

    public KeyStatePacket() {}

    public KeyStatePacket(KeyData keyData) {
        this.keyData = keyData;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    Cibrary.KEY_HANDLER.updateKeyState(player, keyData);
                }
            }
        });
    }
}