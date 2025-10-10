package b100.moreanimals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.SpawnGroup;

@Mixin(value = SpawnGroup.class)
public class SpawnGroupMixin {
	
	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 10, ordinal = 0))
	private static int adjustCreatureLimit(int original) {
		return 30;
	}
	
	
}
