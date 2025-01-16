package b100.continuousmusic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import b100.continuousmusic.access.SoundManagerAccess;
import b100.continuousmusic.access.SoundSystemAccess;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;

@Mixin(value = SoundManager.class)
public class SoundManagerMixin implements SoundManagerAccess {

	@Shadow
	private SoundSystem soundSystem;
	
	@Override
	public boolean isSoundSystemStarted() {
		SoundSystemAccess soundSystemAccess = (SoundSystemAccess) soundSystem;
		
		return soundSystemAccess.isStarted();
	}
}
