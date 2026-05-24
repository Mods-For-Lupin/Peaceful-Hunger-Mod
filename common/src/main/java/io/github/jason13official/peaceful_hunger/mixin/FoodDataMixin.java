package io.github.jason13official.peaceful_hunger.mixin;

import io.github.jason13official.peaceful_hunger.impl.common.ModConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

//  @Inject(at = @At("TAIL"), method = "<init>")
//  private void peaceful_hunger$playerInstanceInit(CallbackInfo ci) {
//    Constants.LOG.info("New food data instance created.");
//  }

  @ModifyVariable(method = "tick", at = @At("STORE"))
  private Difficulty peaceful_hunger$tick$modifyFoodDifficulty(Difficulty difficulty) {
    return difficulty == Difficulty.PEACEFUL ? ModConfig.get().hungerDifficulty : difficulty;
  }
}
