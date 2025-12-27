package net.nebula.wathemappicker.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.nebula.wathemappicker.Wathemappicker;

public record DimensionsS2CPacket(String dimensions) implements CustomPayload {
    public static final Id<DimensionsS2CPacket> ID = new Id<>(Wathemappicker.id("dimensions_s2c"));
    public static final PacketCodec<PacketByteBuf, DimensionsS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.STRING, DimensionsS2CPacket::dimensions, DimensionsS2CPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
