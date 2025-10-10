package b100.moreanimals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;

@Mixin(value = SpawnHelper.class)
public class SpawnHelperMixin {
	
	@WrapOperation(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/SpawnGroup;isRare()Z"))
	private static boolean wrapIsRare(SpawnGroup spawnGroup, Operation<Boolean> original) {
		if(spawnGroup == SpawnGroup.CREATURE) {
			return false;
		}
		return original.call(spawnGroup);
	}
	
}
