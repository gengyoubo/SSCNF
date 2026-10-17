package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.ClientPacketHandlers;

import java.util.function.Supplier;

/** Server to client: open the form color menu (always V2 on Forge). */
@SuppressWarnings("EmptyMethod")
public record OpenColorMenuPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenColorMenuPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "open_color_menu_packet"));
    @Override public CustomPacketPayload.Type<OpenColorMenuPacket> type() { return TYPE; }

    public static void encode(OpenColorMenuPacket packet, RegistryFriendlyByteBuf buffer) {
    }

    public static OpenColorMenuPacket decode(RegistryFriendlyByteBuf buffer) {
        return new OpenColorMenuPacket();
    }

    public static void handle(OpenColorMenuPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> ClientPacketHandlers.openColorMenu());
        
    }
}