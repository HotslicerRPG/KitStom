package net.hotslicer.kit.command;

import net.hotslicer.kit.KitExtension;
import net.hotslicer.kit.data.KitData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;

/**
 * @author Jenya705
 */
public class KitGiveCommand extends Command {

    public KitGiveCommand(KitExtension extension) {
        this(extension, "give");
    }

    public KitGiveCommand(KitExtension extension, String name, String... aliases) {
        super(name, aliases);

        var kitNameArgument = ArgumentType.String("kit-name");

        kitNameArgument.setSuggestionCallback((sender, context, suggestion) ->
                extension.getKitContainer().getKits().forEach((kitName, kit) ->
                        suggestion.addEntry(new SuggestionEntry(kitName))
                )
        );

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only for players!");
                return;
            }
            String kitName = context.get(kitNameArgument);
            KitData kitData = extension.getKitContainer().getKit(kitName);
            if (kitData == null) {
                player.sendMessage(Component
                        .text("Kit %s is not exist".formatted(kitName))
                        .color(NamedTextColor.RED)
                );
                return;
            }
            if (player.getPermissionLevel() < 3 &&
                    !player.hasPermission("kit.give.*") &&
                    !player.hasPermission("kit.give.%s".formatted(kitName))) {
                player.sendMessage(Component
                        .text("You don't have permission to give this kit")
                        .color(NamedTextColor.RED)
                );
                return;
            }
            extension.getKitManager().giveKit(kitData, player);
        }, kitNameArgument);
    }
}
