package io.github.jason13official.examplemod.mixin;

import com.mojang.authlib.GameProfile;
import io.github.jason13official.examplemod.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

  /// dummy
  protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
    super(entityType, level);
  }

  @Inject(at = @At("TAIL"), method = "<init>")
  private void examplemod$playerInstanceInit(Level level, BlockPos pos, float yRot, GameProfile gameProfile, CallbackInfo ci) {
    Constants.LOG.info("New player created with UUID {}", this.getUUID());
  }
}
