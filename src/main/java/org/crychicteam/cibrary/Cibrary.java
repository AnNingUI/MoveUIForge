package org.crychicteam.cibrary;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.Registrate;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
;
import org.crychicteam.cibrary.api.registry.armorset.ArmorSetCustomRegistry;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.common.ArmorSetAttackListener;
import org.crychicteam.cibrary.content.armorset.common.ArmorSetManager;
import org.crychicteam.cibrary.content.armorset.example.ArmorSetRegistryExample;
import org.crychicteam.cibrary.content.armorset.integration.CuriosIntegration;
import org.crychicteam.cibrary.content.events.common.ArmorSetHandler;
import org.crychicteam.cibrary.content.events.common.SetEffectHandler;
import org.crychicteam.cibrary.content.sound.GlobalCibrarySoundManager;
import org.crychicteam.cibrary.network.CibraryNetworkHandler;
import org.slf4j.Logger;

@Mod(Cibrary.MOD_ID)
public class Cibrary
{
	public static final String MOD_ID = "cibrary";
	public static Logger LOGGER = LogUtils.getLogger();
	public static final ArmorSetManager ARMOR_SET_MANAGER = ArmorSetManager.getInstance();
	/**
	 * Currently, although the sound manager has been completed, I couldn't find a way of using it.
	 * Need deeper design with GeckoLib.
 	 */
	public static final GlobalCibrarySoundManager SOUND_MANAGER = GlobalCibrarySoundManager.getInstance();
	public static final Registrate CI_REGISTRATE = Registrate.create(MOD_ID);

	/**
     * Get a resource location for the mod's resources.
     * @param path The path to the resource.
     * @return A ResourceLocation object for the given path.
     */
	public static ResourceLocation source(String path) {
		return new ResourceLocation(Cibrary.MOD_ID, path);
	}


	/**
	 * The main constructor of the Cibrary mod.
	 * This constructor initializes the mod's components, registers event listeners, and sets up the mod's capabilities.
	 */
	public Cibrary() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		ArmorSetCustomRegistry.register(bus);
		bus.addListener(this::onCommonSetup);
		MinecraftForge.EVENT_BUS.register(this);

		// All armorSets handlers.
		MinecraftForge.EVENT_BUS.register(new ArmorSetHandler());
		MinecraftForge.EVENT_BUS.register(new SetEffectHandler());
		// Check if the Curios mod is loaded and register the CuriosIntegration if it is
		if (ModList.get().isLoaded("curios")) {
			MinecraftForge.EVENT_BUS.register(new CuriosIntegration());
		}

		// Register the ArmorSetAttackListener to handle armor set attack events with a priority of 4000
		AttackEventHandler.register(4000, new ArmorSetAttackListener(ARMOR_SET_MANAGER));
		bus.addListener(ArmorSetCapability::register);
	}

	public void onCommonSetup(FMLCommonSetupEvent event) {
		CibraryNetworkHandler.init();
	}

}