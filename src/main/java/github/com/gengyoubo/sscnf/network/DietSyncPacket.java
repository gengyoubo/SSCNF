package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.diet.DietInheritanceService;
import github.com.gengyoubo.sscnf.diet.RecipeDietGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/** Compact item registry IDs are Forge-synchronized before play packets arrive. */
public record DietSyncPacket(Map<Integer, Integer> entries) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DietSyncPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "diet_sync_packet"));
    @Override public CustomPacketPayload.Type<DietSyncPacket> type() { return TYPE; }

    private static final int MAX_ENTRIES = 100_000;

    public DietSyncPacket {
        entries = Map.copyOf(entries);
        if (entries.size() > MAX_ENTRIES) throw new IllegalArgumentException("Diet cache too large");
    }

    public static void encode(DietSyncPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.entries.size());
        packet.entries.forEach((id, flags) -> {
            buffer.writeVarInt(id);
            buffer.writeByte(flags);
        });
    }

    public static DietSyncPacket decode(RegistryFriendlyByteBuf buffer) {
        int count = buffer.readVarInt();
        if (count < 0 || count > MAX_ENTRIES) throw new IllegalArgumentException("Invalid diet cache size");
        Map<Integer, Integer> entries = new HashMap<>();
        for (int index = 0; index < count; index++) {
            int id = buffer.readVarInt();
            int flags = buffer.readUnsignedByte();
            if (id < 0 || (flags & ~RecipeDietGraph.ALL) != 0) {
                throw new IllegalArgumentException("Invalid diet cache entry");
            }
            entries.put(id, flags);
        }
        return new DietSyncPacket(entries);
    }

    public static void handle(DietSyncPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> DietInheritanceService.acceptClientSnapshot(packet.entries));
        
    }
}
