package net.iceice666;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Set;


public class CommandRules {

    static Mod.ConfigRuleSet ruleSets = Mod.ruleSets;

    /**
     * Check if a command is allowed to be executed by a player.
     * <p>
     * This function has the following checking process:
     * <p>
     * If the source is not a player, the command is allowed.
     * <p>
     * If the player is admin, the command is allowed.
     * <p>
     * If the command is in the player's whitelist, the command is allowed.
     * <p>
     * If the command is in the common whitelist, the command is allowed.
     * <p>
     * If the command is in the common blacklist, the command is not allowed.
     * <p>
     * If the command is in the player's blacklist, the command is not allowed.
     * <p>
     * Otherwise, the command is allowed.
     * <p>
     *
     * @param source  The command source.
     * @param command The command.
     * @return True if the command is allowed to be executed.
     */
    public static boolean isCommandAbleToExecute(ServerCommandSource source, String command) {
        // Source is not a player, so the command is allowed.
        if (!(source.getEntity() instanceof ServerPlayerEntity)) {
            return true;
        }

        var entity = source.getEntity();
        String playerUuid = entity.getUuidAsString();
        String playerName = entity.getEntityName();

        if (isPlayerAdmin(playerName) || isPlayerAdmin(playerUuid)) {
            return true;
        }

        // Command is in the player's whitelist.
        if (isInWhitelist(playerName, command) || isInWhitelist(playerUuid, command)) {
            return true;
        }

        // Command is in the common whitelist.
        if ((ruleSets.common.whitelist != null) && ruleSets.common.whitelist.contains(command)) {
            return true;
        }

        // Command is in the common blacklist.
        if ((ruleSets.common.blacklist != null) && ruleSets.common.blacklist.contains(command)) {
            return false;
        }

        // Command is in the player's blacklist.
        if (isInBlacklist(playerName, command) || isInBlacklist(playerUuid, command)) {
            return false;
        }

        return true;


    }

    private static boolean isInWhitelist(String player, String command) {
        if (!ruleSets.player.containsKey(player)) {
            return false;
        }

        Set<String> whitelist = ruleSets.player.get(player).whitelist;

        if (whitelist == null || whitelist.isEmpty()) {
            return false;
        }
        if (whitelist.contains("@ALL")) {
            return true;
        }

        return whitelist.contains(command);
    }

    private static boolean isInBlacklist(String player, String command) {
        if (!ruleSets.player.containsKey(player)) {
            return false;
        }

        Set<String> blacklist = ruleSets.player.get(player).blacklist;

        if (blacklist == null || blacklist.isEmpty()) {
            return false;
        }

        if (blacklist.contains("@ALL")) {
            return true;
        }


        return blacklist.contains(command);
    }


    private static boolean isPlayerAdmin(String player) {
        if (!ruleSets.player.containsKey(player)) {
            return false;
        }

        return ruleSets.player.get(player).admin;
    }
}
