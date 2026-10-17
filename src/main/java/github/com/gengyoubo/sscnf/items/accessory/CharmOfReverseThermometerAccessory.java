package github.com.gengyoubo.sscnf.items.accessory;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import github.com.gengyoubo.sscnf.items.accessory.AccessoryItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CharmOfReverseThermometerAccessory extends AccessoryItem {
    public CharmOfReverseThermometerAccessory(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.shape-shifter-curse.charm_of_reverse_thermometer.tooltip").withStyle(ChatFormatting.YELLOW));
    }
}
