package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge;
import github.com.gengyoubo.sscnf.other.perk.PerkService;

import java.util.function.Supplier;

/** Client request only; all Perk validation and persistence remain on the server. */
public record UnlockPerkPacket(ResourceLocation perkId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UnlockPerkPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "unlock_perk_packet"));
    @Override public CustomPacketPayload.Type<UnlockPerkPacket> type() { return TYPE; }

    public static void encode(UnlockPerkPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceLocation(packet.perkId);
    }

    public static UnlockPerkPacket decode(RegistryFriendlyByteBuf buffer) {
        return new UnlockPerkPacket(buffer.readResourceLocation());
    }

    public static void handle(UnlockPerkPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> {
            var player = (context.player() instanceof net.minecraft.server.level.ServerPlayer sender ? sender : null);
            if (player == null) return;
            PerkService.UnlockResult result = PerkService.unlock(player, packet.perkId);
            String key = "screen." + ShapeShifterCurseNeoForge.RESOURCE_NAMESPACE + ".form_attuner.purchase."
                    + result.name().toLowerCase(java.util.Locale.ROOT);
            player.displayClientMessage(Component.translatable(key), true);
            ModNetwork.refreshFormAttuner(player, key);
        });
        
    }
}
