package org.anningui.moveui.client;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.anningui.moveui.MoveUIStore;
import org.jetbrains.annotations.NotNull;


public class MoveUIReload implements ResourceManagerReloadListener {
    public static MoveUIReload INSTANCE;

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager pResourceManager) {
        MoveUIStore.kjs$reload();
    }

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = new MoveUIReload();
        }
    }


    public static void onReload(AddReloadListenerEvent event) {
        if (INSTANCE!= null) {
            event.addListener(INSTANCE);
        }
    }
}