package github.com.gengyoubo.sscnf.network;

import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import github.com.gengyoubo.sscnf.form.FormManager;
import github.com.gengyoubo.sscnf.form.FormRegistry;
import github.com.gengyoubo.sscnf.other.advancement.SscAdvancementTriggers;

import java.util.function.Supplier;

/**
 * Start-book confirm button (client to server, empty). Enables the mod for players
 * still on the pre-enable form, mirroring Fabric's VALIDATE_START_BOOK_BUTTON flow.
 */
@SuppressWarnings("EmptyMethod")
public record ValidateStartBookPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ValidateStartBookPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shape_shifter_curse", "validate_start_book_packet"));
    @Override public CustomPacketPayload.Type<ValidateStartBookPacket> type() { return TYPE; }

    public static void encode(ValidateStartBookPacket packet, RegistryFriendlyByteBuf buffer) {
    }

    public static ValidateStartBookPacket decode(RegistryFriendlyByteBuf buffer) {
        return new ValidateStartBookPacket();
    }

    public static void handle(ValidateStartBookPacket packet, IPayloadContext context) {
        
        context.enqueueWork(() -> {
            ServerPlayer player = (context.player() instanceof net.minecraft.server.level.ServerPlayer sender ? sender : null);
            if (player == null) {
                return;
            }
            ResourceLocation current = FormManager.current(player).id();
            if (FormRegistry.ORIGINAL_BEFORE_ENABLE.equals(current)) {
                FormManager.setForm(player, FormRegistry.ORIGINAL_SHIFTER);
                SscAdvancementTriggers.ON_ENABLE_MOD.trigger(player);
                player.sendSystemMessage(Component.translatable(
                        "info.shape-shifter-curse.on_enable_mod"
                ).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        });
        
    }
}
