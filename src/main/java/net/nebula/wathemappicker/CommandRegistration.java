package net.nebula.wathemappicker;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CommandRegistration {

    public static void register() {
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(
                    CommandManager.literal("wathe:setMap")
                            .then(CommandManager.argument("dimension", StringArgumentType.word())
                                    .suggests((ctx, builder) -> {
                                        ctx.getSource().getServer().getWorldRegistryKeys().stream()
                                                .map(RegistryKey::getValue)
                                                .filter(id -> !isVanillaDimension(id))
                                                .forEach(id -> builder.suggest(id.getPath()));
                                        return builder.buildFuture();
                                    })
                                    .executes(ctx -> {
                                        MapManager.setMap(ctx);
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                            .requires(source -> source.hasPermissionLevel(2))
            );

        }));
    }

    public static boolean isVanillaDimension(Identifier id) {
        return id.equals(World.OVERWORLD.getValue()) || id.equals(World.NETHER.getValue()) || id.equals(World.END.getValue());
    }
}
