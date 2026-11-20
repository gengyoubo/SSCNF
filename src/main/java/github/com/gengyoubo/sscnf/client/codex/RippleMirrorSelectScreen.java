package github.com.gengyoubo.sscnf.client.codex;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import github.com.gengyoubo.sscnf.network.ModNetwork;
import github.com.gengyoubo.sscnf.network.SelectMirrorFormPacket;

import java.util.List;
import java.util.UUID;

/** Reuses the paged layout, with only the server-authorized cosmetic choices. */
public final class RippleMirrorSelectScreen extends NormalFormSelectScreen {
    private final List<ResourceLocation> forms;

    public RippleMirrorSelectScreen(String playerName, UUID playerId, List<ResourceLocation> forms) {
        super(Component.translatable("item.shape-shifter-curse.ripple_mirror"), playerName, playerId);
        this.forms = List.copyOf(forms);
    }

    @Override
    protected List<ResourceLocation> getAvailableForms() {
        return forms;
    }

    @Override
    protected void sendSetForm(ResourceLocation formId) {
        net.neoforged.neoforge.network.PacketDistributor.sendToServer(new SelectMirrorFormPacket(formId));
    }
}
