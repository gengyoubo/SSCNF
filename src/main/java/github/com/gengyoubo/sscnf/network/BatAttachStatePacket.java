package github.com.gengyoubo.sscnf.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.ClientPacketHandlers;

import java.util.UUID;
import java.util.function.Supplier;

/** Authoritative wall/ceiling attachment state for the owner and tracking clients. */
public record BatAttachStatePacket(UUID playerId, BlockPos pos, Direction side) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BatAttachStatePacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "bat_attach_state_packet"));
    @Override public CustomPacketPayload.Type<BatAttachStatePacket> type() { return TYPE; }

    public static void encode(BatAttachStatePacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeUUID(packet.playerId());
        buffer.writeBoolean(packet.pos() != null && packet.side() != null);
        if (packet.pos() != null && packet.side() != null) {
            buffer.writeBlockPos(packet.pos());
            buffer.writeEnum(packet.side());
        }
    }

    public static BatAttachStatePacket decode(RegistryFriendlyByteBuf buffer) {
        UUID playerId = buffer.readUUID();
        if (!buffer.readBoolean()) return new BatAttachStatePacket(playerId, null, null);
        return new BatAttachStatePacket(playerId, buffer.readBlockPos(), buffer.readEnum(Direction.class));
    }

    public static void handle(BatAttachStatePacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> ClientPacketHandlers.setBatAttachState(packet.playerId(), packet.pos(), packet.side()));
        
    }
}
