package net.hotslicer.kit.instance;

import lombok.experimental.UtilityClass;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;

import java.nio.file.Path;

@UtilityClass
public class InstanceUtils {

    public Instance loadAnvil(Path path) {
        AnvilLoader loader = new AnvilLoader(path);
        Instance result = MinecraftServer.getInstanceManager().createInstanceContainer();
        loader.loadInstance(result);
        return result;
    }

}
