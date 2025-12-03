package b100.betabiomecolors;

import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

import b100.betabiomecolors.util.ThreadedCache;
import b100.lib.client.translate.Translate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

public class BetaBiomeColors implements ClientModInitializer {
	
	public static final String MODID = "betabiomecolors";
	public static final File CONFIG_FOLDER = Paths.get("config").toFile();
	public static final File CONFIG_FILE = new File(CONFIG_FOLDER, MODID + ".properties");
	
	private static boolean sodiumInstalled = false;
	
	public static NoiseGeneratorOctaves2 field_4194_e;
	public static NoiseGeneratorOctaves2 field_4193_f;
	public static NoiseGeneratorOctaves2 field_4192_g;

	static {
		Translate.registerNamespace(MODID);
		
		BetaBiomeColorsConfig.load(CONFIG_FILE);
		
		setSeed(BetaBiomeColorsConfig.seed);
		
		BetaBiomeColorsConfig.save(CONFIG_FILE);
	}
	
	public static void setSeed(long seed) {
		field_4194_e = new NoiseGeneratorOctaves2(new Random(seed * 9871L), 4);
		field_4193_f = new NoiseGeneratorOctaves2(new Random(seed * 39811L), 4);
		field_4192_g = new NoiseGeneratorOctaves2(new Random(seed * 543321L), 2);
	}
	
	public static int getGrassColor(BlockPos pos) {
		if(pos == null) {
			return GrassColors.getColor(1.0, 0.5);
		}
		
		int x = pos.getX();
		int z = pos.getZ();
		return getGrassColor(x, z);
	}
	
	public static int getFoliageColor(BlockPos pos) {
		if(pos == null) {
			return FoliageColors.getColor(1.0, 0.5);
		}
		
		int x = pos.getX();
		int z = pos.getZ();
		return getGrassColor(x, z);
	}
	
	public static int getGrassColor(int x, int z) {
		ThreadedCache cache = ThreadedCache.get();
		cache.loadBlockGeneratorData(x, z, 1, 1);
		
		return GrassColors.getColor(cache.temperature[0], cache.humidity[0]);
	}
	
	public static int getFoliageColor(int x, int z) {
		ThreadedCache cache = ThreadedCache.get();
		cache.loadBlockGeneratorData(x, z, 1, 1);
		
		return FoliageColors.getColor(cache.temperature[0], cache.humidity[0]);
	}
	
	public static boolean isSodiumInstalled() {
		return sodiumInstalled;
	}

	@Override
	public void onInitializeClient() {
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
//			System.out.println("DEBUG MIXIN AUDIT");
//			MixinEnvironment.getDefaultEnvironment().audit();
		}
		
		sodiumInstalled = FabricLoader.getInstance().isModLoaded("sodium");
	}
	
	@SuppressWarnings("resource")
	public static void reloadChunks() {
		MinecraftClient.getInstance().worldRenderer.reload();
	}
}