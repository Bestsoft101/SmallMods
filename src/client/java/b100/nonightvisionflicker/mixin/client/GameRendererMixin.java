package b100.nonightvisionflicker.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;

@Mixin(value = GameRenderer.class)
public class GameRendererMixin {
	
	@Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
	private static void overrideGetNightVisionStrength(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> ci) {
		ci.setReturnValue(entity.hasStatusEffect(StatusEffects.NIGHT_VISION) ? 1.0f : 0.0f);
		ci.cancel();
	}
	
}
