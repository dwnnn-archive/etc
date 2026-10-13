/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.ProjectState;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class ProjectionState {
    public static boolean enabled = true;
    public static UUID targetFrameId = null;
    public static double planeSize = 0.96;
    public static double inset = 0.015;
    public static final double SNAP_SURFACE_DEPTH = 0.0625;
    public static final double SNAP_EPSILON = 0.001;
    public static double surfaceOffset = 0.0635;
    public static final double OFFSET_STEP = 0.001;

    private ProjectionState() {
    }

    public static void snapToSurface() {
        surfaceOffset = 0.0635;
        ProjectState.saveProgress();
    }

    public static void forward() {
        surfaceOffset = Math.min(0.1, surfaceOffset + 0.001);
        ProjectState.saveProgress();
    }

    public static void backward() {
        surfaceOffset = Math.max(0.045, surfaceOffset - 0.001);
        ProjectState.saveProgress();
    }

    public static String offsetText() {
        return String.format("%.4f", surfaceOffset);
    }
}

