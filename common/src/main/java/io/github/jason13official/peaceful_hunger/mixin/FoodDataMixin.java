package io.github.jason13official.peaceful_hunger.mixin;

import io.github.jason13official.peaceful_hunger.Constants;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

  @Inject(at = @At("TAIL"), method = "<init>")
  private void peaceful_hunger$playerInstanceInit(CallbackInfo ci) {
    Constants.LOG.info("New food data instance created.");
  }
}
