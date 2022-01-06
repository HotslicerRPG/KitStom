package net.hotslicer.kit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Cleanup;
import lombok.Data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * @author Jenya705
 */
@Data
public class KitConfiguration {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static KitConfiguration load(KitExtension extension) throws IOException {
        File file = new File(extension.getDataDirectory().toFile(), "config.json");
        if (!file.exists()) {
            KitConfiguration configuration = new KitConfiguration();
            Files.writeString(file.toPath(), gson.toJson(configuration), StandardOpenOption.CREATE);
            return configuration;
        }
        @Cleanup Reader inputStream = new FileReader(file);
        return gson.fromJson(inputStream, KitConfiguration.class);
    }

    private boolean replaceItems = false;

    private boolean saveKitOnCreation = true;

}
