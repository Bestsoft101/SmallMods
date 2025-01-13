package b100.miningmod;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;

public class MiningMod {
	
	public static boolean enableHasteEffect = false;
	
	public static final boolean INDEV = FabricLoader.getInstance().isDevelopmentEnvironment();
	
	public static int getBlockHitDelay(PlayerEntity player, float delta) {
		int newBlockHitDelay;
		if(delta >= 1.0f) {
			newBlockHitDelay = 1;
		}else if(delta > 1.0f / 2.0f) {
			newBlockHitDelay = 2;
		}else if(delta > 1.0f / 2.5f) {
			newBlockHitDelay = 3;
		}else if(delta > 1.0f / 3.0f) {
			newBlockHitDelay = 4;
		}else {
			newBlockHitDelay = 5;
		}
		
		int hasteLevel = getHasteLevel(player);
		newBlockHitDelay -= hasteLevel;
		newBlockHitDelay = Math.max(0, newBlockHitDelay);
		
		if(INDEV) {
			print("Delta: " + (int)(delta * 100) + " Haste: " + hasteLevel + " Delay: " + newBlockHitDelay);
		}
		
		return newBlockHitDelay;
	}
	
	public static int getHasteLevel(PlayerEntity player) {
		if(enableHasteEffect && StatusEffectUtil.hasHaste(player)) {
			return StatusEffectUtil.getHasteAmplifier(player) + 1;
		}
		return 0;
	}
	
	public static void print(String string) {
		System.out.print(string + "\n");
	}
	
}
