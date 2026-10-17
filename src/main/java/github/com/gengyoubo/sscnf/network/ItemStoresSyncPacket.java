package github.com.gengyoubo.sscnf.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Supplier;

/** Owner-only snapshot of persisted virtual item slots. */
public record ItemStoresSyncPacket(CompoundTag stores) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ItemStoresSyncPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "item_stores_sync_packet"));
    @Override public CustomPacketPayload.Type<ItemStoresSyncPacket> type() { return TYPE; }

    public static void encode(ItemStoresSyncPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeNbt(packet.stores);
    }

    public static ItemStoresSyncPacket decode(RegistryFriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readNbt();
        return new ItemStoresSyncPacket(tag == null ? new CompoundTag() : tag);
    }

    public static void handle(ItemStoresSyncPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> github.com.gengyoubo.sscnf.client.ClientPacketHandlers
                        .setItemStores(packet.stores));
        
    }
}
