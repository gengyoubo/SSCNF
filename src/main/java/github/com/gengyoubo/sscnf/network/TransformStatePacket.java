package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.ClientPacketHandlers;

import java.util.function.Supplier;

/** Authoritative transform-state broadcast: which player is transforming from/to what form. */
public record TransformStatePacket(int entityId, boolean transforming, String startFormId, String endFormId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TransformStatePacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "transform_state_packet"));
    @Override public CustomPacketPayload.Type<TransformStatePacket> type() { return TYPE; }

    public static void encode(TransformStatePacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeBoolean(packet.transforming);
        buffer.writeUtf(packet.startFormId == null ? "" : packet.startFormId);
        buffer.writeUtf(packet.endFormId == null ? "" : packet.endFormId);
    }

    public static TransformStatePacket decode(RegistryFriendlyByteBuf buffer) {
        return new TransformStatePacket(
                buffer.readInt(),
                buffer.readBoolean(),
                buffer.readUtf(256),
                buffer.readUtf(256)
        );
    }

    public static void handle(TransformStatePacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> ClientPacketHandlers.setTransformState(
                        packet.entityId(), packet.transforming(), packet.startFormId(), packet.endFormId()));
        
    }
}
