package github.com.gengyoubo.sscnf.client.screen;

import github.com.gengyoubo.sscnf.client.ColorAutoSync;
import github.com.gengyoubo.sscnf.client.codex.FormColorSelectMenu;
import github.com.gengyoubo.sscnf.client.codex.FormColorSelectMenuV2;
import github.com.gengyoubo.sscnf.client.color.FormColorData;
import github.com.gengyoubo.sscnf.other.config.SscClientConfig;
import github.com.gengyoubo.sscnf.other.config.SscCommonConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Mods list config entry, backed by the same config values used in gameplay. */
public final class SscConfigScreen extends Screen {
    private static final int PAGES = 3;
    private static final int ROW_HEIGHT = 24;
    private final Screen parent;
    private final List<Row> rows = new ArrayList<>();
    private int page;
    private int offset;

    private record Row(String key, Supplier<String> value, Consumer<String> set, boolean toggle) {
    }

    public SscConfigScreen(Screen parent) {
        super(Component.translatable("text.shape-shifter-curse.config.title"));
        this.parent = parent;
    }

    private void bool(String key, ModConfigSpec.BooleanValue value) {
        rows.add(new Row(key, () -> Boolean.toString(value.get()), text -> value.set(Boolean.parseBoolean(text)), true));
    }

    private void integer(String key, ModConfigSpec.IntValue value, int min, int max) {
        rows.add(new Row(key, () -> value.get().toString(), text -> {
            try {
                int number = Integer.parseInt(text);
                if (number >= min && number <= max) value.set(number);
            } catch (NumberFormatException ignored) {
            }
        }, false));
    }

    private void decimal(String key, ModConfigSpec.DoubleValue value) {
        rows.add(new Row(key, () -> value.get().toString(), text -> {
            try {
                double number = Double.parseDouble(text);
                if (Double.isFinite(number) && number >= 0 && number <= 1) value.set(number);
            } catch (NumberFormatException ignored) {
            }
        }, false));
    }

