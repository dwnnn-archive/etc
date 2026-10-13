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
public enum PaletteMode {
    MEASURED("\uae30\uc874 \uc2e4\uce21", "palette.json"),
    DOSE("\ub3c4\uc2a4 \ud314\ub808\ud2b8", "dose_palette.json");

    private final String label;
    private final String resourceName;

    private PaletteMode(String label, String resourceName) {
        this.label = label;
        this.resourceName = resourceName;
    }

    public String label() {
        return this.label;
    }

    public String resourceName() {
        return this.resourceName;
    }
}

