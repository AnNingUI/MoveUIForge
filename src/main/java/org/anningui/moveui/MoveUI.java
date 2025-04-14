package org.anningui.moveui;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.anningui.moveui.client.MoveUIReload;
import org.slf4j.Logger;

@Mod(MoveUI.MOD_ID)
public class MoveUI
{
	public static final String MOD_ID = "moveui";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final Boolean kubejsIsOk = ModList.get().isLoaded("kubejs");

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
	public MoveUI() {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, MoveUIReload::onReload);
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::onSetup);
		var modConfig = ModLoadingContext.get();
		modConfig.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
		LOGGER.info("MoveUI Forge Mod Loaded.");
	}

	public void onSetup(FMLCommonSetupEvent event) {
		MoveUIReload.init();
	}
}
