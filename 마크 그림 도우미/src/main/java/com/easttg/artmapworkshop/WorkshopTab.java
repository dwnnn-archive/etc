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
public enum WorkshopTab {
    IMAGE("\uc774\ubbf8\uc9c0 \uc900\ube44"),
    PALETTE("\uc0c9\uac10 \uc120\ud0dd"),
    WORK("\uadf8\ub9bc \uc791\uc5c5"),
    ACCESSIBILITY("\ubcf4\uae30 \ud3b8\ud558\uac8c"),
    SETTINGS("\ud654\uba74 \uc124\uc815"),
    TUTORIAL("\uc0ac\uc6a9\ubc95");

    private final String label;

    private WorkshopTab(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}

