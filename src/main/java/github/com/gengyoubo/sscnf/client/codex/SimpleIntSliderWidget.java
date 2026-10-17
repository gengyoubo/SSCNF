package github.com.gengyoubo.sscnf.client.codex;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/** Integer slider over [minValue, maxValue] with a change callback. */
public class SimpleIntSliderWidget extends AbstractSliderButton {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/slider.png");

    public final int minValue;
    public final int maxValue;

    public int intValue = 0;
    public Consumer<SimpleIntSliderWidget> onChanged = null;

    public SimpleIntSliderWidget(int x, int y, int width, int height, Component message,
                                 double value, int minValue, int maxValue) {
        super(x, y, width, height, message, value);
        this.minValue = minValue;
        this.maxValue = maxValue;
        if (this.maxValue == this.minValue) {
            throw new IllegalArgumentException("Max value must be greater than min value");
        }
    }

    @Override
    protected void updateMessage() {
    }

    @Override
    protected void applyValue() {
        double value = this.value;
        this.intValue = (int) (value * (this.maxValue - this.minValue) + this.minValue);
        if (this.onChanged != null) {
            this.onChanged.accept(this);
        }
    }

    public void setIntValue(int value) {
        this.value = (value - this.minValue) / (double) (this.maxValue - this.minValue);
        this.applyValue();
    }

    public int getIntValue() {
        return this.intValue;
    }
}
