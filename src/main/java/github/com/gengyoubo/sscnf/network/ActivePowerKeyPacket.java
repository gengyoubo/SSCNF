package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.power.FormActivePowerService;

import java.util.function.Supplier;

/** Client input only; every power is validated and executed by the server. */
public record ActivePowerKeyPacket(String key, boolean pressed) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ActivePowerKeyPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "active_power_key_packet"));
    @Override public CustomPacketPayload.Type<ActivePowerKeyPacket> type() { return TYPE; }

    public static void encode(ActivePowerKeyPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(packet.key, 96);
        buffer.writeBoolean(packet.pressed);
    }

    public static ActivePowerKeyPacket decode(RegistryFriendlyByteBuf buffer) {
        return new ActivePowerKeyPacket(buffer.readUtf(96), buffer.readBoolean());
    }

    public static void handle(ActivePowerKeyPacket packet, IPayloadContext context) {
           
        context.enqueueWork(() -> {
            if ((context.player() instanceof net.minecraft.server.level.ServerPlayer sender ? sender : null) != null) {
                FormActivePowerService.setKeyPressed((context.player() instanceof net.minecraft.server.level.ServerPlayer sender ? sender : null), packet.key, packet.pressed);
            }
        });
        
    }
}
