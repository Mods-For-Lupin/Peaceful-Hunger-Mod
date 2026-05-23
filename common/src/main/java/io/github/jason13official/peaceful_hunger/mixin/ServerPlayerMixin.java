package io.github.jason13official.peaceful_hunger.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

  /// dummy
  public ServerPlayerMixin(Level level, GameProfile gameProfile) {
    super(level, gameProfile);
  }

  @Shadow
  public abstract ServerLevel level();

  @Inject(at = @At("HEAD"), method = "tickRegeneration", cancellable = true)
  private void peaceful_hunger$tickRegeneration(CallbackInfo ci) {
    if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.level().getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION)) {
      ci.cancel();
    }
  }
}
