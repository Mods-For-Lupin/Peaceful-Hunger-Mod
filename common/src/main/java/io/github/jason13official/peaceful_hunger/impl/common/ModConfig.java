package io.github.jason13official.peaceful_hunger.impl.common;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.peaceful_hunger.Constants;
import io.github.jason13official.peaceful_hunger.impl.common.network.packet.ConfigSyncS2CPacket;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.world.Difficulty;

public class ModConfig {

  private static ModConfig INSTANCE = new ModConfig();
  private static boolean SYNCED_TO_REMOTE = false;
  public Difficulty hungerDifficulty = Difficulty.EASY;

  public static ModConfig get() {
    return INSTANCE;
  }

  public static void unsync() {
    SYNCED_TO_REMOTE = false;
  }

  public static void load(Path configDir) {

    if (SYNCED_TO_REMOTE) {
      return;
    }

    Path file = configDir.resolve(Constants.MOD_ID + "-server.toml");

    try {
      Files.createDirectories(configDir);
    } catch (Exception e) {
      Constants.LOG.error("Failed to create config directory, using defaults", e);
      return;
    }

    try (CommentedFileConfig config = CommentedFileConfig.builder(file.toFile()).build()) {
      if (Files.exists(file)) {
        config.load();
      }

      ModConfig loaded = new ModConfig();
      loaded.hungerDifficulty = config.getOrElse("hunger_difficulty", Difficulty.EASY);

      INSTANCE = loaded;

      config.setComment("hunger_difficulty", " The difficulty used for depleting saturation / food levels");
      config.set("hunger_difficulty", INSTANCE.hungerDifficulty);
      config.save();
    } catch (Exception e) {
      Constants.LOG.error("Failed to load Peaceful Hunger config, using defaults", e);
      INSTANCE = new ModConfig();
    }
  }

  public void sync(ConfigSyncS2CPacket packet) {
    hungerDifficulty = packet.difficulty();
    SYNCED_TO_REMOTE = true;
  }
}
