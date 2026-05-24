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
  private String hungerDifficultyString = "easy";

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

      // get raw value from config
      loaded.hungerDifficultyString = config.getOrElse("hunger_difficulty", "easy");
      Constants.LOG.info("Got difficulty string from config: {}", loaded.hungerDifficultyString);

      // set real value on config
      Difficulty nullableDifficulty = Difficulty.byName(loaded.hungerDifficultyString);;

      if (nullableDifficulty == null) {
        // Constants.LOG.info("Failed to read provided string as difficulty: {}", loaded.hungerDifficultyString);
        String lower = loaded.hungerDifficultyString.toLowerCase();
        // Constants.LOG.info("Trying as lowercase name instead {}", lower);
        nullableDifficulty = Difficulty.byName(lower);
      }

      if (nullableDifficulty == null) {
        Constants.LOG.info("Failed to lowercase provided string and read as difficulty: {}", loaded.hungerDifficultyString.toLowerCase());
        Constants.LOG.info("Using default value \"easy\" for Difficulty.EASY");
        Constants.LOG.info("Supported difficulties: \"peaceful\", \"easy\", \"normal\", or \"hard\"");
        nullableDifficulty = Difficulty.EASY;
      }

      loaded.hungerDifficulty = nullableDifficulty;
      Constants.LOG.info("Set hunger difficulty: {}", loaded.hungerDifficulty.toString());

      // set instance
      INSTANCE = loaded;

      config.setComment("hunger_difficulty", " Supported difficulties: \"peaceful\", \"easy\", \"normal\", or \"hard\"");
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
