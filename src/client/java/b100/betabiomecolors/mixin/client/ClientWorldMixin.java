package b100.betabiomecolors.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import b100.betabiomecolors.BetaBiomeColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.ColorResolver;

@Mixin(value = ClientWorld.class)
public class ClientWorldMixin {
	
//	@Shadow
//	private Object2ObjectArrayMap<ColorResolver, BiomeColorCache> colorCache;
//	
//	@Inject(method = "<init>", at = @At("TAIL"))
//	private void onInit(ClientPlayNetworkHandler c, ClientWorld.Properties p, RegistryKey<?> k, RegistryEntry<?> e, int i0, int i1, Supplier<?> s, WorldRenderer w, boolean b, long l, CallbackInfo ci) {
//		
//	}
//	
//	@Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
//	private void onGetColor(BlockPos pos, ColorResolver colorResolver, CallbackInfoReturnable<Integer> ci) {
//	}
	
	@Inject(method = "calculateColor", at = @At("HEAD"), cancellable = true)
	private void onCalculateColor(BlockPos pos, ColorResolver colorResolver, CallbackInfoReturnable<Integer> ci) {
		// Only effective without sodium
		if(colorResolver == BiomeColors.GRASS_COLOR) {
			ci.setReturnValue(BetaBiomeColors.getGrassColor(pos));
		}else if(colorResolver == BiomeColors.FOLIAGE_COLOR) {
			ci.setReturnValue(BetaBiomeColors.getFoliageColor(pos));
		}
	}
}
