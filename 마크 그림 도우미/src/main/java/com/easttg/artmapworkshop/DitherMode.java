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
public enum DitherMode {
    OFF("\ub044\uae30"),
    FLOYD_STEINBERG("Floyd-Steinberg"),
    BAYER_4X4("Bayer 4x4"),
    BAYER_8X8("Bayer 8x8");

    private final String label;

    private DitherMode(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public DitherMode next() {
        DitherMode[] v = DitherMode.values();
        return v[(this.ordinal() + 1) % v.length];
    }
}

