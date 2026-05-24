package io.github.jason13official.peaceful_hunger;

import io.github.jason13official.peaceful_hunger.impl.common.ModConfig;
import io.github.jason13official.peaceful_hunger.impl.common.network.packet.ConfigSyncS2CPacket;
import io.github.jason13official.peaceful_hunger.impl.common.registry.ModBlocks;
import io.github.jason13official.peaceful_hunger.impl.common.registry.ModEntities;
import io.github.jason13official.peaceful_hunger.impl.common.registry.ModItems;
import io.github.jason13official.peaceful_hunger.impl.common.registry.ModMenus;
import io.github.jason13official.peaceful_hunger.impl.common.registry.ModParticles;
import io.github.jason13official.peaceful_hunger.impl.common.registry.ModTabs;
import io.github.jason13official.peaceful_hunger.impl.common.registry.ModTiles;
import io.github.jason13official.peaceful_hunger.platform.Services;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class PeacefulHungerNeoForge {

  public static IEventBus EVENT_BUS;

  public PeacefulHungerNeoForge(final IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    bind(Registries.BLOCK, ModBlocks::register);
    bind(Registries.ENTITY_TYPE, ModEntities::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.PARTICLE_TYPE, ModParticles::register);
    bind(Registries.BLOCK_ENTITY_TYPE, ModTiles::register);
    bind(Registries.MENU, ModMenus::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);

    EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> {
      PeacefulHunger.init();
      PeacefulHunger.clientBoundPacketSender = PacketDistributor::sendToPlayer;
    });

    EVENT_BUS.addListener((Consumer<RegisterPayloadHandlersEvent>) event -> {
      PayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
      registrar.playToClient(ConfigSyncS2CPacket.TYPE, ConfigSyncS2CPacket.STREAM_CODEC);
    });

    NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
      if (event.getEntity() instanceof ServerPlayer serverPlayer) {
        PeacefulHunger.clientBoundPacketSender.accept(serverPlayer, new ConfigSyncS2CPacket(ModConfig.get().hungerDifficulty));
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<AddServerReloadListenersEvent>) event -> {
      event.addListener(PeacefulHunger.identifier(Constants.MOD_ID), new ResourceReloadListener());
    });

    if (FMLLoader.getCurrent().getDist() == Dist.CLIENT) {
      new PeacefulHungerClientNeoForge(EVENT_BUS);
    }
  }

  public <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, Identifier>> source) {

    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return PeacefulHunger.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      ModConfig.load(Services.PLATFORM.getConfigDirectory());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}