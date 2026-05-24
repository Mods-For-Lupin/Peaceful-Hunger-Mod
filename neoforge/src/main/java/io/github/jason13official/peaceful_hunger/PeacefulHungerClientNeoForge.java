package io.github.jason13official.peaceful_hunger;

import io.github.jason13official.peaceful_hunger.impl.common.ModConfig;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

public class PeacefulHungerClientNeoForge {

  public PeacefulHungerClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> PeacefulHungerClient.init());

    NeoForge.EVENT_BUS.addListener((Consumer<EntityLeaveLevelEvent>) event -> {
      if (Minecraft.getInstance().player != null && event.getEntity() == Minecraft.getInstance().player) {
        ModConfig.unsync();
      }
    });
  }
}
