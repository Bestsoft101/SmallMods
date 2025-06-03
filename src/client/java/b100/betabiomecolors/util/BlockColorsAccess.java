package b100.betabiomecolors.util;

import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.state.property.Property;
import net.minecraft.util.collection.IdList;

public interface BlockColorsAccess {

	public IdList<BlockColorProvider> providers();
	
	public Map<Block, Set<Property<?>>> properties();
	
}
