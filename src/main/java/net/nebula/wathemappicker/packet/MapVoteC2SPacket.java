package net.nebula.wathemappicker.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nebula.wathemappicker.MapVotePlayerComponent;
import net.nebula.wathemappicker.Wathemappicker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record MapVoteC2SPacket(String mapIdentifier) implements CustomPayload {
    public static final Id<MapVoteC2SPacket> ID = new Id<>(Wathemappicker.id("map_vote_c2s"));
    public static final PacketCodec<PacketByteBuf, MapVoteC2SPacket> CODEC = PacketCodec.tuple(PacketCodecs.STRING, MapVoteC2SPacket::mapIdentifier, MapVoteC2SPacket::new);

    public static final Map<String, Integer> VOTES = new HashMap<>();

    // <player UUID, mapIdentifier>
    private static final Map<UUID, String> PLAYER_VOTES = new HashMap<>();


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }


    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<MapVoteC2SPacket> {

        @Override
        public void receive(MapVoteC2SPacket packet, ServerPlayNetworking.Context context) {
            context.server().execute(() -> {
                String dimensionString = Wathemappicker.getDimensionString(context.server());
                String mapIdentifier = packet.mapIdentifier();

                boolean isValid = Arrays.stream(dimensionString.split(","))
                        .map(String::trim)
                        .anyMatch(s -> s.equals(mapIdentifier));

                if (!isValid) {
                    return;
                }

                var player = context.player();
                var playerId = player.getUuid();

                String oldVote = PLAYER_VOTES.get(playerId);

                if (mapIdentifier.equals(oldVote)) {
                    return;
                }

                if (oldVote != null) {
                    PLAYER_VOTES.remove(playerId);
                    VOTES.merge(oldVote, -1, Integer::sum);
                }

                VOTES.merge(mapIdentifier, 1, Integer::sum);
                PLAYER_VOTES.put(playerId, mapIdentifier);

                MapVotePlayerComponent.KEY.get(player).setVoteTarget(mapIdentifier);
            });
        }
    }

    public static void removeVote(ServerPlayerEntity playerEntity) {
        String vote = PLAYER_VOTES.get(playerEntity.getUuid());

        if (vote != null) {
            PLAYER_VOTES.remove(playerEntity.getUuid());
            VOTES.merge(vote, -1, Integer::sum);
        }

        MapVotePlayerComponent.KEY.get(playerEntity).reset();
    }
    public static String getWinningMap() {
        if (VOTES.isEmpty()) {
            return null; // no votes yet
        }

        String winningMap = null;
        int highestVotes = -1;

        for (Map.Entry<String, Integer> entry : VOTES.entrySet()) {
            if (entry.getValue() > highestVotes) {
                highestVotes = entry.getValue();
                winningMap = entry.getKey();
            }
        }
        System.out.println("winning map: " + winningMap);
        return winningMap;

    }
}
