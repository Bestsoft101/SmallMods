package b100.continuousmusic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.continuousmusic.ContinuousMusicMod;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(value = GameRenderer.class)
public class GameRendererMixin {
	
	@Inject(method = "render", at = @At("TAIL"))
	private void onRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
		if(!ContinuousMusicMod.ENABLE_TICK_THREAD) {
			ContinuousMusicMod.tickChannel();	
		}
	}
	
}
