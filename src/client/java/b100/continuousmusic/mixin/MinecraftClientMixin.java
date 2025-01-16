package b100.continuousmusic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;

@Mixin(value = MinecraftClient.class)
public class MinecraftClientMixin {
	
	@WrapOperation(
		method = "reset(Lnet/minecraft/client/gui/screen/Screen;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/sound/SoundManager;stopAll()V"
		)
	)
	private void dontResetSounds(SoundManager instance, Operation<Void> operation) {
		
	}
	
	
}
