package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.ClientPacketHandlers;

import java.util.function.Supplier;

/** Server-authoritative dynamic keep_sneaking condition for client pose and crawl animation. */
public record SyncForcedSneakingPacket(int entityId, boolean forced) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncForcedSneakingPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "sync_forced_sneaking_packet"));
    @Override public CustomPacketPayload.Type<SyncForcedSneakingPacket> type() { return TYPE; }

    public static void encode(SyncForcedSneakingPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId());
        buffer.writeBoolean(packet.forced());
    }

    public static SyncForcedSneakingPacket decode(RegistryFriendlyByteBuf buffer) {
        return new SyncForcedSneakingPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(SyncForcedSneakingPacket packet,
                              IPayloadContext context) {
        
        context.enqueueWork(() -> ClientPacketHandlers.applyForcedSneaking(packet.entityId(), packet.forced()));
        
    }
}
