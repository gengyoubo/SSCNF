package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.codex.CursedMoonData;

import java.util.function.Supplier;

/** Server-authoritative Cursed Moon day flag used by the client Codex and warning message. */
public record CursedMoonSyncPacket(boolean cursedMoonDay) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CursedMoonSyncPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "cursed_moon_sync_packet"));
    @Override public CustomPacketPayload.Type<CursedMoonSyncPacket> type() { return TYPE; }

    public static void encode(CursedMoonSyncPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.cursedMoonDay);
    }

    public static CursedMoonSyncPacket decode(RegistryFriendlyByteBuf buffer) {
        return new CursedMoonSyncPacket(buffer.readBoolean());
    }

    public static void handle(CursedMoonSyncPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> CursedMoonData.setServerCursedMoonDay(packet.cursedMoonDay));
        
    }
}
