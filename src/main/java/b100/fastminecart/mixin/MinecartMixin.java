package b100.fastminecart.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import b100.fastminecart.FastMinecartMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

@Mixin(value = AbstractMinecartEntity.class)
public abstract class MinecartMixin extends Entity {

	public MinecartMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	private boolean isRepeat = false;

	@Inject(method = "tick", at = @At("HEAD"))
	public void tickInject(CallbackInfo ci) {
		if (!isRepeat) {
			isRepeat = true;
			World world = this.getWorld();
			if (world != null) {
				MinecraftServer server = world.getServer();
				if (server != null) {
					int multiplier = server.getGameRules().getInt(FastMinecartMod.MINECART_SPEED_MULTIPLIER);
					for (int i = 1; i < multiplier; i++) {
						tick();
					}
				}
			}
			isRepeat = false;
		}
	}

}
