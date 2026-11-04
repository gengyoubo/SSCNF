package github.com.gengyoubo.sscnf.other.perk;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import github.com.gengyoubo.sscnf.api.SscApi;
import github.com.gengyoubo.sscnf.api.registry.Perk;
import github.com.gengyoubo.sscnf.api.registry.PerkTree;
import github.com.gengyoubo.sscnf.api.registry.SscJavaRegistries;
import github.com.gengyoubo.sscnf.block.entity.FormAttunerBlockEntity;

import java.util.Objects;

/** Server-authoritative Perk query and purchase API. */
public final class PerkService {
    private PerkService() {
    }

    public static boolean hasUnlocked(ServerPlayer player, ResourceLocation perkId) {
        return player != null && perkId != null
                && SscApi.currentForm(player).map(data -> data.hasUnlockedPerk(perkId)).orElse(false);
    }

    /**
     * Attempts one purchase from the player's last-used Form Attuner. Call only from server-side
     * menu/network handling; clients must never mutate the capability directly.
     */
    public static UnlockResult unlock(ServerPlayer player, ResourceLocation perkId) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(perkId, "perkId");
        Perk perk = SscJavaRegistries.perk(perkId).orElse(null);
        PerkTree tree = SscJavaRegistries.perkTreeFor(perkId).orElse(null);
        if (perk == null || tree == null) return UnlockResult.UNKNOWN_PERK;
        if (!perk.implemented()) return UnlockResult.NOT_IMPLEMENTED;

        var data = SscApi.currentForm(player).orElse(null);
        if (data == null) return UnlockResult.NO_PLAYER_DATA;
        if (data.hasUnlockedPerk(perkId)) return UnlockResult.ALREADY_UNLOCKED;
        if (!tree.formGroupId().toString().equals(data.getFormGroupId())
                || !tree.matchesForm(ResourceLocation.tryParse(data.getFormId()))) return UnlockResult.WRONG_FORM_GROUP;

        FormAttunerBlockEntity attuner = FormAttunerBlockEntity.getLastUsed(player);
        if (attuner == null || player.distanceToSqr(attuner.getBlockPos().getX() + 0.5,
                attuner.getBlockPos().getY() + 0.5, attuner.getBlockPos().getZ() + 0.5) > 64
                || attuner.getAttunementLevel() < perk.requiredAttunerLevel()) {
            return UnlockResult.ATTUNER_TOO_WEAK;
        }
        if (!SscJavaRegistries.requiredPerks(perkId).stream().allMatch(data::hasUnlockedPerk)) {
            return UnlockResult.MISSING_PREREQUISITE;
        }
        if (!perk.canUnlock(player)) return UnlockResult.CUSTOM_REQUIREMENT_FAILED;
        if (!player.getAbilities().instabuild && (player.experienceLevel < perk.experienceLevels()
                || experienceAfterLevelCost(player, perk.experienceLevels()) < perk.experiencePoints())) {
            return UnlockResult.INSUFFICIENT_EXPERIENCE;
        }

        var costItem = perk.costItem() == null ? null : net.minecraft.core.registries.BuiltInRegistries.ITEM.getOptional(perk.costItem()).orElse(null);
        int available = 0;
        if (costItem != null) for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            var stack = player.getInventory().getItem(i);
            if (stack.is(costItem)) available += stack.getCount();
        }
        if (!player.getAbilities().instabuild && perk.itemCost() > available) return UnlockResult.INSUFFICIENT_ITEMS;
        if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-perk.experienceLevels());
            player.giveExperiencePoints(-perk.experiencePoints());
            int remaining = perk.itemCost();
            for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
                var stack = player.getInventory().getItem(i);
                if (costItem != null && stack.is(costItem)) {
                    int count = Math.min(stack.getCount(), remaining);
                    stack.shrink(count);
                    remaining -= count;
                }
            }
            player.getInventory().setChanged();
        }
        data.unlockPerk(perkId);
        perk.onUnlocked(player);
        github.com.gengyoubo.sscnf.network.ModNetwork.sendFormSync(player);
        github.com.gengyoubo.sscnf.power.FormActivePowerService.synchronizeMana(player);
        return UnlockResult.SUCCESS;
    }

    private static long experienceAfterLevelCost(ServerPlayer player, int levelCost) {
        int level = Math.max(0, player.experienceLevel - levelCost);
        long base = level <= 16 ? (long) level * level + 6L * level
                : level <= 31 ? (5L * level * level - 81L * level + 720L) / 2
                : (9L * level * level - 325L * level + 4440L) / 2;
        int next = level >= 30 ? 112 + (level - 30) * 9 : level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        return base + (long) (player.experienceProgress * next);
    }

    public enum UnlockResult {
        SUCCESS,
        UNKNOWN_PERK,
        NO_PLAYER_DATA,
        ALREADY_UNLOCKED,
        WRONG_FORM_GROUP,
        ATTUNER_TOO_WEAK,
        MISSING_PREREQUISITE,
        CUSTOM_REQUIREMENT_FAILED,
        INSUFFICIENT_EXPERIENCE,
        INSUFFICIENT_ITEMS,
        NOT_IMPLEMENTED
    }
}
