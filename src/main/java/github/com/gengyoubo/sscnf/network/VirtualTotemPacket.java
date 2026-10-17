package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Supplier;

/** Server to client: play the totem activation animation with a specific (possibly virtual) stack. */
public record VirtualTotemPacket(ItemStack stack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VirtualTotemPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "virtual_totem_packet"));
    @Override public CustomPacketPayload.Type<VirtualTotemPacket> type() { return TYPE; }

    public static void encode(VirtualTotemPacket packet, RegistryFriendlyByteBuf buffer) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, packet.stack);
    }

    public static VirtualTotemPacket decode(RegistryFriendlyByteBuf buffer) {
        return new VirtualTotemPacket(ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer));
    }

    public static void handle(VirtualTotemPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> github.com.gengyoubo.sscnf.client.ClientPacketHandlers.displayTotem(packet.stack));
        
    }
}