package net.iceice666;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;


public class Mod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("op-no-cheat");

    static File configFile = FabricLoader
            .getInstance()
            .getConfigDir()
            .resolve("op-no-cheat.toml")
            .toFile();
    static ConfigRuleSet ruleSets;
    Toml defaultRuleSets = new Toml()
            .read(
                    """

                            
[common]
    whitelist = []
    blacklist = [
    "gamemode",
    "gamerule",
    "give",
    "kill",
    "op",
    "pardon",
    "deop"
    "pardon-ip",
    "ban",
    "ban-ip",
    "execute",
    "enchant",
    "effect",
    "clear",
    "difficulty",
    "kick",
    "damage",
    "ride",
    "advancement",
    "attribute",
    "bossbar",
    "clone",
    "data",
    "datapack",
    "xp",
    "experience",
    "fill",
    "fillbiome",
    "forceload",
    "function",
    "item",
    "loot",
    "place",
    "reload",
    "schedule",
    "scoreboard",
    "setblock",
    "setworldspawn",
    "spawnpoint",
    "summon",
    "spreadplayers",
    "tag",
    "worldborder",
    "debug",
    "setidletimeout",
    "whitelist",
    "jfr",
    "perf",
    "stop"
    ]
                        

[player]
 """);
    static TomlWriter tomlWriter = new TomlWriter();

    static class ConfigRuleSet {
        static class RuleSet {
            public Set<String> whitelist;
            public Set<String> blacklist;


        }

        static class PlayerRuleSet extends RuleSet {
            public boolean admin;
        }

        public RuleSet common;
        public HashMap<String, PlayerRuleSet> player;
    }

    public static void saveConfig() throws IOException {
        tomlWriter.write(ruleSets, configFile);
    }

    @Override
    public void onInitialize() {
        if (!configFile.exists() || !configFile.isFile() || configFile.length() == 0) {
            try {
                configFile.createNewFile();
                tomlWriter.write(defaultRuleSets.to(ConfigRuleSet.class), configFile);
            } catch (IOException e) {
                LOGGER.error(Arrays.toString(e.getStackTrace()));
            }
        }

        ruleSets = new Toml().read(defaultRuleSets)
                .read(configFile)
                .to(ConfigRuleSet.class);


    }

}
