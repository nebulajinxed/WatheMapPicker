package net.nebula.wathemappicker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.nebula.wathemappicker.client.gui.screen.MapVotingScreen;
import net.nebula.wathemappicker.packet.DimensionItemS2CPacket;
import net.nebula.wathemappicker.packet.DimensionsS2CPacket;

import java.util.*;

public class WathemappickerClient implements ClientModInitializer {
    public static List<String> dimensions = new ArrayList<>();
    public static Map<String, Item> ITEMS = new HashMap<>();

    @Override
    public void onInitializeClient() {
        registerPackets();
    }

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DimensionsS2CPacket.ID, ((payload, context) -> {
            dimensions = new ArrayList<>(Arrays.asList(payload.dimensions().split(",")));
        }));
        ClientPlayNetworking.registerGlobalReceiver(DimensionItemS2CPacket.ID, (payload, context) -> {
            Map<String, Item> itemMap = new HashMap<>();

            String data = payload.dimensionItems(); // the comma-separated string

            if (data != null && !data.isEmpty()) {
                String[] entries = data.split(",");

                for (String entry : entries) {
                    entry = entry.trim();
                    if (entry.isEmpty()) continue;

                    // Split key and item
                    String[] parts = entry.split(":", 3); // limit 3
                    if (parts.length != 3) {
                        System.err.println("[MapVoting] Invalid item entry: " + entry);
                        continue;
                    }

                    String key = parts[0];
                    String namespace = parts[1];
                    String path = parts[2];

                    Identifier id = Identifier.of(namespace, path);
                    if (Registries.ITEM.containsId(id)) {
                        itemMap.put(key, Registries.ITEM.get(id));
                        System.out.println("put: " + key + " with " + id);
                    } else {
                        System.err.println("[MapVoting] Unknown item ID: " + id);
                    }
                }
            }

            // Store the map wherever you need it, e.g., a static field
            ITEMS = itemMap;
        });

        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> {
            dimensions.clear();
            MapVotingScreen.widgetDimensions.clear();
        });
    }

}
