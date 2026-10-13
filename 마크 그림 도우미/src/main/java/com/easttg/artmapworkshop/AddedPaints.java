/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_3620
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.PaletteColor;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3620;

@Environment(value=EnvType.CLIENT)
public final class AddedPaints {
    private static final List<Paint> PAINTS = List.of(new Paint("N001", "\ud6c4\ub834\uacfc\uc0c9", 185), new Paint("N002", "\uc640\uc778\uc0c9", 229), new Paint("N003", "\ubbfc\ud2b8\uc0c9", 233), new Paint("N004", "\uc2ac\ub77c\uc784\uc0c9", 133), new Paint("N005", "\uc5d0\uba54\ub784\ub4dc\uc0c9", 165), new Paint("N006", "\uc22f\uc0c9", 205), new Paint("N007", "\uc560\ud50c\uc0c9", 201), new Paint("N008", "\ubcbd\ub3cc\uc0c9", 113), new Paint("N009", "\uc801\ube68\uac15\uc0c9", 217), new Paint("N010", "\uc554\uc0c9", 237), new Paint("N011", "\uac00\uc2a4\ud2b8\uc0c9", 45));
    private static final String[] ACTIONS = new String[]{"\uae30\ubcf8", "\uae43\ud138 1\ud68c", "\uc11d\ud0c4 1\ud68c", "\uc11d\ud0c4 2\ud68c"};
    private static final int[] SHADES = new int[]{1, 2, 0, 3};
    private static final int[] BRIGHTNESS = new int[]{180, 220, 255, 135};

    private AddedPaints() {
    }

    public static int packedId(PaletteColor color) {
        if (color == null) {
            return -1;
        }
        for (Paint paint : PAINTS) {
            if (!paint.code().equals(color.code())) continue;
            for (int stage = 0; stage < ACTIONS.length; ++stage) {
                if (!ACTIONS[stage].equals(color.action())) continue;
                return paint.basePackedId() & 0xFFFFFFFC | SHADES[stage];
            }
        }
        return -1;
    }

    public static List<PaletteColor> colors() {
        ArrayList<PaletteColor> result = new ArrayList<PaletteColor>(PAINTS.size() * ACTIONS.length);
        for (Paint paint : PAINTS) {
            int rgb = class_3620.method_38479((int)(paint.basePackedId() >>> 2)).field_16011;
            for (int stage = 0; stage < ACTIONS.length; ++stage) {
                int factor = BRIGHTNESS[SHADES[stage]];
                result.add(new PaletteColor(paint.code(), paint.name(), ACTIONS[stage], (rgb >>> 16 & 0xFF) * factor / 255, (rgb >>> 8 & 0xFF) * factor / 255, (rgb & 0xFF) * factor / 255));
            }
        }
        return List.copyOf(result);
    }

    @Environment(value=EnvType.CLIENT)
    private record Paint(String code, String name, int basePackedId) {
    }
}

