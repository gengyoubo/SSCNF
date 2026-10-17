package github.com.gengyoubo.sscnf.network;

import java.util.function.Supplier;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.ManaClientState;

/** Owner-only sync for the active mana pool used by the HUD and local feedback. */
public record ManaSyncPacket(String manaType, float mana, float maximum) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ManaSyncPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "mana_sync_packet"));
    @Override public CustomPacketPayload.Type<ManaSyncPacket> type() { return TYPE; }

    public static void encode(ManaSyncPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(packet.manaType);
        buffer.writeFloat(packet.mana);
        buffer.writeFloat(packet.maximum);
    }

    public static ManaSyncPacket decode(RegistryFriendlyByteBuf buffer) {
        return new ManaSyncPacket(buffer.readUtf(256), buffer.readFloat(), buffer.readFloat());
    }

    public static void handle(ManaSyncPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> ManaClientState.set(packet.manaType, packet.mana, packet.maximum));
        
    }
}
