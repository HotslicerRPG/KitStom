package net.hotslicer.kit;

import lombok.Getter;
import lombok.SneakyThrows;
import net.hotslicer.kit.command.KitCommand;
import net.hotslicer.kit.data.KitContainer;
import net.hotslicer.kit.data.KitLoader;
import net.hotslicer.kit.data.KitManager;
import net.hotslicer.kit.instance.KitInstanceManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;

/**
 * @author Jenya705
 */
@Getter
public class KitExtension extends Extension {

    private final KitLoader kitLoader = new KitLoader(this);
    private final KitContainer kitContainer = new KitContainer(this);
    private final KitManager kitManager = new KitManager(this);
    private final KitInstanceManager kitInstanceManager = new KitInstanceManager(this);

    private KitConfiguration config;

    @Override
    @SneakyThrows
    public void initialize() {
        getDataDirectory().toFile().mkdirs();
        config = KitConfiguration.load(this);
        kitInstanceManager.load();
        kitContainer.load();
        MinecraftServer.getCommandManager().register(new KitCommand(this));
        getLogger().info("Resources loaded");
    }

    @Override
    @SneakyThrows
    public void terminate() {
        kitContainer.save();
        getLogger().info("Resources saved");
    }
}
