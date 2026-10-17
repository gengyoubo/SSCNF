package github.com.gengyoubo.sscnf.api;

import github.com.gengyoubo.sscnf.client.render.FormTextureUtils;

/** Stable public access contract for a player's form skin settings. */
public interface PlayerSkinData {
    boolean isKeepOriginalSkin();

    void setKeepOriginalSkin(boolean keepOriginalSkin);

    boolean isEnableFormColor();

    void setEnableFormColor(boolean enableFormColor);

    FormTextureUtils.ColorSetting getFormColor();

    void setFormColor(FormTextureUtils.ColorSetting formColor);

    boolean isEnableFormRandomSound();

    void setEnableFormRandomSound(boolean enableFormRandomSound);

    void copyFrom(PlayerSkinData other);
}
