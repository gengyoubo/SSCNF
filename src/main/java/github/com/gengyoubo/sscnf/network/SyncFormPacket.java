package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.FormSyncClientHandler;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Form state plus an explicit real-transform marker; login/respawn synchronisation never sets it. */
public record SyncFormPacket(int entityId, String formId, String previousFormId, String groupId, int tier,
                             boolean enabled, List<ResourceLocation> assignedPowerIds,
                             boolean playTransformAnimation) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncFormPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "sync_form_packet"));
    @Override public CustomPacketPayload.Type<SyncFormPacket> type() { return TYPE; }

    private static final int MAX_POWER_IDS = 4096;

    public SyncFormPacket {
        assignedPowerIds = List.copyOf(assignedPowerIds);
    }

    public static void encode(SyncFormPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeUtf(packet.formId);
        buffer.writeUtf(packet.previousFormId);
        buffer.writeUtf(packet.groupId);
        buffer.writeInt(packet.tier);
        buffer.writeBoolean(packet.enabled);
        buffer.writeVarInt(packet.assignedPowerIds.size());
        for (ResourceLocation powerId : packet.assignedPowerIds) {
            buffer.writeResourceLocation(powerId);
        }
        buffer.writeBoolean(packet.playTransformAnimation);
    }

    public static SyncFormPacket decode(RegistryFriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        String formId = buffer.readUtf(256);
        String previousFormId = buffer.readUtf(256);
        String groupId = buffer.readUtf(256);
        int tier = buffer.readInt();
        boolean enabled = buffer.readBoolean();
        int powerCount = buffer.readVarInt();
        if (powerCount < 0 || powerCount > MAX_POWER_IDS) {
            throw new IllegalArgumentException("Invalid synced form power count: " + powerCount);
        }
        List<ResourceLocation> powerIds = new ArrayList<>(powerCount);
        for (int i = 0; i < powerCount; i++) {
            powerIds.add(buffer.readResourceLocation());
        }
        boolean playTransformAnimation = buffer.readBoolean();
        return new SyncFormPacket(
                entityId,
                formId,
                previousFormId,
                groupId,
                tier,
                enabled,
                powerIds,
                playTransformAnimation
        );
    }

    public static void handle(SyncFormPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> FormSyncClientHandler.apply(packet));
        
    }
}
