/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.ColorMatchMode;
import com.easttg.artmapworkshop.ColorMath;
import com.easttg.artmapworkshop.PaletteColor;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class ColorMatcher {
    private ColorMatcher() {
    }

    public static PaletteColor nearest(int r, int g, int b, List<PaletteColor> palette, ColorMatchMode mode) {
        double[] raw;
        double[] src = raw = ColorMath.lab(r, g, b);
        if (mode == ColorMatchMode.SOFT_TONE) {
            src = new double[]{Math.min(100.0, raw[0] + 3.5), raw[1] * 0.86, raw[2] * 0.86};
        } else if (mode == ColorMatchMode.ADAPTIVE_TONE_V2) {
            src = ColorMatcher.adaptiveSource(raw);
        }
        PaletteColor best = palette.get(0);
        double bestD = Double.MAX_VALUE;
        for (PaletteColor p : palette) {
            double d;
            double[] dst = ColorMath.lab(p.r(), p.g(), p.b());
            if (mode == ColorMatchMode.SOFT_TONE) {
                double dl = src[0] - dst[0];
                double da = src[1] - dst[1];
                double db = src[2] - dst[2];
                d = Math.sqrt(1.18 * dl * dl + da * da + db * db);
            } else {
                d = mode == ColorMatchMode.ADAPTIVE_TONE_V2 ? ColorMatcher.adaptiveDistance(src, dst) : ColorMath.distance(src, dst);
            }
            if (!(d < bestD)) continue;
            bestD = d;
            best = p;
        }
        return best;
    }

    private static double[] adaptiveSource(double[] raw) {
        double chromaScale;
        double lift;
        double l = raw[0];
        double a = raw[1];
        double b = raw[2];
        double chroma = Math.hypot(a, b);
        double hue = Math.toDegrees(Math.atan2(b, a));
        if (hue < 0.0) {
            hue += 360.0;
        }
        if (chroma < 16.0) {
            lift = l >= 78.0 ? 4.2 : 3.2;
            chromaScale = 0.62;
        } else if (hue >= 20.0 && hue <= 85.0 && l >= 35.0) {
            lift = 2.2;
            chromaScale = 0.97;
        } else if (hue >= 85.0 && hue <= 175.0) {
            lift = 1.4;
            chromaScale = 0.94;
        } else {
            lift = 2.4;
            chromaScale = 0.9;
        }
        return new double[]{Math.min(100.0, l + lift), a * chromaScale, b * chromaScale};
    }

    private static double adaptiveDistance(double[] src, double[] dst) {
        double dl = src[0] - dst[0];
        double da = src[1] - dst[1];
        double db = src[2] - dst[2];
        double srcChroma = Math.hypot(src[1], src[2]);
        double dstChroma = Math.hypot(dst[1], dst[2]);
        double d2 = 1.24 * dl * dl + da * da + db * db;
        if (srcChroma < 14.0 && dstChroma > srcChroma) {
            double excess = dstChroma - srcChroma;
            d2 += 0.55 * excess * excess;
        }
        return Math.sqrt(d2);
    }
}

