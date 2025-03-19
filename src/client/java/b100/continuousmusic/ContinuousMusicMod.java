package b100.continuousmusic;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b100.continuousmusic.access.MusicTrackerAccess;
import b100.continuousmusic.access.SoundManagerAccess;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundCategory;

public class ContinuousMusicMod {

	public static final boolean INDEV = FabricLoader.getInstance().isDevelopmentEnvironment();
	public static final String MODID = "continuousmusic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	
	public static void tickMusic(MusicTrackerAccess musicTracker) {
		MinecraftClient mc = MinecraftClient.getInstance();
		
		SoundManager soundManager = mc.getSoundManager();
		SoundManagerAccess soundManagerAccess = (SoundManagerAccess) soundManager;
		
		if(!soundManagerAccess.isSoundSystemStarted()) {
			return;
		}
		
		float musicVolume = mc.options.getSoundVolume(SoundCategory.MUSIC);
		if(musicVolume <= 0.0f) {
			musicTracker.setCurrent(null);
			return;
		}

		SoundInstance current = musicTracker.getCurrent();
		if(current != null && !soundManager.isPlaying(current)) {
			debug("Stopped Playing: " + current.getId());
			musicTracker.setCurrent(null);
		}
		
		if(musicTracker.getCurrent() == null) {
			MusicInstance music = mc.getMusicInstance();
			MusicSound musicSound = music.music();
			
			debug("Play Music: " + ContinuousMusicMod.getName(musicSound.getSound()));
			musicTracker.play(music);
		}
	}
	
	public static boolean isBackgroundMusic(SoundInstance soundInstance) {
		return soundInstance.getId().getPath().startsWith("music.");
	}
	
	public static String getName(RegistryEntry<?> entry) {
		Optional<?> key = entry.getKey();
		if(key.isPresent()) {
			RegistryKey<?> obj = (RegistryKey<?>) key.get();
			return obj.getValue().toString();
		}
		return null;
	}
	
	public static void print(String string) {
		if(INDEV) {
			System.out.print(string + "\n");	
		}else {
			LOGGER.info(string);	
		}
	}
	
	public static void debug(String string) {
		if(INDEV) {
			System.out.print(string + "\n");	
		}
	}
}