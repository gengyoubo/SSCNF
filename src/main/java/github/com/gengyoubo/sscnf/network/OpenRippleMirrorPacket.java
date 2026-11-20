package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

/** Server to client: open the Ripple Mirror form picker with the forms the player may choose. */
public record OpenRippleMirrorPacket(List<ResourceLocation> forms) implements CustomPacketPayload {
    public static final Type<OpenRippleMirrorPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "open_ripple_mirror"));
    @Override public Type<OpenRippleMirrorPacket> type() { return TYPE; }

    public static void encode(OpenRippleMirrorPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeCollection(packet.forms, (buf, id) -> buf.writeResourceLocation(id));
    }

    public static OpenRippleMirrorPacket decode(RegistryFriendlyByteBuf buffer) {
        return new OpenRippleMirrorPacket(buffer.readList(buf -> buf.readResourceLocation()));
    }

    public static void handle(OpenRippleMirrorPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> github.com.gengyoubo.sscnf.client.ClientPacketHandlers
                .openRippleMirror(packet.forms));
    }
}
