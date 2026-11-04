package github.com.gengyoubo.sscnf.network;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import github.com.gengyoubo.sscnf.power.FrostDiveService;
public record FrostDiveStatePacket(boolean diving) implements CustomPacketPayload {
    public static final Type<FrostDiveStatePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "frost_dive_state"));
    @Override public Type<FrostDiveStatePacket> type() { return TYPE; }
    public static void encode(FrostDiveStatePacket packet, RegistryFriendlyByteBuf buffer) { buffer.writeBoolean(packet.diving); }
    public static FrostDiveStatePacket decode(RegistryFriendlyByteBuf buffer) { return new FrostDiveStatePacket(buffer.readBoolean()); }
    public static void handle(FrostDiveStatePacket packet, IPayloadContext context) { context.enqueueWork(() -> FrostDiveService.setClientDiving(context.player(), packet.diving)); }
}
