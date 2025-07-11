package b100.betabiomecolors.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import b100.betabiomecolors.BetaBiomeColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Mixin(value = BiomeColors.class)
public class BiomeColorsMixin {
	
	@Inject(method = "getGrassColor", at = @At("HEAD"), cancellable = true)
	private static void onGetGrassColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(BetaBiomeColors.getGrassColor(pos));
	}
	
}
