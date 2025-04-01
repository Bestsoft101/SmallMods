package b100.fastminecart;

import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;

public class FastMinecartMod implements ModInitializer {

    public static GameRules.Key<GameRules.IntRule> MINECART_SPEED_MULTIPLIER;

    @Override
    public void onInitialize() {
        MINECART_SPEED_MULTIPLIER = GameRuleRegistry
                .register("minecartSpeedMultiplier", Category.MISC, GameRuleFactory.createIntRule(2));
    }
}
