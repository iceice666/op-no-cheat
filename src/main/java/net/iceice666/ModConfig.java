import org.jetbrains.annotations.NotNull;
import net.fabricmc.loader.api.FabricLoader;
import net.iceice666.lib.Comment;
import net.iceice666.lib.CustomConfig;

import java.nio.file.Path;

public class ModConfig implements CustomConfig {


    @Comment()
    public List<String> blacklist [];


    @NotNull
    @Override
    public Path getConfigFilePath() {
        return FabricLoader.getInstance().getConfigDir().resolve("op-no-cheat.properties");
    }
}