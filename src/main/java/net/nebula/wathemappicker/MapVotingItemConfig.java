package net.nebula.wathemappicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class MapVotingItemConfig {

    private static final String FILE_NAME = "wathemappicker-server.json";

    public static final Map<String, Item> ITEMS = new HashMap<>();

    // Defaults used ONLY when creating the file
    private static final Map<String, String> DEFAULTS = Map.of(
            "townofharpy", "minecraft:diamond",
            "borealisexpress", "minecraft:raw_gold_block"
    );

    public static void load(MinecraftServer server) {
        ITEMS.clear();

        Path path = server.getSavePath(WorldSavePath.ROOT).resolve(FILE_NAME);

        try {
            // Create parent directories if missing
            Files.createDirectories(path.getParent());

            JsonObject json;

            // Read existing file if it exists
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    json = JsonParser.parseReader(reader).getAsJsonObject();
                    if (json == null) json = new JsonObject();
                }
            } else {
                json = new JsonObject();
            }

            final JsonObject finalJson = json;

            DEFAULTS.forEach((key, value) -> {
                if (!finalJson.has(key)) {
                    finalJson.addProperty(key, value);
                }
            });


            // Write back the merged JSON
            try (Writer writer = Files.newBufferedWriter(path)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(json, writer);
            }

            // Load items into memory
            for (var entry : json.entrySet()) {
                Identifier id = Identifier.tryParse(entry.getValue().getAsString());
                if (id != null && Registries.ITEM.containsId(id)) {
                    ITEMS.put(entry.getKey(), Registries.ITEM.get(id));
                    System.out.println(entry.getKey() + " : " + id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Item get(String key) {
        return ITEMS.get(key);
    }

    public static String mapToString(Map<String, Item> map) {
        if (map == null || map.isEmpty()) return "";

        return map.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Item item = entry.getValue();
                    Identifier id = Registries.ITEM.getId(item); // get the namespace:path
                    return key + ":" + id.getNamespace() + ":" + id.getPath();
                })
                .collect(Collectors.joining(","));
    }
}