    private void populate() {
        rows.clear();
        if (page == 0) {
            bool("big_screen", SscClientConfig.BIG_CODEX);
            bool("first_person_form_arms", SscClientConfig.FIRST_PERSON_FORM_ARMS);
            bool("ignore_hide_arms", SscClientConfig.IGNORE_HIDE_ARMS);
            bool("change_fpm_config", SscClientConfig.CHANGE_FPM_CONFIG);
            integer("blink_min_interval", SscClientConfig.BLINK_MIN_INTERVAL, 1, 12000);
            integer("blink_max_interval", SscClientConfig.BLINK_MAX_INTERVAL, 1, 12000);
            integer("blink_duration", SscClientConfig.BLINK_DURATION, 1, 1200);
            integer("instinct_position", SscClientConfig.INSTINCT_POSITION, 1, 9);
            integer("instinct_offset_x", SscClientConfig.INSTINCT_OFFSET_X, -10000, 10000);
            integer("instinct_offset_y", SscClientConfig.INSTINCT_OFFSET_Y, -10000, 10000);
            integer("mana_position", SscClientConfig.MANA_POSITION, 1, 9);
            integer("mana_offset_x", SscClientConfig.MANA_OFFSET_X, -10000, 10000);
            integer("mana_offset_y", SscClientConfig.MANA_OFFSET_Y, -10000, 10000);
            integer("magic_position", SscClientConfig.MAGIC_POSITION, 1, 9);
            integer("magic_offset_x", SscClientConfig.MAGIC_OFFSET_X, -10000, 10000);
            integer("magic_offset_y", SscClientConfig.MAGIC_OFFSET_Y, -10000, 10000);
            integer("item_store_position", SscClientConfig.ITEM_STORE_POSITION, 1, 9);
            integer("item_store_offset_x", SscClientConfig.ITEM_STORE_OFFSET_X, -10000, 10000);
            integer("item_store_offset_y", SscClientConfig.ITEM_STORE_OFFSET_Y, -10000, 10000);
        } else if (page == 1) {
            bool("disable_color_unlock_check", SscClientConfig.DISABLE_COLOR_UNLOCK_CHECK);
            bool("fcs_use_v1_menu", SscClientConfig.FCS_USE_V1_MENU);
            bool("auto_sync_config", SscClientConfig.CUSTOM_AUTO_SYNC_CONFIG);
            bool("enable_server_modify_fcd", SscClientConfig.CUSTOM_ENABLE_SERVER_MODIFY_FCD);
            bool("enable_form_default_color_system", SscClientConfig.CUSTOM_ENABLE_FORM_DEFAULT_COLOR_SYSTEM);
            bool("auto_sync_color_config", SscClientConfig.CUSTOM_AUTO_SYNC_COLOR_CONFIG);
            bool("keep_original_skin", SscClientConfig.CUSTOM_KEEP_ORIGINAL_SKIN);
            bool("enable_form_color", SscClientConfig.CUSTOM_ENABLE_FORM_COLOR);
            bool("enable_form_random_sound", SscClientConfig.CUSTOM_ENABLE_FORM_RANDOM_SOUND);
        } else {
            bool("immediate_transform", SscCommonConfig.IMMEDIATE_TRANSFORM);
            bool("food_habit", SscCommonConfig.FOOD_HABIT);
            bool("full_stack_upgrade", SscCommonConfig.FULL_STACK_UPGRADE);
            bool("initial_form_enabled", SscCommonConfig.ENABLE_INITIAL_FORM);
            rows.add(new Row("initial_forms", () -> String.join(", ", SscCommonConfig.INITIAL_FORMS.get()), text ->
                    SscCommonConfig.INITIAL_FORMS.set(Arrays.stream(text.split(","))
                            .map(String::trim).filter(part -> !part.isEmpty()).toList()), false));
            bool("allow_sleep", SscCommonConfig.ALLOW_SLEEP_IN_CURSED_MOON);
            bool("enable_transform", SscCommonConfig.ENABLE_CURSED_MOON_TRANSFORM);
            bool("movement_logging", SscCommonConfig.ENABLE_MOVEMENT_DEBUG_LOGGING);
            bool("witch_potion_for_pre_book", SscCommonConfig.WITCH_POTION_FOR_PRE_BOOK);
            bool("status_potion_with_curse", SscCommonConfig.STATUS_POTION_WITH_CURSE);
            decimal("bat_chance", SscCommonConfig.TRANSFORMATIVE_BAT_SPAWN_CHANCE);
            decimal("axolotl_chance", SscCommonConfig.TRANSFORMATIVE_AXOLOTL_SPAWN_CHANCE);
            decimal("ocelot_chance", SscCommonConfig.TRANSFORMATIVE_OCELOT_SPAWN_CHANCE);
            decimal("wolf_chance", SscCommonConfig.TRANSFORMATIVE_WOLF_SPAWN_CHANCE);
            decimal("spider_chance", SscCommonConfig.TRANSFORMATIVE_SPIDER_SPAWN_CHANCE);
            rows.add(new Row("moon_phases", () -> SscCommonConfig.CURSED_MOON_PHASES.get().toString()
                    .replace("[", "").replace("]", ""), SscConfigScreen::setMoonPhases, false));
        }
    }

    /** Applies a comma-separated phase list; any invalid entry leaves the config unchanged. */
    private static void setMoonPhases(String text) {
        try {
            List<Integer> phases = new ArrayList<>();
            if (!text.isBlank()) {
                for (String part : text.split(",")) {
                    int phase = Integer.parseInt(part.trim());
                    if (phase < 0 || phase > 7) return;
                    phases.add(phase);
                }
            }
            SscCommonConfig.CURSED_MOON_PHASES.set(phases);
        } catch (NumberFormatException ignored) {
        }
    }

    private static Component label(String key) {
        return Component.translatable("screen.shape-shifter-curse.config." + key);
    }

