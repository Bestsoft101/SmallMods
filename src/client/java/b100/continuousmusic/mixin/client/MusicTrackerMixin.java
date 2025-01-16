package b100.continuousmusic.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundCategory;

@Mixin(value = MusicTracker.class)
public abstract class MusicTrackerMixin {
	
	@Shadow
	private int timeUntilNextSong;
	@Shadow
	private SoundInstance current;
	
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void beforeTick(CallbackInfo ci) {
		ci.cancel();
		
		MinecraftClient mc = MinecraftClient.getInstance();
		
		float musicVolume = mc.options.getSoundVolume(SoundCategory.MUSIC);
		if(musicVolume <= 0.0f) {
			current = null;
			return;
		}
		
		if(current != null && !mc.getSoundManager().isPlaying(current)) {
			current = null;
		}
		
		if(current == null) {
			MusicSound musicType = mc.getMusicType();
			
			play(musicType);
		}
	}
	
	@Inject(method = "stop(Lnet/minecraft/sound/MusicSound;)V", at = @At("HEAD"), cancellable = true)
	private void onStop(MusicSound type, CallbackInfo ci) {
		ci.cancel();
	}
	
	@Inject(method = "stop()V", at = @At("HEAD"), cancellable = true)
	private void onStop(CallbackInfo ci) {
		ci.cancel();	
	}
	
	@Shadow
	public abstract void play(MusicSound type);
	
}
