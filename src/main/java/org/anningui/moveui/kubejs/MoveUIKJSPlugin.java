package org.anningui.moveui.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.anningui.moveui.MoveUIStore;
import org.jetbrains.annotations.NotNull;

import static org.anningui.moveui.MoveUI.kubejsIsOk;

public class MoveUIKJSPlugin extends KubeJSPlugin {
    @Override
    public void registerBindings(@NotNull BindingsEvent event) {
        if (event.getType().isClient() && kubejsIsOk) {
            event.add("MoveUIStore", MoveUIStore.class);
        }
    }
}
