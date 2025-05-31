package b100.betabiomecolors;

import net.caffeinemc.mods.sodium.client.util.color.BoxBlur;

public interface SliceAccess {
	
	public BoxBlur.ColorBuffer getBuffer();
	
	public long getLastPopulateStamp();
	
	public void setLastPopulateStamp(long l);
	
}
