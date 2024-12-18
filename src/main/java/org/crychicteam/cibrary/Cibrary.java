package org.crychicteam.cibrary;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.Registrate;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crychicteam.cibrary.api.common.GlobalCibrarySoundManager;
import org.crychicteam.cibrary.api.registry.ArmorSetRegistry;
import org.crychicteam.cibrary.content.armorset.SkillKey;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.common.ArmorSetAttackListener;
import org.crychicteam.cibrary.content.armorset.common.ArmorSetManager;
import org.crychicteam.cibrary.content.armorset.example.ArmorSetRegistryExample;
import org.crychicteam.cibrary.content.armorset.integration.CuriosIntegration;
import org.crychicteam.cibrary.content.events.server.ArmorSetHandler;
import org.crychicteam.cibrary.content.events.server.ServerKeyHandler;
import org.crychicteam.cibrary.content.events.server.SetEffectHandler;
import org.crychicteam.cibrary.content.key.DefaultKey;
import org.crychicteam.cibrary.content.key.KeyRegistry;
import org.crychicteam.cibrary.network.CibraryNetworkHandler;
import org.slf4j.Logger;

@Mod(Cibrary.MOD_ID)
public class Cibrary {
	public static final String MOD_ID = "cibrary";
	public static Logger LOGGER = LogUtils.getLogger();
	public static final ArmorSetManager ARMOR_SET_MANAGER = ArmorSetManager.getInstance();
	public static final GlobalCibrarySoundManager SOUND_MANAGER = GlobalCibrarySoundManager.getInstance();
	public static final ServerKeyHandler KEY_HANDLER = ServerKeyHandler.getInstance();
	public static final Registrate CI_REGISTRATE = Registrate.create(MOD_ID);

	public static ResourceLocation source(String path) {
		return new ResourceLocation(Cibrary.MOD_ID, path);
	}

	public Cibrary() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus modEventBus = ctx.getModEventBus();
		modEventBus.addListener(this::initializeKeySystem);
		modEventBus.addListener(this::onClientSetup);

		initializeRegistries(modEventBus);
		registerEventListeners();
		initializeArmorSets(modEventBus);
		SkillKey.init();
		ArmorSetRegistryExample.init();
	}

	private void initializeRegistries(IEventBus modEventBus) {
		ArmorSetRegistry.register(modEventBus);
		modEventBus.addListener(this::onCommonSetup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void registerEventListeners() {
		MinecraftForge.EVENT_BUS.register(new ArmorSetHandler());
		MinecraftForge.EVENT_BUS.register(new SetEffectHandler());
		if (ModList.get().isLoaded("curios")) {
			MinecraftForge.EVENT_BUS.register(new CuriosIntegration());
		}
	}

	private void initializeArmorSets(IEventBus modEventBus) {
		AttackEventHandler.register(4000, new ArmorSetAttackListener(ARMOR_SET_MANAGER));
		modEventBus.addListener(ArmorSetCapability::register);
	}

	private void initializeKeySystem(FMLConstructModEvent event) {
		event.enqueueWork(KeyRegistry::init);
	}

	public void onCommonSetup(FMLCommonSetupEvent event) {
		CibraryNetworkHandler.init();
	}

	public void onClientSetup(FMLClientSetupEvent event) {
		DefaultKey.register();
	}
}