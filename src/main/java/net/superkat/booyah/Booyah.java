package net.superkat.booyah;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import net.superkat.booyah.item.BooyahItems;
import net.superkat.booyah.network.BooyahPackets;
import net.superkat.booyah.network.BooyahServerNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Booyah implements ModInitializer {
	public static final String MOD_ID = "booyah";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		BooyahItems.init();
		BooyahPackets.init();
		BooyahServerNetworkHandler.init();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}