package github.com.gengyoubo.sscnf.network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import github.com.gengyoubo.sscnf.client.ClientPacketHandlers;
import github.com.gengyoubo.sscnf.power.FormActivePowerService;

/**
 * Owner-only charge state. {@code chargeKey} is the power's {@code charge_power_id} and
 * {@code tier} the completed charge tier (Fabric's {@code ChargePower.renderTier}), which the
 * web resource bar shows.
 */
public record ChargeStatePacket(ResourceLocation id, boolean charging, String chargeKey, int tier) implements CustomPacketPayload {
    public static final Type<ChargeStatePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "charge_state"));
    @Override public Type<ChargeStatePacket> type() { return TYPE; }

    public static void encode(ChargeStatePacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceLocation(packet.id);
        buffer.writeBoolean(packet.charging);
        buffer.writeUtf(packet.chargeKey);
        buffer.writeVarInt(packet.tier);
    }

    public static ChargeStatePacket decode(RegistryFriendlyByteBuf buffer) {
        return new ChargeStatePacket(buffer.readResourceLocation(), buffer.readBoolean(),
                buffer.readUtf(256), buffer.readVarInt());
    }

    public static void handle(ChargeStatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            FormActivePowerService.acceptClientCharge(context.player(), packet.id, packet.charging);
            ClientPacketHandlers.setChargeTier(packet.chargeKey, packet.charging ? packet.tier : 0);
        });
    }
}
