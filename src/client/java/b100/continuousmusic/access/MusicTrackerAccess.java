package b100.continuousmusic.access;

import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.SoundInstance;

public interface MusicTrackerAccess {
	
	public SoundInstance getCurrent();
	
	public void setCurrent(SoundInstance sound);
	
	public void play(MusicInstance type);
	
}