    private static Component toggleLabel(Row row) {
        return Component.translatable(Boolean.parseBoolean(row.value.get()) ? "options.on" : "options.off");
    }

    private int left() {
        return Math.max(8, width / 2 - 170);
    }

    private int visibleRows() {
        return Math.max(1, (height - 125) / ROW_HEIGHT);
    }

    private int maxOffset() {
        return Math.max(0, rows.size() - visibleRows());
    }

    private void changePage(int delta) {
        page = Math.floorMod(page + delta, PAGES);
        offset = 0;
        rebuildWidgets();
    }

    private void scrollBy(int delta) {
        offset = Math.max(0, Math.min(maxOffset(), offset + delta));
        rebuildWidgets();
    }

    @Override
    protected void init() {
        populate();
        int left = left();
        int top = 52;
        offset = Math.max(0, Math.min(offset, maxOffset()));
        for (int i = offset; i < Math.min(rows.size(), offset + visibleRows()); i++) {
            Row row = rows.get(i);
            int y = top + (i - offset) * ROW_HEIGHT;
            if (row.toggle) {
                addRenderableWidget(Button.builder(toggleLabel(row), button -> {
                    row.set.accept(Boolean.toString(!Boolean.parseBoolean(row.value.get())));
                    button.setMessage(toggleLabel(row));
                }).pos(left + 235, y).size(100, 20).build());
            } else {
                EditBox edit = new EditBox(font, left + 235, y, 100, 20, label(row.key));
                edit.setMaxLength(2048);
                edit.setValue(row.value.get());
                edit.setResponder(row.set);
                addRenderableWidget(edit);
            }
        }
        addRenderableWidget(Button.builder(Component.literal("<"), button -> changePage(-1)).bounds(left, 25, 30, 20).build());
        addRenderableWidget(Button.builder(Component.literal(">"), button -> changePage(1)).bounds(left + 305, 25, 30, 20).build());
        addRenderableWidget(Button.builder(Component.literal("▲"), button -> scrollBy(-1)).bounds(left + 180, height - 67, 35, 20).build());
        addRenderableWidget(Button.builder(Component.literal("▼"), button -> scrollBy(1)).bounds(left + 220, height - 67, 35, 20).build());
        if (page == 1) {
            addRenderableWidget(Button.builder(label("colors"), button ->
                    minecraft.setScreen(new FormColorSelectMenuV2(label("colors"), this))).bounds(left, height - 67, 85, 20).build());
            addRenderableWidget(Button.builder(label("colors_v1"), button ->
                    minecraft.setScreen(new FormColorSelectMenu(label("colors_v1"), this))).bounds(left + 88, height - 67, 85, 20).build());
            addRenderableWidget(Button.builder(label("unlock_all"), button ->
                    FormColorData.client().unlockAll()).bounds(left, height - 44, 165, 20).build());
            addRenderableWidget(Button.builder(label("clear_unlocks"), button -> {
                FormColorData.client().unlockedForms.clear();
                FormColorData.client().writeToConfig();
            }).bounds(left + 170, height - 44, 165, 20).build());
        }
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(width / 2 - 50, height - 21, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // super.render() draws the blurred background, so the text has to come after it to stay sharp.
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 8, 0xFFFFFF);
        graphics.drawCenteredString(font, label(page == 0 ? "client" : page == 1 ? "custom" : "common"), width / 2, 31, 0xFFFFFF);
        int left = left();
        for (int i = offset; i < Math.min(rows.size(), offset + visibleRows()); i++) {
            graphics.drawString(font, label(rows.get(i).key), left, 58 + (i - offset) * ROW_HEIGHT, 0xFFFFFF);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        scrollBy(scrollY < 0 ? 1 : -1);
        return true;
    }

    @Override
    public void onClose() {
        SscClientConfig.SPEC.save();
        SscCommonConfig.SPEC.save();
        ColorAutoSync.syncNow();
        minecraft.setScreen(parent);
    }
}
