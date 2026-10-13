/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1533
 *  net.minecraft.class_1799
 *  net.minecraft.class_1806
 *  net.minecraft.class_1937
 *  net.minecraft.class_22
 *  net.minecraft.class_310
 *  net.minecraft.class_9209
 *  net.minecraft.class_9334
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.ArtImage;
import com.easttg.artmapworkshop.CanvasDetector;
import com.easttg.artmapworkshop.MeasuredMapColors;
import com.easttg.artmapworkshop.ProjectState;
import com.easttg.artmapworkshop.ProjectionState;
import java.util.Arrays;
import java.util.BitSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1533;
import net.minecraft.class_1799;
import net.minecraft.class_1806;
import net.minecraft.class_1937;
import net.minecraft.class_22;
import net.minecraft.class_310;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

@Environment(value=EnvType.CLIENT)
public final class AutoProgress {
    private static byte[] previousColors;
    private static int previousMapId;
    private static final BitSet pendingCells;
    private static ArtImage previousImage;
    private static Object previousLevel;
    private static class_1533 previousFrame;
    private static int previousTile;
    private static final int MAX_CELLS_PER_TICK = 128;
    private static final long BUDGET_NANOS = 2000000L;

    private AutoProgress() {
    }

    public static void reset() {
        previousColors = null;
        previousMapId = Integer.MIN_VALUE;
        pendingCells.clear();
        previousImage = null;
        previousLevel = null;
        previousFrame = null;
        previousTile = -1;
    }

    public static void tick(class_310 mc) {
        if (!ProjectState.autoProgressEnabled || ProjectState.image == null || mc.field_1724 == null || mc.field_1687 == null || ProjectionState.targetFrameId == null) {
            AutoProgress.reset();
            return;
        }
        AutoProgress.sample(mc);
    }

    private static void sample(class_310 mc) {
        block10: {
            try {
                class_1533 frame = CanvasDetector.target(mc);
                if (frame == null) {
                    AutoProgress.reset();
                    return;
                }
                class_1799 stack = frame.method_6940();
                class_9209 mapId = (class_9209)stack.method_57824(class_9334.field_49646);
                if (mapId == null) {
                    return;
                }
                class_22 state = class_1806.method_7997((class_9209)mapId, (class_1937)mc.field_1687);
                if (state == null || state.field_122 == null || state.field_122.length < 16384) {
                    return;
                }
                int id = mapId.comp_2315();
                byte[] colors = state.field_122;
                if (previousColors == null || previousMapId != id || previousColors.length != colors.length || previousImage != ProjectState.image || previousTile != ProjectState.tile || previousLevel != mc.field_1687 || previousFrame != frame) {
                    pendingCells.clear();
                    previousColors = Arrays.copyOf(colors, colors.length);
                    previousMapId = id;
                    previousImage = ProjectState.image;
                    previousTile = ProjectState.tile;
                    previousLevel = mc.field_1687;
                    previousFrame = frame;
                    return;
                }
                for (int i = 0; i < 16384; ++i) {
                    if (previousColors[i] == colors[i]) continue;
                    int mx = i % 128;
                    int my = i / 128;
                    pendingCells.set(my / 4 * 32 + mx / 4);
                    AutoProgress.previousColors[i] = colors[i];
                }
                if (pendingCells.isEmpty()) break block10;
                long started = System.nanoTime();
                int processed = 0;
                int cell = pendingCells.nextSetBit(0);
                while (cell >= 0) {
                    int x = cell % 32;
                    int y = cell / 32;
                    ArtImage.Cell target = ProjectState.image.cell(ProjectState.tile, x, y);
                    if (!target.emptyPixel() && MeasuredMapColors.packedId(target.color()) >= 0) {
                        ProjectState.setPixelCompletedAutomatically(x, y, AutoProgress.matchesTarget(colors, x, y));
                    }
                    pendingCells.clear(cell);
                    if (++processed < 128 && System.nanoTime() - started < 2000000L) {
                        cell = pendingCells.nextSetBit(cell + 1);
                        continue;
                    }
                    break;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    private static boolean matchesTarget(byte[] colors, int x, int y) {
        ArtImage.Cell target = ProjectState.image.cell(ProjectState.tile, x, y);
        if (target.emptyPixel() || target.color() == null) {
            return false;
        }
        int sx = x * 4;
        int sy = y * 4;
        int packed = colors[sy * 128 + sx] & 0xFF;
        for (int dy = 0; dy < 4; ++dy) {
            for (int dx = 0; dx < 4; ++dx) {
                if ((colors[(sy + dy) * 128 + sx + dx] & 0xFF) == packed) continue;
                return false;
            }
        }
        return packed == MeasuredMapColors.packedId(target.color());
    }

    static {
        previousMapId = Integer.MIN_VALUE;
        pendingCells = new BitSet(1024);
        previousTile = -1;
    }
}

