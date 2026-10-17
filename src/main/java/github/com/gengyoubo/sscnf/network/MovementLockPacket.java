package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.ClientPacketHandlers;

import java.util.function.Supplier;

/** Re-locks the local player's movement/jump for the current transform phase. */
public record MovementLockPacket(int noMoveTicks, int noJumpTicks) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MovementLockPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "movement_lock_packet"));
    @Override public CustomPacketPayload.Type<MovementLockPacket> type() { return TYPE; }

    public static void encode(MovementLockPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(packet.noMoveTicks);
        buffer.writeInt(packet.noJumpTicks);
    }

    public static MovementLockPacket decode(RegistryFriendlyByteBuf buffer) {
        return new MovementLockPacket(buffer.readInt(), buffer.readInt());
    }

    public static void handle(MovementLockPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> ClientPacketHandlers.applyMovementLock(packet.noMoveTicks(), packet.noJumpTicks()));
        
    }
}
