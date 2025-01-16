package b100.continuousmusic.access;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.MusicSound;

public interface MusicTrackerAccess {
	
	public SoundInstance getCurrent();
	
	public void setCurrent(SoundInstance sound);
	
	public void play(MusicSound type);
	
}
