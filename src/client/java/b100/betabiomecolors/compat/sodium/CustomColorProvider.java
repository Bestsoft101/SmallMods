package b100.betabiomecolors.compat.sodium;

import b100.betabiomecolors.BetaBiomeColorsConfig;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;

public class CustomColorProvider implements ColorProvider<BlockState> {
	
	public BlockColorProvider color;
	public ColorProvider<BlockState> original;
	
	public CustomColorProvider(BlockColorProvider color, ColorProvider<BlockState> original) {
		this.color = color;
		this.original = original;
	}

	@Override
	public void getColors(LevelSlice slice, BlockPos pos, Mutable scratchPos, BlockState state, ModelQuadView quad, int[] output) {
		if(!BetaBiomeColorsConfig.modEnabled) {
			original.getColors(slice, pos, scratchPos, state, quad, output);
			return;
		}
		
		int x = pos.getX();
		int z = pos.getZ();
		
		if(BetaBiomeColorsConfig.useSodiumLinearInterpolation) {
			// TODO This only works on the top face of blocks
			output[0] = color.getColor(x, z);
			output[1] = color.getColor(x, z + 1);
			output[2] = color.getColor(x + 1, z + 1);
			output[3] = color.getColor(x + 1, z);	
		}else {
			int col = color.getColor(x, z);
			output[0] = col;
			output[1] = col;
			output[2] = col;
			output[3] = col;
		}
	}
	
	public static interface BlockColorProvider {
		
		public int getColor(int x, int z);
		
	}
}
