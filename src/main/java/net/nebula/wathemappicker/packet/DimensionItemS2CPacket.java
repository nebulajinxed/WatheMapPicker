package net.nebula.wathemappicker.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.nebula.wathemappicker.Wathemappicker;

public record DimensionItemS2CPacket(String dimensionItems) implements CustomPayload {
    public static final Id<DimensionItemS2CPacket> ID = new Id<>(Wathemappicker.id("dimensionitems_s2c"));
    public static final PacketCodec<PacketByteBuf, DimensionItemS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.STRING, DimensionItemS2CPacket::dimensionItems, DimensionItemS2CPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
