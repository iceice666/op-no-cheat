package net.iceice666.mixin;

import com.mojang.brigadier.ParseResults;
import net.iceice666.CommandRules;
import net.iceice666.Mod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.Objects;

public class ModMixin {


    @Mixin(CommandManager.class)
    private static class commandExecuteMixin {
        @Shadow
        @Final
        private static Logger LOGGER;


        @Inject(at = @At("HEAD"), method = "execute(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)I", cancellable = true)
        private void executeChecker(ParseResults<ServerCommandSource> parseResults, String command,
                                    CallbackInfoReturnable<Integer> cir) {
            String rootCommand = command.split(" ", 2)[0];
            ServerCommandSource serverCommandSource = parseResults.getContext().getSource();


            if (!CommandRules.isCommandAbleToExecute(serverCommandSource, rootCommand)) {
                MutableText mutableText = Text.empty();
                mutableText.append(Text.of("You are not able to run this command: " + rootCommand + "\n"));
                mutableText.append(Text.of("If you need further information,\n" +
                        " please contact the server administrator."));

                serverCommandSource.sendError(mutableText);


                var playerName = serverCommandSource.getName();
                var playerUuid = Objects.requireNonNull(serverCommandSource.getPlayer()).getUuidAsString();

                LOGGER.info("Gotcha! Player " + playerName + " (" + playerUuid + ")" + " tried to run command:");
                LOGGER.info(command);


                cir.setReturnValue(0);
            }
        }
    }


    @Mixin(MinecraftServer.class)
    static class serverStopMixin {
        @Inject(at = @At("HEAD"), method = "stop")
        private void saveConfig(CallbackInfo info) throws IOException {
            Mod.saveConfig();
        }
    }
}
