package io.github.jason13official.peaceful_hunger.impl.common.network.packet;

import io.github.jason13official.peaceful_hunger.PeacefulHunger;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.Difficulty;

public record ConfigSyncS2CPacket(Difficulty difficulty) implements CustomPacketPayload {

  public static final Type<ConfigSyncS2CPacket> TYPE = new Type<>(PeacefulHunger.identifier("config_sync"));

  public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncS2CPacket> STREAM_CODEC =
      StreamCodec.composite(
          Difficulty.STREAM_CODEC, ConfigSyncS2CPacket::difficulty,
          ConfigSyncS2CPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
