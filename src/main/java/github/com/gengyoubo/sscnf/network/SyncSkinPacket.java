package github.com.gengyoubo.sscnf.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.api.PlayerSkinData;
import github.com.gengyoubo.sscnf.api.SscApi;
import github.com.gengyoubo.sscnf.client.render.FormTextureUtils;

import java.util.function.Supplier;

/** Server to client skin state broadcast (mirrors CCA sync incl. self). */
public record SyncSkinPacket(int entityId, boolean keepOriginalSkin, boolean enableFormColor,
                             int primaryColor, int accentColor1, int accentColor2,
                             int eyeColorA, int eyeColorB,
                             boolean primaryGreyReverse, boolean accent1GreyReverse,
                             boolean accent2GreyReverse, boolean enableFormRandomSound) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncSkinPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "sync_skin_packet"));
    @Override public CustomPacketPayload.Type<SyncSkinPacket> type() { return TYPE; }

    public static SyncSkinPacket forPlayer(Player player,
                                           PlayerSkinData data) {
        return new SyncSkinPacket(player.getId(), data.isKeepOriginalSkin(), data.isEnableFormColor(),
                data.getFormColor().primaryColor(), data.getFormColor().accentColor1(),
                data.getFormColor().accentColor2(), data.getFormColor().eyeColorA(),
                data.getFormColor().eyeColorB(), data.getFormColor().primaryGreyReverse(),
                data.getFormColor().accent1GreyReverse(), data.getFormColor().accent2GreyReverse(),
                data.isEnableFormRandomSound());
    }

    public static void encode(SyncSkinPacket packet, RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeBoolean(packet.keepOriginalSkin);
        buffer.writeBoolean(packet.enableFormColor);
        buffer.writeInt(packet.primaryColor);
        buffer.writeInt(packet.accentColor1);
        buffer.writeInt(packet.accentColor2);
        buffer.writeInt(packet.eyeColorA);
        buffer.writeInt(packet.eyeColorB);
        buffer.writeBoolean(packet.primaryGreyReverse);
        buffer.writeBoolean(packet.accent1GreyReverse);
        buffer.writeBoolean(packet.accent2GreyReverse);
        buffer.writeBoolean(packet.enableFormRandomSound);
    }

    public static SyncSkinPacket decode(RegistryFriendlyByteBuf buffer) {
        return new SyncSkinPacket(buffer.readInt(), buffer.readBoolean(), buffer.readBoolean(),
                buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(),
                buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }

    public static void handle(SyncSkinPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) {
                return;
            }
            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
            if (entity instanceof Player player) {
                SscApi.currentSkin(player).ifPresent(data -> {
                    data.setKeepOriginalSkin(packet.keepOriginalSkin);
                    data.setEnableFormColor(packet.enableFormColor);
                    data.setFormColor(new FormTextureUtils.ColorSetting(packet.primaryColor, packet.accentColor1,
                            packet.accentColor2, packet.eyeColorA, packet.eyeColorB,
                            packet.primaryGreyReverse, packet.accent1GreyReverse, packet.accent2GreyReverse));
                    data.setEnableFormRandomSound(packet.enableFormRandomSound);
                });
            }
        });
        
    }
}
