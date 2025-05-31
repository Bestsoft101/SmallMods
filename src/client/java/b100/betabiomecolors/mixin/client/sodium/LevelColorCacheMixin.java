package b100.betabiomecolors.mixin.client.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.betabiomecolors.BetaBiomeColors;
import b100.betabiomecolors.SliceAccess;
import net.caffeinemc.mods.sodium.client.util.color.BoxBlur;
import net.caffeinemc.mods.sodium.client.world.biome.LevelColorCache;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.world.biome.ColorResolver;

@Mixin(value = LevelColorCache.class)
public class LevelColorCacheMixin {

	@Shadow
	private long populateStamp;

	@Shadow
	private int blendRadius;
	
	@Shadow private int minBlockX;
	@Shadow private int minBlockZ;
	@Shadow private int maxBlockX;
	@Shadow private int maxBlockZ;
	
	@Inject(method = "updateColorBuffers", at = @At("HEAD"), cancellable = true)
	private void onUpdateColorBuffers(int relY, ColorResolver colorResolver, @Coerce Object slice, CallbackInfo ci) {
		if(colorResolver != BiomeColors.FOLIAGE_COLOR && colorResolver != BiomeColors.GRASS_COLOR) {
			return;
		}
		
		ci.cancel();
		SliceAccess sliceAccess = (SliceAccess) slice;
		
		int minBlockZ = this.minBlockZ - this.blendRadius;
		int minBlockX = this.minBlockX - this.blendRadius;
		int maxBlockZ = this.maxBlockZ + this.blendRadius;
		int maxBlockX = this.maxBlockX + this.blendRadius;
		
		BoxBlur.ColorBuffer buffer = sliceAccess.getBuffer();
		
		for (int blockZ = minBlockZ; blockZ <= maxBlockZ; blockZ++) {
			for (int blockX = minBlockX; blockX <= maxBlockX; blockX++) {
				int relBlockX = blockX - minBlockX;
				int relBlockZ = blockZ - minBlockZ;
				
				buffer.set(relBlockX, relBlockZ, colorResolver == BiomeColors.GRASS_COLOR ? BetaBiomeColors.getGrassColor(blockX, blockZ) : BetaBiomeColors.getFoliageColor(blockX, blockZ));
			}
		}
		
		sliceAccess.setLastPopulateStamp(populateStamp);
	}
}

