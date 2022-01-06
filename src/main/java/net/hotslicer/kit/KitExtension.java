package net.hotslicer.kit;

import lombok.Getter;
import lombok.SneakyThrows;
import net.hotslicer.kit.command.KitCommand;
import net.hotslicer.kit.data.KitContainer;
import net.hotslicer.kit.data.KitLoader;
import net.hotslicer.kit.data.KitManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

/**
 * @author Jenya705
 */
@Getter
public class KitExtension extends Extension {

    private final KitLoader kitLoader = new KitLoader(this);
    private final KitContainer kitContainer = new KitContainer(this);
    private final KitManager kitManager = new KitManager(this);

    private KitConfiguration config;

    @Override
    @SneakyThrows
    public void initialize() {
        getDataDirectory().toFile().mkdirs();
        config = KitConfiguration.load(this);
        kitContainer.load();
        MinecraftServer.getCommandManager().register(new KitCommand(this));
        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, e -> e.getPlayer().getInventory().addItemStack(ItemStack.of(Material.STICK)));
        getLogger().info("Resources loaded");
    }

    @Override
    @SneakyThrows
    public void terminate() {
        kitContainer.save();
        getLogger().info("Resources saved");
    }
}
