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
public enum ColorMatchMode {
    CLASSIC("\uae30\uc874 CIE Lab"),
    SOFT_TONE("\uac1c\uc120 \uc0c9\uac10 V1"),
    ADAPTIVE_TONE_V2("\uac1c\uc120 \uc0c9\uac10 V2");

    private final String label;

    private ColorMatchMode(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}

