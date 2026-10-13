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
public enum WorkMode {
    FULL("\uc804\uccb4"),
    ROW("\ud604\uc7ac \ud589"),
    FOCUS("\ud604\uc7ac \uc0c9\uc0c1"),
    INCOMPLETE("\ubbf8\uc644\ub8cc"),
    CURSOR("\ud604\uc7ac \ud53d\uc140");

    private final String label;

    private WorkMode(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public WorkMode next() {
        WorkMode[] values = WorkMode.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}

