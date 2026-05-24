package io.github.jason13official.peaceful_hunger.impl.common.network.packet;

import io.github.jason13official.peaceful_hunger.PeacefulHunger;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap.OutOfBoundsStrategy;
import net.minecraft.world.Difficulty;

public record ConfigSyncS2CPacket(Difficulty difficulty) implements CustomPacketPayload {

  public static final Type<ConfigSyncS2CPacket> TYPE = new Type<>(PeacefulHunger.identifier("config_sync"));

  /// backport from 26.1.2 as the stream codec doesn't exist in this version
  private static final IntFunction<Difficulty> DIFFICULTY_BY_ID = ByIdMap.continuous(Difficulty::getId, Difficulty.values(), OutOfBoundsStrategy.WRAP);

  /// backport from 26.1.2 as the stream codec doesn't exist in this version
  public static final StreamCodec<ByteBuf, Difficulty> DIFFICULTY_STREAM_CODEC = ByteBufCodecs.idMapper(DIFFICULTY_BY_ID, Difficulty::getId);

  public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncS2CPacket> STREAM_CODEC =
      StreamCodec.composite(
          DIFFICULTY_STREAM_CODEC, ConfigSyncS2CPacket::difficulty,
          ConfigSyncS2CPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
