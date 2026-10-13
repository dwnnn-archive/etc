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
public enum Resize32Mode {
    PAD("\ud22c\uba85 \uc5ec\ubc31"),
    FIT("\ube44\uc728 \uc720\uc9c0"),
    STRETCH("\uac15\uc81c \ub9de\ucda4");

    private final String label;

    private Resize32Mode(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public Resize32Mode next() {
        Resize32Mode[] values = Resize32Mode.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}

