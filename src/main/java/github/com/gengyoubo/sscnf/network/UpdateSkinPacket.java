package github.com.gengyoubo.sscnf.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.api.SscApi;
import github.com.gengyoubo.sscnf.client.render.FormTextureUtils;

import java.util.function.Supplier;

/**
 * Client to server skin update. Field order mirrors Fabric's UPDATE_CUSTOM_COLOR:
 * extra flag, optional keep/enable flags, 5 ABGR color ints, 3 grey-reverse flags.
 */
public record UpdateSkinPacket(boolean sendExtraData, boolean keepOriginalSkin, boolean enableFormColor,
                               int primaryColor, int accentColor1, int accentColor2,
                               int eyeColorA, int eyeColorB,
                               boolean primaryGreyReverse, boolean accent1GreyReverse,
                               boolean accent2GreyReverse) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateSkinPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "update_skin_packet"));
    @Override public CustomPacketPayload.Type<UpdateSkinPacket> type() { return TYPE; }

    public static void encode(UpdateSkinPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.sendExtraData);
        if (packet.sendExtraData) {
            buffer.writeBoolean(packet.keepOriginalSkin);
            buffer.writeBoolean(packet.enableFormColor);
        }
        buffer.writeInt(packet.primaryColor);
        buffer.writeInt(packet.accentColor1);
        buffer.writeInt(packet.accentColor2);
        buffer.writeInt(packet.eyeColorA);
        buffer.writeInt(packet.eyeColorB);
        buffer.writeBoolean(packet.primaryGreyReverse);
        buffer.writeBoolean(packet.accent1GreyReverse);
        buffer.writeBoolean(packet.accent2GreyReverse);
    }

    public static UpdateSkinPacket decode(RegistryFriendlyByteBuf buffer) {
        boolean sendExtraData = buffer.readBoolean();
        boolean keepOriginalSkin = false;
        boolean enableFormColor = false;
        if (sendExtraData) {
            keepOriginalSkin = buffer.readBoolean();
            enableFormColor = buffer.readBoolean();
        }
        return new UpdateSkinPacket(sendExtraData, keepOriginalSkin, enableFormColor,
                buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(),
                buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }

    public static void handle(UpdateSkinPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> {
            ServerPlayer player = (context.player() instanceof net.minecraft.server.level.ServerPlayer sender ? sender : null);
            if (player == null) {
                return;
            }
            SscApi.currentSkin(player).ifPresent(data -> {
                if (packet.sendExtraData) {
                    data.setKeepOriginalSkin(packet.keepOriginalSkin);
                    data.setEnableFormColor(packet.enableFormColor);
                }
                data.setFormColor(new FormTextureUtils.ColorSetting(packet.primaryColor, packet.accentColor1,
                        packet.accentColor2, packet.eyeColorA, packet.eyeColorB,
                        packet.primaryGreyReverse, packet.accent1GreyReverse, packet.accent2GreyReverse));
                ModNetwork.sendSkinSync(player);
            });
        });
        
    }
}
