package net.hotslicer.kit.data;

import com.google.gson.JsonObject;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import net.hotslicer.kit.KitConfiguration;
import net.hotslicer.kit.KitExtension;
import net.hotslicer.kit.nbt.NbtParser;
import net.minestom.server.item.ItemStack;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jenya705
 */
@RequiredArgsConstructor
public class KitLoader {

    private final KitExtension extension;

    public void save(KitData data) throws IOException {
        JsonObject obj = NbtParser.toJson(
                new NBTCompound().modify(mutableNBTCompound ->
                        data.getItems().forEach((index, item) ->
                                mutableNBTCompound.set(Integer.toString(index), item.toItemNBT())
                        )
                )
        );
        Files.writeString(kitFile(data.getName()).toPath(), KitConfiguration.gson.toJson(obj));
    }

    public KitData load(String name) throws IOException {
        @Cleanup Reader reader = new FileReader(kitFile(name));
        NBTCompound kitNbt = (NBTCompound) NbtParser.fromJson(
                KitConfiguration.gson.fromJson(reader, JsonObject.class)
        );
        KitData kitData = new KitData();
        kitData.setName(name);
        kitData.setItems(new HashMap<>());
        for (Map.Entry<String, NBT> entry : kitNbt.asMapView().entrySet()) {
            int key = Integer.parseInt(entry.getKey());
            NBTCompound item = (NBTCompound) entry.getValue();
            kitData.getItems().put(key, ItemStack.fromItemNBT(item));
        }
        return kitData;
    }

    private File kitFile(String name) {
        return new File(extension.getDataDirectory().toFile(), "kits/%s.json".formatted(name));
    }
}
