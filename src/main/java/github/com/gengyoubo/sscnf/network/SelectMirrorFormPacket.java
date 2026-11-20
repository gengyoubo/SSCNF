package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import github.com.gengyoubo.sscnf.items.RippleMirror;

/** Survival mirror request, deliberately separate from the admin SetFormPacket. */
public record SelectMirrorFormPacket(ResourceLocation formId) implements CustomPacketPayload {
    public static final Type<SelectMirrorFormPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "select_mirror_form"));
    @Override public Type<SelectMirrorFormPacket> type() { return TYPE; }

    public static void encode(SelectMirrorFormPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceLocation(packet.formId);
    }

    public static SelectMirrorFormPacket decode(RegistryFriendlyByteBuf buffer) {
        return new SelectMirrorFormPacket(buffer.readResourceLocation());
    }

    public static void handle(SelectMirrorFormPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer sender) {
                RippleMirror.selectForm(sender, packet.formId);
            }
        });
    }
}
