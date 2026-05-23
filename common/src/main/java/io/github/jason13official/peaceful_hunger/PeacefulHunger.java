package io.github.jason13official.peaceful_hunger;

import net.minecraft.resources.Identifier;

public class PeacefulHunger {

  public static void init() {
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}