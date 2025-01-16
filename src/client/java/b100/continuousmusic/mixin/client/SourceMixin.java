package b100.continuousmusic.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.continuousmusic.SourceAccess;
import net.minecraft.client.sound.Source;

@Mixin(value = Source.class)
public class SourceMixin implements SourceAccess {
	
	public boolean isBackgroundMusic;
	
	@Inject(method = "pause", at = @At("HEAD"), cancellable = true)
	public void cancelPause(CallbackInfo ci) {
		if(isBackgroundMusic) {
			ci.cancel();	
		}
	}

	@Override
	public boolean isBackgroundMusic() {
		return isBackgroundMusic;
	}

	@Override
	public void setIsBackgroundMusic(boolean isBackgroundMusic) {
		this.isBackgroundMusic = isBackgroundMusic;
	}
	
}
