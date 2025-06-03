package b100.betabiomecolors.util;

import java.util.HashMap;
import java.util.Map;

import b100.betabiomecolors.BetaBiomeColors;
import net.minecraft.util.math.MathHelper;

public class ThreadedCache {
	
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
		
		temperature = BetaBiomeColors.field_4194_e.func_4112_a(temperature, (double) x, (double) z, w, w, 0.025, 0.025, 0.25);
		humidity = BetaBiomeColors.field_4193_f.func_4112_a(humidity, (double) x, (double) z, w, w, 0.05, 0.05, 1.0 / 3.0);
		field_4196_c = BetaBiomeColors.field_4192_g.func_4112_a(field_4196_c, (double) x, (double) z, w, w, 0.25, 0.25, 0.5882352941176471);
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