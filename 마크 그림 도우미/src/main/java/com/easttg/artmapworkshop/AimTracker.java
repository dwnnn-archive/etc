/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1533
 *  net.minecraft.class_2350
 *  net.minecraft.class_243
 *  net.minecraft.class_310
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.CanvasDetector;
import com.easttg.artmapworkshop.ProjectState;
import com.easttg.artmapworkshop.ProjectionState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1533;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_310;

@Environment(value=EnvType.CLIENT)
public final class AimTracker {
    public static boolean valid = false;
    public static int x = -1;
    public static int y = -1;

    private AimTracker() {
    }

    public static void update(class_310 mc) {
        valid = false;
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        class_1533 frame = CanvasDetector.target(mc);
        if (frame == null) {
            return;
        }
        Basis basis = AimTracker.basis(frame);
        class_243 origin = mc.field_1724.method_5836(1.0f);
        class_243 ray = mc.field_1724.method_5828(1.0f).method_1029();
        class_243 center = frame.method_19538().method_1019(basis.normal.method_1021(ProjectionState.surfaceOffset));
        double denom = ray.method_1026(basis.normal);
        if (Math.abs(denom) < 1.0E-6) {
            return;
        }
        double t = center.method_1020(origin).method_1026(basis.normal) / denom;
        if (t <= 0.0 || t > 32.0) {
            return;
        }
        class_243 hit = origin.method_1019(ray.method_1021(t));
        class_243 local = hit.method_1020(center);
        double usable = ProjectionState.planeSize * (1.0 - ProjectionState.inset * 2.0);
        double horizontal = local.method_1026(basis.right);
        double vertical = local.method_1026(basis.up);
        double u = horizontal / usable + 0.5;
        double v = 0.5 - vertical / usable;
        if (u < 0.0 || u >= 1.0 || v < 0.0 || v >= 1.0) {
            return;
        }
        x = Math.max(0, Math.min(31, (int)Math.floor(u * 32.0)));
        y = Math.max(0, Math.min(31, (int)Math.floor(v * 32.0)));
        valid = true;
        ProjectState.cursorX = x;
        ProjectState.cursorY = y;
        ProjectState.row = y;
    }

    private static Basis basis(class_1533 frame) {
        class_243 right;
        class_243 up;
        class_2350 facing = frame.method_5735();
        class_243 normal = new class_243((double)facing.method_10148(), (double)facing.method_10164(), (double)facing.method_10165());
        if (facing.method_10166().method_10179()) {
            up = new class_243(0.0, 1.0, 0.0);
            right = up.method_1036(normal).method_1029();
        } else {
            right = new class_243(1.0, 0.0, 0.0);
            up = normal.method_1036(right).method_1029();
            if (facing == class_2350.field_11033) {
                up = up.method_1021(-1.0);
            }
        }
        return new Basis(right, up, normal);
    }

    @Environment(value=EnvType.CLIENT)
    private record Basis(class_243 right, class_243 up, class_243 normal) {
    }
}

