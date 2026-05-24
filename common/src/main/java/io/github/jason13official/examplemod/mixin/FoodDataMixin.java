package io.github.jason13official.examplemod.mixin;

import com.mojang.authlib.GameProfile;
import io.github.jason13official.examplemod.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

  @Inject(at = @At("TAIL"), method = "<init>")
  private void examplemod$playerInstanceInit(CallbackInfo ci) {
    Constants.LOG.info("New food data instance created.");
  }
}
