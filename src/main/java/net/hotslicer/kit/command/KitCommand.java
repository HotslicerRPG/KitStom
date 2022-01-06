package net.hotslicer.kit.command;

import net.hotslicer.kit.KitExtension;
import net.minestom.server.command.builder.Command;

/**
 * @author Jenya705
 */
public class KitCommand extends Command {

    public KitCommand(KitExtension extension) {
        this(extension, "kit");
    }

    public KitCommand(KitExtension extension, String name, String... aliases) {
        super(name, aliases);

        addSubcommand(new KitCreateCommand(extension));
        addSubcommand(new KitGiveCommand(extension));

    }

}
