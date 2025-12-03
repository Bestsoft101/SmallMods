package b100.betabiomecolors.mixin.client.sodium;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.betabiomecolors.BetaBiomeColors;
import b100.betabiomecolors.compat.sodium.CustomColorProvider;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.color.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;

@Mixin(value = ColorProviderRegistry.class, remap = false)
public abstract class ColorProviderRegistryMixin {

	@Shadow
	private Reference2ReferenceMap<Block, ColorProvider<BlockState>> blocks;
	@Shadow
	private Reference2ReferenceMap<Fluid, ColorProvider<FluidState>> fluids;
	
	@Inject(method = "installOverrides", at = @At("TAIL"))
	private void onInstallOverrides(CallbackInfo ci) {
		// Get color providers for grass and oak leaves
		ColorProvider<BlockState> originalGrassColor = blocks.get(Blocks.GRASS_BLOCK);
		ColorProvider<BlockState> originalFoliageColor = blocks.get(Blocks.OAK_LEAVES);
		
		// Find all blocks that use the same color provider
		List<Block> grassColorBlocks = new ArrayList<Block>();
		List<Block> foliageColorBlocks = new ArrayList<Block>();
		
		for(Block block : blocks.keySet()) {
			ColorProvider<BlockState> blockColor = blocks.get(block);
			if(blockColor == originalGrassColor) {
				grassColorBlocks.add(block);
			}
			if(blockColor == originalFoliageColor) {
				foliageColorBlocks.add(block);
			}
		}
		
		// Replace color providers for all those blocks
		CustomColorProvider customGrassColor = new CustomColorProvider((x, z) -> BetaBiomeColors.getGrassColor(x, z), originalGrassColor);
		CustomColorProvider customFoliageColor = new CustomColorProvider((x, z) -> BetaBiomeColors.getFoliageColor(x, z), originalFoliageColor);
		
		for(Block block : grassColorBlocks) {
			blocks.put(block, customGrassColor);
		}
		for(Block block : foliageColorBlocks) {
			blocks.put(block, customFoliageColor);
		}
	}
	
	@Shadow
	protected abstract void registerBlocks(ColorProvider<BlockState> provider, Block... blocks);
	
	@Shadow
	protected abstract void registerFluids(ColorProvider<BlockState> provider, Fluid... blocks);
	
}
