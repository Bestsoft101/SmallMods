package b100.betabiomecolors;

import java.io.File;

public class BetaBiomeColorsConfig {

	public static boolean modEnabled = true;
	public static long seed = 1619655818957931509L;
	public static boolean useSodiumLinearInterpolation = false;
	
	public static void load(File configFile) {
		if(!configFile.exists()) {
			return;
		}
		
		ConfigUtil.loadConfig(configFile, BetaBiomeColorsConfig::parse, ':');
	}
	
	public static void parse(String key, String value) {
		if(key.equals("modEnabled")) {
			modEnabled = value.equalsIgnoreCase("true");
		}else if(key.equals("seed")) {
			seed = Long.parseLong(value);
		}else if(key.equals("useSodiumLinearInterpolation")) {
			useSodiumLinearInterpolation = value.equalsIgnoreCase("true");
		}
	}
	
	public static void save(File configFile) {
		StringBuilder str = new StringBuilder();

		str.append("modEnabled:").append(modEnabled).append('\n');
		str.append("seed:").append(seed).append('\n');
		str.append("useSodiumLinearInterpolation:").append(useSodiumLinearInterpolation).append('\n');
		
		ConfigUtil.saveStringToFile(str.toString(), configFile);
	}
	
}
