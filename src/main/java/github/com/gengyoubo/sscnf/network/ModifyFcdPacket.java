package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.color.FormColorClientHandler;

import java.util.function.Supplier;

/**
 * Server to client color-slot command. Contract (both ends owned here):
 * commandType one of save/load/delete/config/list; formId is the target form
 * (placeholder for config); arg1 is scope ("form"/"global"/"form_default") or
 * value; arg2 is the slot name; arg3/arg4 reserved.
 */
public record ModifyFcdPacket(String commandType, String formId, String arg1, String arg2,
                              String arg3, String arg4) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ModifyFcdPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "modify_fcd_packet"));
    @Override public CustomPacketPayload.Type<ModifyFcdPacket> type() { return TYPE; }

    public static void encode(ModifyFcdPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(packet.commandType, 32);
        buffer.writeUtf(packet.formId, 256);
        buffer.writeUtf(packet.arg1, 256);
        buffer.writeUtf(packet.arg2, 256);
        buffer.writeUtf(packet.arg3, 256);
        buffer.writeUtf(packet.arg4, 256);
    }

    public static ModifyFcdPacket decode(RegistryFriendlyByteBuf buffer) {
        return new ModifyFcdPacket(buffer.readUtf(32), buffer.readUtf(256), buffer.readUtf(256),
                buffer.readUtf(256), buffer.readUtf(256), buffer.readUtf(256));
    }

    public static void handle(ModifyFcdPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> FormColorClientHandler.applyModifyFcd(packet));
        
    }
}
