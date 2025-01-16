package b100.continuousmusic.mixin;

import java.util.Map;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import b100.continuousmusic.TickChannelThread;
import b100.continuousmusic.access.SoundSystemAccess;
import b100.continuousmusic.access.SourceAccess;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.Source;
import net.minecraft.resource.ResourceFactory;

@Mixin(value = SoundSystem.class)
public class SoundSystemMixin implements SoundSystemAccess {

	@Shadow private Channel channel;
	@Shadow private boolean started;
	@Shadow private Map<SoundInstance, Channel.SourceManager> sources;
	@Shadow private Map<SoundInstance, Integer> soundEndTicks;
	@Shadow private int ticks;
	
	@Inject(
		method = "<init>",
		at = @At("TAIL")
	)
	private void onInit(SoundManager loader, GameOptions settings, ResourceFactory resourceFactory, CallbackInfo ci) {
		new TickChannelThread(channel);
	}
	
	@ModifyArg(
		method = "play",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/sound/Channel$SourceManager;run(Ljava/util/function/Consumer;)V"
		)
	)
	private Consumer<Source> setIsBackgroundMusic(Consumer<Source> original, @Local SoundInstance sound) {
		return (source) -> {
			original.accept(source);
			
			SourceAccess sourceAccess = (SourceAccess) source;
			sourceAccess.setSoundInstance(sound);
		};
	}
	
	@WrapOperation(
		method = "tick(Z)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/sound/Channel;tick()V"
		)
	)
	private void dontTickChannel(Channel instance, Operation<Void> operation) {
		
	}

	@Override
	public boolean isStarted() {
		return started;
	}
}
