package b100.continuousmusic.mixin.client;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;

import b100.continuousmusic.ContinuousMusicMod;
import b100.continuousmusic.SourceAccess;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.Source;

@Mixin(value = SoundSystem.class)
public class SoundSystemMixin {
	
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
			sourceAccess.setIsBackgroundMusic(ContinuousMusicMod.isBackgroundMusic(sound));
		};
		
	}
	
}
