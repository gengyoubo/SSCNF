package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.power.PowerAnimationService;
import java.util.function.Supplier;

/** Read-only recovery request when a client player entity is first observed. */
public record RequestPowerAnimationPacket(int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RequestPowerAnimationPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "request_power_animation_packet"));
    @Override public CustomPacketPayload.Type<RequestPowerAnimationPacket> type() { return TYPE; }

    public static void encode(RequestPowerAnimationPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.entityId);
    }

    public static RequestPowerAnimationPacket decode(RegistryFriendlyByteBuf buffer) {
        return new RequestPowerAnimationPacket(buffer.readVarInt());
    }

    public static void handle(RequestPowerAnimationPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> {
            ServerPlayer receiver = (context.player() instanceof net.minecraft.server.level.ServerPlayer sender ? sender : null);
            if (receiver != null && receiver.serverLevel().getEntity(packet.entityId) instanceof ServerPlayer target
                    && (receiver == target || receiver.distanceToSqr(target) <= 256 * 256)) {
                PowerAnimationService.synchronizeTo(target, receiver);
            }
        });
        
    }
}
