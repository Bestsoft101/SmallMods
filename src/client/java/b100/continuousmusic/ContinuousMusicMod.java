package b100.continuousmusic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.sound.SoundInstance;

public class ContinuousMusicMod {
	
	public static final String MODID = "continuousmusic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	
	public static boolean isBackgroundMusic(SoundInstance soundInstance) {
		return soundInstance.getId().getPath().startsWith("music.");
	}
	
	public static void print(String string) {
		System.out.print(string + "\n");
	}
}