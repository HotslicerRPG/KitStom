package net.hotslicer.kit.instance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.hotslicer.kit.BlockPosition;
import net.hotslicer.kit.KitExtension;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import java.nio.file.Path;
import java.util.Arrays;

@RequiredArgsConstructor
public class KitInstanceManager {

    private final KitExtension extension;

    @Getter
    private Instance instance;

    private volatile int current = 0;

    public void load() {
        if (extension.getConfig().getWorld() == null) {
            return;
        }
        String world = extension.getConfig().getWorld().getPath();
        if (world != null && !world.isEmpty()) {
            String[] splitPath = world.split("/");
            if (splitPath.length == 0) {
                return;
            }
            instance = InstanceUtils.loadAnvil(Path.of(splitPath[0], Arrays.copyOfRange(splitPath, 1, splitPath.length)));
        }
    }

    public boolean hasInstance() {
        return instance != null;
    }

    public void spawnPlayer(Player player) {
        if (!hasInstance()) {
            return;
        }
        player.setInstance(getInstance());
        player.teleport(spawnPositions()[nextCurrent()].toPos());
    }

    private BlockPosition[] spawnPositions() {
        return extension.getConfig().getWorld().getSpawnPositions();
    }

    private synchronized int nextCurrent() {
        return current = (current + 1) % spawnPositions().length;
    }

}
