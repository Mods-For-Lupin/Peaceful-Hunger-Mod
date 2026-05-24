package io.github.jason13official.peaceful_hunger.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jason13official.peaceful_hunger.impl.common.ModConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

  protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
    super(entityType, level);
  }

  /// effectively cancels the regeneration block from firing
  @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getDifficulty()Lnet/minecraft/world/Difficulty;"))
  private Difficulty peaceful_hunger$aiStep$getDifficultyRedirect(Level instance) {
    return instance.getDifficulty() == Difficulty.PEACEFUL ? ModConfig.get().hungerDifficulty : instance.getDifficulty();
  }

//  @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/Level;getDifficulty()Lnet/minecraft/world/Difficulty;"), method = "aiStep", cancellable = true)
//  private void peaceful_hunger$aiStep$getDifficultyInject(CallbackInfo ci) {
//
//  }

//  @Shadow
//  public abstract ServerLevel level();
//
//  @Inject(at = @At("HEAD"), method = "tickRegeneration", cancellable = true)
//  private void peaceful_hunger$tickRegeneration(CallbackInfo ci) {
//    if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.level().getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION)) {
//      ci.cancel();
//    }
//  }
}
