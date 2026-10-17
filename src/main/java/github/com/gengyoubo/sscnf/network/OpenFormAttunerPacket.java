package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

/** Server-authoritative request to show the temporary Form Attuner status screen. */
public record OpenFormAttunerPacket(int level, int maxLevel, String formGroupId, Set<String> unlockedPerks,
                                    String statusKey) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenFormAttunerPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "open_form_attuner_packet"));
    @Override public CustomPacketPayload.Type<OpenFormAttunerPacket> type() { return TYPE; }

    public static void encode(OpenFormAttunerPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.level);
        buffer.writeVarInt(packet.maxLevel);
        buffer.writeUtf(packet.formGroupId, 256);
        buffer.writeVarInt(packet.unlockedPerks.size());
        packet.unlockedPerks.forEach(id -> buffer.writeUtf(id, 256));
        buffer.writeUtf(packet.statusKey, 256);
    }

    public static OpenFormAttunerPacket decode(RegistryFriendlyByteBuf buffer) {
        int level = buffer.readVarInt();
        int maxLevel = buffer.readVarInt();
        String formGroupId = buffer.readUtf(256);
        int size = buffer.readVarInt();
        if (size < 0 || size > 512) throw new IllegalArgumentException("Invalid Form Attuner perk count: " + size);
        Set<String> unlockedPerks = new LinkedHashSet<>();
        for (int index = 0; index < size; index++) unlockedPerks.add(buffer.readUtf(256));
        return new OpenFormAttunerPacket(level, maxLevel, formGroupId, Set.copyOf(unlockedPerks), buffer.readUtf(256));
    }

    public static void handle(OpenFormAttunerPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> github.com.gengyoubo.sscnf.client.ClientPacketHandlers.openFormAttuner(
                        packet.level, packet.maxLevel, packet.formGroupId, packet.unlockedPerks, packet.statusKey));
        
    }
}