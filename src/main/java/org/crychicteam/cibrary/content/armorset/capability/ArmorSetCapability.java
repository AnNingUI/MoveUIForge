package org.crychicteam.cibrary.content.armorset.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.crychicteam.cibrary.Cibrary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Cibrary.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorSetCapability {
    public static final Capability<IArmorSetCapability> ARMOR_SET_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static final ResourceLocation ARMOR_SET_CAPABILITY_ID = new ResourceLocation(Cibrary.MOD_ID, "armor_set");

    private static boolean isRegistered = false;

    public static void register(RegisterCapabilitiesEvent event) {
        if (!isRegistered) {
            event.register(IArmorSetCapability.class);
            isRegistered = true;
        }
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!event.getCapabilities().containsKey(ARMOR_SET_CAPABILITY_ID)) {
                event.addCapability(ARMOR_SET_CAPABILITY_ID, new Provider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(ARMOR_SET_CAPABILITY).ifPresent(oldStore ->
                    event.getEntity().getCapability(ARMOR_SET_CAPABILITY).ifPresent(newStore -> {
                        CompoundTag nbt = oldStore.serializeNBT();
                        newStore.deserializeNBT(nbt);
                    })
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            event.getEntity().getCapability(ARMOR_SET_CAPABILITY).ifPresent(cap ->
                    cap.syncToClient(event.getEntity())
            );
        }
    }

    public static class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final ArmorSetCapabilityHandler handler = new ArmorSetCapabilityHandler();
        private final LazyOptional<IArmorSetCapability> instance = LazyOptional.of(() -> handler);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ARMOR_SET_CAPABILITY.orEmpty(cap, instance);
        }

        @Override
        public CompoundTag serializeNBT() {
            return handler.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            handler.deserializeNBT(nbt);
        }

        public void invalidate() {
            instance.invalidate();
        }
    }
}