/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1533
 *  net.minecraft.class_239
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_3966
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.ProjectionState;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_3966;

@Environment(value=EnvType.CLIENT)
public final class CanvasDetector {
    private CanvasDetector() {
    }

    public static boolean detect(class_310 mc) {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return false;
        }
        List frames = mc.field_1687.method_8390(class_1533.class, mc.field_1724.method_5829().method_1014(16.0), frame -> true);
        class_1297 hit = null;
        class_239 class_2392 = mc.field_1765;
        if (class_2392 instanceof class_3966) {
            class_3966 ehr = (class_3966)class_2392;
            hit = ehr.method_17782();
        }
        class_1533 selected = null;
        if (hit instanceof class_1533) {
            class_1533 frame2;
            selected = frame2 = (class_1533)hit;
        }
        if (selected == null && hit != null) {
            class_243 hp = hit.method_19538();
            selected = frames.stream().filter(f -> f.method_19538().method_1022(hp) <= 2.0).min(Comparator.comparingDouble(f -> f.method_19538().method_1025(hp))).orElse(null);
        }
        if (selected == null) {
            selected = CanvasDetector.rayCandidate(mc, frames);
        }
        if (selected == null) {
            selected = frames.stream().min(Comparator.comparingDouble(f -> f.method_5858((class_1297)mc.field_1724))).orElse(null);
        }
        if (selected == null) {
            ProjectionState.targetFrameId = null;
            CanvasDetector.message(mc, "ArtMap: ItemFrame \uac10\uc9c0 \uc2e4\ud328 | nearby=" + frames.size());
            return false;
        }
        ProjectionState.targetFrameId = selected.method_5667();
        double distance = Math.sqrt(selected.method_5858((class_1297)mc.field_1724));
        CanvasDetector.message(mc, "ArtMap: ItemFrame \uac10\uc9c0 \uc131\uacf5 | " + String.format("%.2fm", distance) + " | facing=" + selected.method_5735().method_10151() + " | nearby=" + frames.size());
        return true;
    }

    public static class_1533 target(class_310 mc) {
        if (ProjectionState.targetFrameId == null || mc.field_1724 == null || mc.field_1687 == null) {
            return null;
        }
        List frames = mc.field_1687.method_8390(class_1533.class, mc.field_1724.method_5829().method_1014(32.0), frame -> true);
        for (class_1533 frame2 : frames) {
            if (!frame2.method_5667().equals(ProjectionState.targetFrameId)) continue;
            return frame2;
        }
        return null;
    }

    private static class_1533 rayCandidate(class_310 mc, List<class_1533> frames) {
        class_243 eye = mc.field_1724.method_5836(1.0f);
        class_243 look = mc.field_1724.method_5828(1.0f).method_1029();
        class_1533 best = null;
        double bestScore = Double.MAX_VALUE;
        for (class_1533 frame : frames) {
            double score;
            class_243 to = frame.method_19538().method_1020(eye);
            double forward = to.method_1026(look);
            if (forward <= 0.0 || forward > 16.0) continue;
            class_243 nearest = eye.method_1019(look.method_1021(forward));
            double side = frame.method_19538().method_1022(nearest);
            if (side > 1.75 || !((score = side * 12.0 + forward * 0.1) < bestScore)) continue;
            bestScore = score;
            best = frame;
        }
        return best;
    }

    private static void message(class_310 mc, String text) {
        if (mc.field_1724 != null) {
            mc.field_1724.method_7353((class_2561)class_2561.method_43470((String)text), true);
        }
    }
}

