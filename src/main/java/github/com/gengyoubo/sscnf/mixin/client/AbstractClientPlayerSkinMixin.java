package github.com.gengyoubo.sscnf.mixin.client;

import github.com.gengyoubo.sscnf.client.render.FormTextureUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerSkinMixin {
    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    private void ssc$useBaseSkin(CallbackInfoReturnable<PlayerSkin> callback) {
        if (!FormTextureUtils.usesBaseSkin((AbstractClientPlayer) (Object) this)) return;
        PlayerSkin skin = callback.getReturnValue();
        callback.setReturnValue(new PlayerSkin(ResourceLocation.fromNamespaceAndPath("shape-shifter-curse",
                "textures/entity/base_player/ssc_base_skin.png"), null, skin.capeTexture(), skin.elytraTexture(),
                skin.model(), skin.secure()));
    }
}
