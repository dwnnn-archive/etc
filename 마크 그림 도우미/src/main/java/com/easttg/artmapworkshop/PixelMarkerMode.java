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
public enum PixelMarkerMode {
    OFF("OFF"),
    WHITE_SQUARE("\ud770\uc0c9 \ub124\ubaa8"),
    AUTO_CONTRAST("\uc790\ub3d9 \ub300\ube44");

    private final String label;

    private PixelMarkerMode(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public PixelMarkerMode next() {
        PixelMarkerMode[] values = PixelMarkerMode.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}

