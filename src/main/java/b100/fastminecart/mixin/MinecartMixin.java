package b100.fastminecart.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;

@Mixin(value = AbstractMinecart.class)
public abstract class MinecartMixin extends Entity {
	
	public MinecartMixin(EntityType<?> type, Level world) {
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
