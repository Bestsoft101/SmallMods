package b100.continuousmusic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.continuousmusic.ContinuousMusicMod;
import b100.continuousmusic.access.MusicTrackerAccess;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.MusicSound;

@Mixin(value = MusicTracker.class)
public abstract class MusicTrackerMixin implements MusicTrackerAccess {
	
	@Shadow
	private int timeUntilNextSong;
	@Shadow
	private SoundInstance current;
	
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void beforeTick(CallbackInfo ci) {
		ci.cancel();
		
		ContinuousMusicMod.tickMusic(this);
	}
	
	@Inject(method = "stop(Lnet/minecraft/sound/MusicSound;)V", at = @At("HEAD"), cancellable = true)
	private void onStop(MusicSound type, CallbackInfo ci) {
		ci.cancel();
	}
	
	@Inject(method = "stop()V", at = @At("HEAD"), cancellable = true)
	private void onStop(CallbackInfo ci) {
		ci.cancel();	
	}
	
	@Override
	public void setCurrent(SoundInstance sound) {
		current = sound;
	}
	
	@Override
	public SoundInstance getCurrent() {
		return current;
	}
	
	@Shadow
	public abstract void play(MusicInstance type);
	
}
