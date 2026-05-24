package io.github.jason13official.peaceful_hunger.mixin;

import io.github.jason13official.peaceful_hunger.impl.common.ModConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FoodData.class)
public class FoodDataMixin {

  @ModifyVariable(method = "tick", at = @At("STORE"), name = "difficulty")
  private Difficulty peaceful_hunger$tick(Difficulty difficulty) {
    return difficulty == Difficulty.PEACEFUL ? ModConfig.get().hungerDifficulty : difficulty;
  }
}
