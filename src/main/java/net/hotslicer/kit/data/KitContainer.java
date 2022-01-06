package net.hotslicer.kit.data;

import lombok.Getter;
import lombok.SneakyThrows;
import net.hotslicer.kit.KitExtension;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jenya705
 */
public class KitContainer {

    @Getter
    private final Map<String, KitData> kits = new HashMap<>();
    private final KitExtension extension;

    public KitContainer(KitExtension extension) {
        this.extension = extension;
    }

    public void load() throws IOException, NBTException {
        File dataDirectory = new File(extension.getDataDirectory().toFile(), "kits");
        dataDirectory.mkdir();
        File[] files = dataDirectory.listFiles();
        if (files == null) return;
        for (File file: files) {
            String fileName = file.getName();
            if (!fileName.endsWith(".json")) continue;
            fileName = fileName.substring(0, fileName.length() - 5);
            KitData kitData = extension.getKitLoader().load(fileName);
            addKit(kitData);
        }
    }

    public void save() throws IOException {
        for (Map.Entry<String, KitData> entry: kits.entrySet()) {
            KitData kitData = entry.getValue();
            extension.getKitLoader().save(kitData);
        }
    }

    public KitData getKit(String name) {
        return kits.get(name.toLowerCase(Locale.ROOT));
    }

    @SneakyThrows
    public void addKit(KitData kit) {
        if (extension.getConfig().isSaveKitOnCreation()) {
            extension.getKitLoader().save(kit);
        }
        kits.put(kit.getName().toLowerCase(Locale.ROOT), kit);
    }

}
