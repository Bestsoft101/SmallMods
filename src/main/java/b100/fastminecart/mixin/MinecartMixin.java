package b100.fastminecart.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;

@Mixin(value = AbstractMinecartEntity.class)
public abstract class MinecartMixin extends Entity {
	
	public MinecartMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	private boolean isRepeat = false;
	
	@Inject(method = "tick", at = @At("HEAD"))
	public void tickInject(CallbackInfo ci) {
		if(!isRepeat) {
			isRepeat = true;
			tick();
			isRepeat = false;
		}	
	}

}
