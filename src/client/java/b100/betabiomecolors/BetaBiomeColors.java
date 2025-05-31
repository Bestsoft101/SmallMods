package b100.betabiomecolors;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

public class BetaBiomeColors implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			System.out.println("DEBUG MIXIN AUDIT");
//			MixinEnvironment.getDefaultEnvironment().audit();
		}
	}
	
	private static NoiseGeneratorOctaves2 field_4194_e;
	private static NoiseGeneratorOctaves2 field_4193_f;
	private static NoiseGeneratorOctaves2 field_4192_g;

	static {
		setSeed(1619655818957931509L);
	}
	
	public static void setSeed(long seed) {
		field_4194_e = new NoiseGeneratorOctaves2(new Random(seed * 9871L), 4);
		field_4193_f = new NoiseGeneratorOctaves2(new Random(seed * 39811L), 4);
		field_4192_g = new NoiseGeneratorOctaves2(new Random(seed * 543321L), 2);
	}
	
	public static int getGrassColor(BlockPos pos) {
		int x = pos.getX();
		int z = pos.getZ();
		return getGrassColor(x, z);
	}
	
	public static int getFoliageColor(BlockPos pos) {
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
	
	public static class ThreadedCache {
		
		private static Map<Thread, ThreadedCache> caches = new HashMap<>();
		
		public static ThreadedCache get() {
			Thread thread = Thread.currentThread();
			ThreadedCache cache = caches.get(thread);
			if(cache == null) {
				cache = new ThreadedCache();
				caches.put(thread, cache);
			}
			return cache;
		}
		
		////////////////////////////////
		
		public double[] temperature;
		public double[] humidity;
		public double[] field_4196_c;
		
		private int lastX;
		private int lastZ;
		private int lastW;
		private int lastH;
		
		public void loadBlockGeneratorData(int x, int z, int w, int h) {
			if(x == lastX && z == lastZ && w == lastW && h == lastH) {
				return;
			}
			
			lastX = x;
			lastZ = z;
			lastW = w;
			lastH = h;
			
			temperature = field_4194_e.func_4112_a(temperature, (double) x, (double) z, w, w, 0.025, 0.025, 0.25);
			humidity = field_4193_f.func_4112_a(humidity, (double) x, (double) z, w, w, 0.05, 0.05, 1.0 / 3.0);
			field_4196_c = field_4192_g.func_4112_a(field_4196_c, (double) x, (double) z, w, w, 0.25, 0.25, 0.5882352941176471);
			int index = 0;
			
			for(int i = 0; i < w; i++) {
				for(int j = 0; j < h; j++) {
					double variation = field_4196_c[index] * 1.1 + 0.5;
					
					double var11 = 0.01;
					double var13 = 1.0 - var11;
					
					double temp = (temperature[index] * 0.15 + 0.7) * var13 + variation * var11;
					var11 = 0.002;
					var13 = 1.0 - var11;
					
					double rain = (humidity[index] * 0.15 + 0.5) * var13 + variation * var11;
					temp = 1.0 - (1.0 - temp) * (1.0 - temp);
					
					temp = MathHelper.clamp(temp, 0.0, 1.0);
					rain = MathHelper.clamp(rain, 0.0, 1.0);
					
					temperature[index] = temp;
					humidity[index] = rain;
					index++;
				}
			}
		}
	}
}