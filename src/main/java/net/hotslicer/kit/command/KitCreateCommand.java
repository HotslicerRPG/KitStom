package net.hotslicer.kit.command;

import net.hotslicer.kit.KitExtension;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.Map;

/**
 * @author Jenya705
 */
public class KitCreateCommand extends Command {

    public KitCreateCommand(KitExtension extension) {
        this(extension, "create");
    }

    public KitCreateCommand(KitExtension extension, String name, String... aliases) {
        super(name, aliases);

        var kitNameArgument = ArgumentType.String("kit-name");

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only for players!");
                return;
            }
            if (player.getPermissionLevel() < 3 &&
                    !player.hasPermission("kit.create")) {
                player.sendMessage(Component
                        .text("You don't have permissions to create kit")
                        .color(NamedTextColor.RED)
                );
                return;
            }
            String kitName = context.get(kitNameArgument);
            boolean exists = extension.getKitContainer().getKit(kitName) != null;
            if (exists &&
                    player.getPermissionLevel() < 3 &&
                    !player.hasPermission("kit.replace")) {
                player.sendMessage(Component
                        .text("You don't have permissions to create kit")
                        .color(NamedTextColor.RED)
                );
                return;
            }
            extension.getKitContainer().addKit(
                    extension.getKitManager().generateKitData(kitName, player.getInventory())
            );
            player.sendMessage(Component
                    .text("Kit was successfully %s".formatted(exists ? "replaced" : "created"))
                    .color(NamedTextColor.GREEN)
            );
        }, kitNameArgument);
    }
}
