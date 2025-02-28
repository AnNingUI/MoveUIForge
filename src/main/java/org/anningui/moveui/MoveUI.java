package org.anningui.moveui;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(MoveUI.MOD_ID)
public class MoveUI
{
	public static final String MOD_ID = "moveui_forge";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
	public MoveUI() {
		MinecraftForge.EVENT_BUS.register(this);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
		LOGGER.info("MoveUI Forge Mod Loaded.");
	}
}
