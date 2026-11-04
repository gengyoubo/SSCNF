package github.com.gengyoubo.sscnf.network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import github.com.gengyoubo.sscnf.power.FormActivePowerService;
public record PowerResourcePacket(ResourceLocation id, double value) implements CustomPacketPayload {
    public static final Type<PowerResourcePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "power_resource"));
    @Override public Type<PowerResourcePacket> type() { return TYPE; }
    public static void encode(PowerResourcePacket packet, RegistryFriendlyByteBuf buffer) { buffer.writeResourceLocation(packet.id); buffer.writeDouble(packet.value); }
    public static PowerResourcePacket decode(RegistryFriendlyByteBuf buffer) { return new PowerResourcePacket(buffer.readResourceLocation(), buffer.readDouble()); }
    public static void handle(PowerResourcePacket packet, IPayloadContext context) { context.enqueueWork(() -> FormActivePowerService.acceptClientResource(context.player(), packet.id, packet.value)); }
}
