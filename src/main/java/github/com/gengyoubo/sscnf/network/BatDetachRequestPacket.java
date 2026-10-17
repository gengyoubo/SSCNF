package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.power.BatAttachService;

import java.util.function.Supplier;

/** A jump request; the server checks its own attachment state before detaching. */
public record BatDetachRequestPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BatDetachRequestPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "bat_detach_request_packet"));
    @Override public CustomPacketPayload.Type<BatDetachRequestPacket> type() { return TYPE; }

    public static void encode(BatDetachRequestPacket packet, RegistryFriendlyByteBuf buffer) {
    }

    public static BatDetachRequestPacket decode(RegistryFriendlyByteBuf buffer) {
        return new BatDetachRequestPacket();
    }

    public static void handle(BatDetachRequestPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> {
            if ((context.player() instanceof net.minecraft.server.level.ServerPlayer sender ? sender : null) != null) BatAttachService.detachForJump((context.player() instanceof net.minecraft.server.level.ServerPlayer sender ? sender : null));
        });
        
    }
}
