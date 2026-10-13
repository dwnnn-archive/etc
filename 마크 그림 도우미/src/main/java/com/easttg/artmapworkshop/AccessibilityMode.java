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
public enum AccessibilityMode {
    NORMAL("\uae30\ubcf8"),
    COLORBLIND("\uc0c9\uac01 \ubcf4\uc870"),
    LOW_VISION("\uc800\uc2dc\ub825"),
    HIGH_CONTRAST("\uace0\ub300\ube44");

    private final String label;

    private AccessibilityMode(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public AccessibilityMode next() {
        AccessibilityMode[] values = AccessibilityMode.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}

