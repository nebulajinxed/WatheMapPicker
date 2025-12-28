package net.nebula.wathemappicker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.nebula.wathemappicker.client.gui.ClientItemConfig;
import net.nebula.wathemappicker.client.gui.screen.MapVotingScreen;
import net.nebula.wathemappicker.packet.DimensionsS2CPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WathemappickerClient implements ClientModInitializer {
    public static List<String> dimensions = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        registerPackets();
        ClientItemConfig.load();
    }

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DimensionsS2CPacket.ID, ((payload, context) -> {
            dimensions = new ArrayList<>(Arrays.asList(payload.dimensions().split(",")));
        }));
        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> {
            dimensions.clear();
            MapVotingScreen.widgetDimensions.clear();
        });
    }

}
