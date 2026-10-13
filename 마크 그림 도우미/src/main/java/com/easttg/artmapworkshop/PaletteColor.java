/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.easttg.artmapworkshop;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public record PaletteColor(String code, String paintName, String action, int r, int g, int b) {
    public int rgb() {
        return this.r << 16 | this.g << 8 | this.b;
    }

    public String key() {
        return this.code + "|" + this.action;
    }

    public boolean matchesVariant(String variantKey) {
        return variantKey != null && this.key().equals(variantKey);
    }

    public boolean matchesPaint(String paintCode) {
        return paintCode != null && this.code.equals(paintCode);
    }

    public boolean matchesStage(String stage) {
        if (stage == null || stage.isEmpty()) {
            return false;
        }
        if ("\uae30\ubcf8".equals(stage)) {
            return this.action != null && this.action.startsWith("\uae30\ubcf8");
        }
        return stage.equals(this.action);
    }

    public String display() {
        return this.code + " " + this.paintName + " : " + this.action;
    }
}

