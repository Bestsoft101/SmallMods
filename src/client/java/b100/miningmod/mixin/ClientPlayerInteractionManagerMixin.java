package b100.miningmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import b100.miningmod.MiningMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	
	@Shadow
	private MinecraftClient client;
	
	@Shadow
	private int blockBreakingCooldown;
	
	private BlockState blockBeforeBroken;
	
	@Inject(method = "method_41930", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;breakBlock(Lnet/minecraft/util/math/BlockPos;)Z"))
	private void miningmod_setInstantMineDelay(CallbackInfoReturnable<Boolean> info) {
		// Set block break cooldown after block has been instantmined
		blockBreakingCooldown = MiningMod.getBlockHitDelay(client.player, 100.0f);
	}
	
	@Inject(method = "updateBlockBreakingProgress", at = @At(value = "HEAD"))
	private void miningmod_getBlockStateBeforeBroken(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> ci) {
		blockBeforeBroken = client.world.getBlockState(pos);
	}

	@ModifyConstant(method = "updateBlockBreakingProgress", constant = @Constant(intValue = 5, ordinal = 1))
	private int miningmod_changeBlockHitDelay(int value, BlockPos pos, Direction direction) {
		float delta = blockBeforeBroken.calcBlockBreakingDelta(this.client.player, this.client.world, pos);
		return MiningMod.getBlockHitDelay(client.player, delta);
	}
	
	@Inject(method = "cancelBlockBreaking", at = @At("HEAD"))
	private void onCancelBlockBreaking(CallbackInfo ci) {
		if(blockBreakingCooldown > 0) {
			blockBreakingCooldown--;
		}
	}
	
	@WrapOperation(
		method = "isCurrentlyBreaking",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;areItemsAndComponentsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"
		)
	)
	private boolean removeBlockBreakReset(ItemStack a, ItemStack b, Operation<Boolean> original) {
		return true;
	}
	
}