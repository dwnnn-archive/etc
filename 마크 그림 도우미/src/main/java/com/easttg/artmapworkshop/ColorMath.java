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
public final class ColorMath {
    private ColorMath() {
    }

    private static double linear(double c) {
        return c <= 0.04045 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
    }

    private static double f(double t) {
        double d = 0.20689655172413793;
        return t > d * d * d ? Math.cbrt(t) : t / (3.0 * d * d) + 0.13793103448275862;
    }

    public static double[] lab(int r8, int g8, int b8) {
        double r = ColorMath.linear((double)r8 / 255.0);
        double g = ColorMath.linear((double)g8 / 255.0);
        double b = ColorMath.linear((double)b8 / 255.0);
        double x = (r * 0.4124564 + g * 0.3575761 + b * 0.1804375) / 0.95047;
        double y = r * 0.2126729 + g * 0.7151522 + b * 0.072175;
        double z = (r * 0.0193339 + g * 0.119192 + b * 0.9503041) / 1.08883;
        double fx = ColorMath.f(x);
        double fy = ColorMath.f(y);
        double fz = ColorMath.f(z);
        return new double[]{116.0 * fy - 16.0, 500.0 * (fx - fy), 200.0 * (fy - fz)};
    }

    public static double distance(double[] a, double[] b) {
        double dl = a[0] - b[0];
        double da = a[1] - b[1];
        double db = a[2] - b[2];
        return Math.sqrt(dl * dl + da * da + db * db);
    }
}

