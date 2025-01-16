package b100.continuousmusic.access;

import net.minecraft.client.sound.SoundInstance;

public interface SourceAccess {

	public boolean isBackgroundMusic();
	
	public void setSoundInstance(SoundInstance soundInstance);
	
}
