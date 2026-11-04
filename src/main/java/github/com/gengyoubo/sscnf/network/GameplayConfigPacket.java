package github.com.gengyoubo.sscnf.network;

import github.com.gengyoubo.sscnf.other.config.GameplayConfigState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GameplayConfigPacket(boolean foodHabit, boolean fullStack) implements CustomPacketPayload {
    public static final Type<GameplayConfigPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "gameplay_config"));
    @Override public Type<GameplayConfigPacket> type() { return TYPE; }
    public static void encode(GameplayConfigPacket packet, RegistryFriendlyByteBuf buffer) { buffer.writeBoolean(packet.foodHabit); buffer.writeBoolean(packet.fullStack); }
    public static GameplayConfigPacket decode(RegistryFriendlyByteBuf buffer) { return new GameplayConfigPacket(buffer.readBoolean(), buffer.readBoolean()); }
    public static void handle(GameplayConfigPacket packet, IPayloadContext context) { context.enqueueWork(() -> GameplayConfigState.setClient(packet.foodHabit, packet.fullStack)); }
}
