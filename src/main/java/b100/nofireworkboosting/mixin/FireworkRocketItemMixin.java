package b100.nofireworkboosting.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(value = FireworkRocketItem.class)
public class FireworkRocketItemMixin {
	
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void init(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		if(user.isFallFlying()) {
			ci.setReturnValue(TypedActionResult.fail(user.getStackInHand(hand)));
		}
	}
	
}
