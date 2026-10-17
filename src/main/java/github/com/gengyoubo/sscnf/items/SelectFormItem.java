package github.com.gengyoubo.sscnf.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import github.com.gengyoubo.sscnf.network.ModNetwork;

import javax.annotation.Nullable;
import java.util.List;

/** Admin scroll: opens the form select menu for yourself, or for a right-clicked player. */
public final class SelectFormItem extends Item {
    public SelectFormItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            ModNetwork.sendOpenSelectForm(serverPlayer, player);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    /** Forge has no Item#useOnEntity hook; entity interaction arrives as an event. */
    @net.neoforged.fml.common.EventBusSubscriber(
            modid = github.com.gengyoubo.sscnf.ShapeShifterCurseNeoForge.MOD_ID,
            bus = net.neoforged.fml.common.EventBusSubscriber.Bus.GAME)
    public static final class EntityInteractHandler {
        private EntityInteractHandler() {
        }

        @net.neoforged.bus.api.SubscribeEvent
        public static void useOnEntity(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract event) {
            if (event.getLevel().isClientSide || event.getHand() != InteractionHand.MAIN_HAND) {
                return;
            }
            if (!(event.getEntity() instanceof ServerPlayer user)) {
                return;
            }
            if (!(event.getTarget() instanceof Player target)) {
                return;
            }
            if (!(event.getItemStack().getItem() instanceof SelectFormItem)) {
                return;
            }
            ModNetwork.sendOpenSelectForm(user, target);
            event.setCanceled(true);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.shape-shifter-curse.select_form_item.tooltip")
                .withStyle(ChatFormatting.YELLOW));
    }
}
