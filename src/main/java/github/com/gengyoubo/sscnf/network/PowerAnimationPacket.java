package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.client.PowerAnimationClientHandler;

import java.util.function.Supplier;

/** Replicates a high-priority power animation from the server to tracking clients. */
public record PowerAnimationPacket(int entityId, String animationId, Mode mode, int durationOrCount, boolean refresh) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PowerAnimationPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "power_animation_packet"));
    @Override public CustomPacketPayload.Type<PowerAnimationPacket> type() { return TYPE; }

    public enum Mode { TIME, COUNT, LOOP, STOP }

    public static PowerAnimationPacket start(ResourceLocation animationId, Mode mode, int durationOrCount) {
        return new PowerAnimationPacket(-1, animationId.toString(), mode, durationOrCount, false);
    }

    public static PowerAnimationPacket stop() {
        return new PowerAnimationPacket(-1, "", Mode.STOP, -1, false);
    }

    public static PowerAnimationPacket stopMatching(ResourceLocation animationId) {
        return new PowerAnimationPacket(-1, animationId.toString(), Mode.STOP, -1, false);
    }

    public PowerAnimationPacket forEntity(int entityId) {
        return new PowerAnimationPacket(entityId, animationId, mode, durationOrCount, refresh);
    }

    public PowerAnimationPacket asRefresh() {
        return new PowerAnimationPacket(entityId, animationId, mode, durationOrCount, true);
    }

    public static void encode(PowerAnimationPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeUtf(packet.animationId, 256);
        buffer.writeEnum(packet.mode);
        buffer.writeVarInt(packet.durationOrCount);
        buffer.writeBoolean(packet.refresh);
    }

    public static PowerAnimationPacket decode(RegistryFriendlyByteBuf buffer) {
        return new PowerAnimationPacket(buffer.readInt(), buffer.readUtf(256), buffer.readEnum(Mode.class),
                buffer.readVarInt(), buffer.readBoolean());
    }

    public static void handle(PowerAnimationPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> PowerAnimationClientHandler.apply(packet));
        
    }
}
