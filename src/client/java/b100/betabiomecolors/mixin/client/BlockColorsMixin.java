package b100.betabiomecolors.mixin.client;

import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import b100.betabiomecolors.BetaBiomeColors;
import b100.betabiomecolors.BetaBiomeColorsConfig;
import b100.betabiomecolors.util.BlockColorsAccess;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import net.minecraft.util.collection.IdList;

@Mixin(value = BlockColors.class)
public class BlockColorsMixin implements BlockColorsAccess {

	@Shadow
	private IdList<BlockColorProvider> providers;
	@Shadow
	private Map<Block, Set<Property<?>>> properties;
	
	@Inject(method = "create", at = @At("TAIL"))
	private static void onCreate(CallbackInfoReturnable<BlockColors> cir, @Local BlockColors blockColors) {
		BlockColorsAccess blockColorsAccess = (BlockColorsAccess) blockColors;
		
		IdList<BlockColorProvider> providers = blockColorsAccess.providers();
		
		Block[] grassColorBlocks = new Block[] {
			Blocks.GRASS_BLOCK, Blocks.SHORT_GRASS, Blocks.FERN, Blocks.POTTED_FERN, Blocks.TALL_GRASS, Blocks.LARGE_FERN, Blocks.PINK_PETALS
		};
		Block[] foliageColorBlocks = new Block[] {
			Blocks.OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.VINE, Blocks.MANGROVE_LEAVES
		};
		
		// Modify grass color
		for(Block block : grassColorBlocks) {
			final int id = Registries.BLOCK.getRawId(block);
			final BlockColorProvider original = providers.get(id);
			providers.set((state, world, pos, tintIndex) -> {
				if(BetaBiomeColorsConfig.modEnabled) {
					return BetaBiomeColors.getGrassColor(pos);
				}
				return original.getColor(state, world, pos, tintIndex);
			}, id);
		}
		
		// Modify foliage color
		for(Block block : foliageColorBlocks) {
			final int id = Registries.BLOCK.getRawId(block);
			final BlockColorProvider original = providers.get(id);
			providers.set((state, world, pos, tintIndex) -> {
				if(BetaBiomeColorsConfig.modEnabled) {
					return BetaBiomeColors.getFoliageColor(pos);
				}
				return original.getColor(state, world, pos, tintIndex);
			}, id);
		}
		
//		// Remove sugar cane color
		final BlockColorProvider original = providers.get(Registries.BLOCK.getRawId(Blocks.SUGAR_CANE));
		blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
			if(BetaBiomeColorsConfig.modEnabled) {
				return 0xFFFFFFFF;
			}
			return original.getColor(state, world, pos, tintIndex);
		}, Blocks.SUGAR_CANE);
	}

	@Override
	public IdList<BlockColorProvider> providers() {
		return providers;
	}

	@Override
	public Map<Block, Set<Property<?>>> properties() {
		return properties;
	}
}
