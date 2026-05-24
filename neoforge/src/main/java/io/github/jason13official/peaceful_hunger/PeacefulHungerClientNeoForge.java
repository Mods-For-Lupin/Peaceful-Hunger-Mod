package io.github.jason13official.peaceful_hunger;

import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class PeacefulHungerClientNeoForge {

  public PeacefulHungerClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> PeacefulHungerClient.init());
  }
}
