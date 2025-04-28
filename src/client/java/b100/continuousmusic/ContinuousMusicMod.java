package b100.continuousmusic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b100.continuousmusic.access.MusicTrackerAccess;
import b100.continuousmusic.access.SoundManagerAccess;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Channel;
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
	public static final boolean ENABLE_TICK_THREAD = loadConfig();
	
	private static long lastBufferUpdate = 0;
	
	private static Channel channel;
	
	static {
		print("Enable Tick Thread: " + ENABLE_TICK_THREAD);
	}
	
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
			MusicSound musicType = mc.getMusicType();
			
			debug("Play Music: " + ContinuousMusicMod.getName(musicType.getSound()));
			musicTracker.play(musicType);
		}
	}
	
	public static void setChannel(Channel channel) {
		ContinuousMusicMod.channel = channel;
	}
	
	public static void tickChannel() {
		if(channel == null) {
			return;
		}
		long now = System.currentTimeMillis();
		if(now - lastBufferUpdate > 100) {
			lastBufferUpdate = now;
			channel.tick();
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
	
	private static boolean loadConfig() {
		boolean enableThread = false;
		boolean configFileIsValid = false;
		File configFolder = Paths.get("config").toFile();
		File configFile = new File(configFolder, "continuousMusic.cfg");
		if(configFile.exists()) {
			InputStream in = null;
			BufferedReader br = null;
			try {
				in = new FileInputStream(configFile);
				br = new BufferedReader(new InputStreamReader(in));
				while(true) {
					String line = br.readLine();
					if(line == null) {
						break;
					}
					line = line.trim();
					if(line.length() == 0 || line.startsWith("#")) {
						continue;
					}
					String[] split = line.split("=");
					if(split[0].equals("enableTickThread")) {
						enableThread = split[1].equalsIgnoreCase("true");
						configFileIsValid = true;
					}
				}
			}catch (Exception e) {
				System.err.println("Couldn't read config file: " + configFile.getAbsolutePath());
				e.printStackTrace();
			}finally {
				try {
					in.close();
				}catch (Exception e) {}
			}
		}
		if(!configFileIsValid) {
			if(!configFolder.exists()) {
				configFolder.mkdirs();
			}
			FileWriter fw = null;
			try {
				fw = new FileWriter(configFile);
				fw.write("# Turn this option on if you experience the music cutting off, turn it off if you experience crashes\n");
				fw.write("enableTickThread=false");
				fw.flush();
			}catch (Exception e) {
				System.err.println("Couldn't write config file: " + configFile.getAbsolutePath());
				e.printStackTrace();
			}finally {
				try {
					fw.close();
				}catch (Exception e) {}
			}
		}
		return enableThread;
	}
}