package org.crychicteam.cibrary.content.key;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.events.client.ClientKeyHandler;

import java.util.*;

public class KeyRegistry {
    private static final Map<ResourceLocation, KeyConfig> REGISTERED_KEYS = new LinkedHashMap<>();
    private static boolean isInitialized = false;

    public static KeyConfig register(KeyConfig config) {
        if (isInitialized) {
            throw new IllegalStateException("Cannot register keys after initialization");
        }

        if (REGISTERED_KEYS.containsKey(config.id)) {
            throw new IllegalArgumentException("Duplicate key registration: " + config.id);
        }

        REGISTERED_KEYS.put(config.id, config);
        return config;
    }

    public static KeyConfig getConfig(ResourceLocation id) {
        return REGISTERED_KEYS.get(id);
    }

    public static Collection<KeyConfig> getAllConfigs() {
        return Collections.unmodifiableCollection(REGISTERED_KEYS.values());
    }

    public static void init() {
        if (isInitialized) {
            throw new IllegalStateException("KeyRegistry has already been initialized");
        }

        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(KeyRegistry::onKeyMappingRegister);
        modEventBus.addListener(KeyRegistry::onClientSetup);
        isInitialized = true;
    }

    private static void onKeyMappingRegister(RegisterKeyMappingsEvent event) {
        KeyMapping[] existingMappings = Minecraft.getInstance().options.keyMappings;

        for (KeyConfig config : REGISTERED_KEYS.values()) {
            boolean alreadyExists = false;
            for (KeyMapping existing : existingMappings) {
                if (existing.getName().equals(config.keyMapping.getName())) {
                    alreadyExists = true;
                    Cibrary.LOGGER.debug("Skipping registration for existing key mapping: {}", config.id);
                    break;
                }
            }
            if (!alreadyExists) {
                event.register(config.keyMapping);
                Cibrary.LOGGER.debug("Registered new key mapping for: {}", config.id);
            }
        }
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        for (KeyConfig config : REGISTERED_KEYS.values()) {
            ClientKeyHandler.registerKey(config);
        }
    }
}