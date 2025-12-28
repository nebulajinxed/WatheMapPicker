package net.nebula.wathemappicker.client.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ClientItemConfig {

    private static final Path PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("wathemappicker-client.json");

    private static final Map<String, Item> ITEMS = new HashMap<>();

    // Defaults used ONLY when creating the file
    private static final Map<String, String> DEFAULTS = Map.of(
            "townofharpy", "minecraft:diamond",
            "borealisexpress", "minecraft:raw_gold_block"
    );

    public static void load() {
        ITEMS.clear();

        try {
            // Create file if missing
            if (!Files.exists(PATH)) {
                Files.createDirectories(PATH.getParent());

                JsonObject defaults = new JsonObject();
                DEFAULTS.forEach(defaults::addProperty);

                try (Writer writer = Files.newBufferedWriter(PATH)) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(defaults, writer);
                }
            }

            // Read file
            try (Reader reader = Files.newBufferedReader(PATH)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                for (var entry : json.entrySet()) {
                    Identifier id = Identifier.tryParse(entry.getValue().getAsString());
                    if (id != null && Registries.ITEM.containsId(id)) {
                        ITEMS.put(entry.getKey(), Registries.ITEM.get(id));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Item get(String key) {
        return ITEMS.get(key);
    }
}
