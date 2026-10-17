package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;
import java.util.function.Supplier;

/** Server to client: open the form select menu for acting on a target player. */
public record OpenSelectFormPacket(String targetName, UUID targetUUID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenSelectFormPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "open_select_form_packet"));
    @Override public CustomPacketPayload.Type<OpenSelectFormPacket> type() { return TYPE; }

    public static void encode(OpenSelectFormPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(packet.targetName, 256);
        buffer.writeUUID(packet.targetUUID);
    }

    public static OpenSelectFormPacket decode(RegistryFriendlyByteBuf buffer) {
        return new OpenSelectFormPacket(buffer.readUtf(256), buffer.readUUID());
    }

    public static void handle(OpenSelectFormPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> github.com.gengyoubo.sscnf.client.ClientPacketHandlers
                        .openSelectForm(packet.targetName, packet.targetUUID));
        
    }
}