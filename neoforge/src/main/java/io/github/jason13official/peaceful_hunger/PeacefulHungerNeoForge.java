package io.github.jason13official.peaceful_hunger;

import java.util.function.Consumer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@Mod(Constants.MOD_ID)
public class PeacefulHungerNeoForge {

  public static IEventBus EVENT_BUS;

  public PeacefulHungerNeoForge(final IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> PeacefulHunger.init());

    NeoForge.EVENT_BUS.addListener((Consumer<AddReloadListenerEvent>) event -> {
      event.addListener(new ResourceReloadListener());
    });

    if (FMLLoader.getDist() == Dist.CLIENT) {
      new PeacefulHungerClientNeoForge(EVENT_BUS);
    }
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return PeacefulHunger.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      // ModConfig.load(Services.PLATFORM.getConfigDirectory());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}