package github.com.gengyoubo.sscnf.network;

import com.mojang.logging.LogUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.form.FormManager;
import github.com.gengyoubo.sscnf.form.FormRegistry;
import github.com.gengyoubo.sscnf.form.TransformManager;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Client to server form change request. Mirrors Fabric: packets can be forged, so
 * applying requires operator level 2 or creative mode.
 */
public record SetFormPacket(UUID targetUUID, ResourceLocation formId, boolean immediate) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetFormPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "set_form_packet"));
    @Override public CustomPacketPayload.Type<SetFormPacket> type() { return TYPE; }

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void encode(SetFormPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeUUID(packet.targetUUID);
        buffer.writeResourceLocation(packet.formId);
        buffer.writeBoolean(packet.immediate);
    }

    public static SetFormPacket decode(RegistryFriendlyByteBuf buffer) {
        return new SetFormPacket(buffer.readUUID(), buffer.readResourceLocation(), buffer.readBoolean());
    }

    public static void handle(SetFormPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> {
            ServerPlayer sender = (context.player() instanceof net.minecraft.server.level.ServerPlayer serverPlayer ? serverPlayer : null);
            if (sender == null) {
                return;
            }
            ServerPlayer target = sender.server.getPlayerList().getPlayer(packet.targetUUID);
            if (target == null) {
                LOGGER.warn("[SetForm] Player {} not found", packet.targetUUID);
                return;
            }
            if (FormRegistry.get(packet.formId) == null) {
                LOGGER.warn("[SetForm] Unknown form {}", packet.formId);
                return;
            }
            if (sender.hasPermissions(2) || sender.getAbilities().instabuild) {
                TransformManager.forceTransform(target, packet.formId, packet.immediate);
            } else {
                LOGGER.warn("[SetForm] Player {} lacks permission to set forms", sender.getGameProfile().getName());
            }
        });
        
    }
}
