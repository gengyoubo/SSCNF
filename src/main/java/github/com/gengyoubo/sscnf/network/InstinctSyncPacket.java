package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Supplier;

/** Owner-only server snapshot for the instinct HUD. */
public record InstinctSyncPacket(float value, float rate, boolean visible, boolean locked) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<InstinctSyncPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "instinct_sync_packet"));
    @Override public CustomPacketPayload.Type<InstinctSyncPacket> type() { return TYPE; }

    public static void encode(InstinctSyncPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeFloat(packet.value);
        buffer.writeFloat(packet.rate);
        buffer.writeBoolean(packet.visible);
        buffer.writeBoolean(packet.locked);
    }

    public static InstinctSyncPacket decode(RegistryFriendlyByteBuf buffer) {
        return new InstinctSyncPacket(buffer.readFloat(), buffer.readFloat(),
                buffer.readBoolean(), buffer.readBoolean());
    }

    public static void handle(InstinctSyncPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> github.com.gengyoubo.sscnf.client.ClientPacketHandlers.setInstinct(
                        packet.value, packet.rate, packet.visible, packet.locked));
        
    }
}
