package io.github.jason13official.peaceful_hunger;

import io.github.jason13official.peaceful_hunger.impl.common.ModConfig;
import io.github.jason13official.peaceful_hunger.impl.common.network.packet.ConfigSyncS2CPacket;
import io.github.jason13official.peaceful_hunger.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class PeacefulHungerFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    PeacefulHunger.init();
    PeacefulHunger.clientBoundPacketSender = ServerPlayNetworking::send;

    // 26.1.2
    // PayloadTypeRegistry.clientboundPlay().register(ConfigSyncS2CPacket.TYPE, ConfigSyncS2CPacket.STREAM_CODEC);
    // 1.21.1
    PayloadTypeRegistry.playS2C().register(ConfigSyncS2CPacket.TYPE, ConfigSyncS2CPacket.STREAM_CODEC);

    ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
      if (entity instanceof ServerPlayer serverPlayer) {
        PeacefulHunger.clientBoundPacketSender.accept(serverPlayer, new ConfigSyncS2CPacket(ModConfig.get().hungerDifficulty));
      }
    });

    // 26.1.2
    // DataResourceLoader.get().registerReloadListener(PeacefulHunger.identifier(Constants.MOD_ID), new ResourceReloadListener());
    // 1.21.1
    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(PeacefulHunger.identifier(Constants.MOD_ID), ResourceReloadListener::new);
  }

  public static class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {

    public ResourceReloadListener(HolderLookup.Provider provider) {
    }

    @Override
    public ResourceLocation getFabricId() {
      return PeacefulHunger.identifier(Constants.MOD_ID);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
      ModConfig.load(Services.PLATFORM.getConfigDirectory());
    }
  }

//  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {
//
//    @Override
//    public String getName() {
//      return PeacefulHunger.identifier(Constants.MOD_ID).toString();
//    }
//
//    @Override
//    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
//      ModConfig.load(Services.PLATFORM.getConfigDirectory());
//    }
//
//    @Override
//    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
//      return null;
//    }
//  }
}
