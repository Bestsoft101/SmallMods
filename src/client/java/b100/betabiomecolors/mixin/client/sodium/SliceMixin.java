package b100.betabiomecolors.mixin.client.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import b100.betabiomecolors.SliceAccess;
import net.caffeinemc.mods.sodium.client.util.color.BoxBlur;
import net.caffeinemc.mods.sodium.client.util.color.BoxBlur.ColorBuffer;

@Mixin(targets = {"net/caffeinemc/mods/sodium/client/world/biome/LevelColorCache$Slice"})
public class SliceMixin implements SliceAccess {

	@Shadow
	private BoxBlur.ColorBuffer buffer;
	
	@Shadow
	private long lastPopulateStamp;

	@Override
	public ColorBuffer getBuffer() {
		return buffer;
	}

	@Override
	public long getLastPopulateStamp() {
		return lastPopulateStamp;
	}

	@Override
	public void setLastPopulateStamp(long l) {
		lastPopulateStamp = l;
	}
}
