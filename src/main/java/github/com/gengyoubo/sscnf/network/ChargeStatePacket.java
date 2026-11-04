package github.com.gengyoubo.sscnf.network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import github.com.gengyoubo.sscnf.power.FormActivePowerService;
public record ChargeStatePacket(ResourceLocation id, boolean charging) implements CustomPacketPayload {
    public static final Type<ChargeStatePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "charge_state"));
    @Override public Type<ChargeStatePacket> type() { return TYPE; }
    public static void encode(ChargeStatePacket packet, RegistryFriendlyByteBuf buffer) { buffer.writeResourceLocation(packet.id); buffer.writeBoolean(packet.charging); }
    public static ChargeStatePacket decode(RegistryFriendlyByteBuf buffer) { return new ChargeStatePacket(buffer.readResourceLocation(), buffer.readBoolean()); }
    public static void handle(ChargeStatePacket packet, IPayloadContext context) { context.enqueueWork(() -> FormActivePowerService.acceptClientCharge(context.player(), packet.id, packet.charging)); }
}
