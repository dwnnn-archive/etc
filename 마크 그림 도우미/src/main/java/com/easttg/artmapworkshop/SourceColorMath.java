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
public final class SourceColorMath {
    private SourceColorMath() {
    }

    private static double linear(double c) {
        return c <= 0.04045 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
    }

    private static double f(double t) {
        double d = 0.20689655172413793;
        return t > d * d * d ? Math.cbrt(t) : t / (3.0 * d * d) + 0.13793103448275862;
    }

    public static double[] lab(int r8, int g8, int b8) {
        double r = SourceColorMath.linear((double)r8 / 255.0);
        double g = SourceColorMath.linear((double)g8 / 255.0);
        double b = SourceColorMath.linear((double)b8 / 255.0);
        double x = (r * 0.4124564 + g * 0.3575761 + b * 0.1804375) / 0.95047;
        double y = r * 0.2126729 + g * 0.7151522 + b * 0.072175;
        double z = (r * 0.0193339 + g * 0.119192 + b * 0.9503041) / 1.08883;
        double fx = SourceColorMath.f(x);
        double fy = SourceColorMath.f(y);
        double fz = SourceColorMath.f(z);
        return new double[]{116.0 * fy - 16.0, 500.0 * (fx - fy), 200.0 * (fy - fz)};
    }

    public static double deltaE2000(double[] lab1, double[] lab2) {
        double diff;
        double diff2;
        double L1 = lab1[0];
        double a1 = lab1[1];
        double b1 = lab1[2];
        double L2 = lab2[0];
        double a2 = lab2[1];
        double b2 = lab2[2];
        double c1 = Math.hypot(a1, b1);
        double c2 = Math.hypot(a2, b2);
        double cBar = (c1 + c2) / 2.0;
        double cBar7 = Math.pow(cBar, 7.0);
        double g = 0.5 * (1.0 - Math.sqrt(cBar7 / (cBar7 + Math.pow(25.0, 7.0))));
        double ap1 = (1.0 + g) * a1;
        double ap2 = (1.0 + g) * a2;
        double cp1 = Math.hypot(ap1, b1);
        double cp2 = Math.hypot(ap2, b2);
        double hp1 = SourceColorMath.hueDegrees(ap1, b1);
        double hp2 = SourceColorMath.hueDegrees(ap2, b2);
        double dLp = L2 - L1;
        double dCp = cp2 - cp1;
        double dhp = cp1 * cp2 == 0.0 ? 0.0 : (Math.abs(diff2 = hp2 - hp1) <= 180.0 ? diff2 : (diff2 > 180.0 ? diff2 - 360.0 : diff2 + 360.0));
        double dHp = 2.0 * Math.sqrt(cp1 * cp2) * Math.sin(Math.toRadians(dhp / 2.0));
        double LpBar = (L1 + L2) / 2.0;
        double CpBar = (cp1 + cp2) / 2.0;
        double hpBar = cp1 * cp2 == 0.0 ? hp1 + hp2 : ((diff = Math.abs(hp1 - hp2)) <= 180.0 ? (hp1 + hp2) / 2.0 : (hp1 + hp2 < 360.0 ? (hp1 + hp2 + 360.0) / 2.0 : (hp1 + hp2 - 360.0) / 2.0));
        double t = 1.0 - 0.17 * Math.cos(Math.toRadians(hpBar - 30.0)) + 0.24 * Math.cos(Math.toRadians(2.0 * hpBar)) + 0.32 * Math.cos(Math.toRadians(3.0 * hpBar + 6.0)) - 0.2 * Math.cos(Math.toRadians(4.0 * hpBar - 63.0));
        double dTheta = 30.0 * Math.exp(-Math.pow((hpBar - 275.0) / 25.0, 2.0));
        double Rc = 2.0 * Math.sqrt(Math.pow(CpBar, 7.0) / (Math.pow(CpBar, 7.0) + Math.pow(25.0, 7.0)));
        double Sl = 1.0 + 0.015 * Math.pow(LpBar - 50.0, 2.0) / Math.sqrt(20.0 + Math.pow(LpBar - 50.0, 2.0));
        double Sc = 1.0 + 0.045 * CpBar;
        double Sh = 1.0 + 0.015 * CpBar * t;
        double Rt = -Math.sin(Math.toRadians(2.0 * dTheta)) * Rc;
        double l = dLp / Sl;
        double c = dCp / Sc;
        double h = dHp / Sh;
        return Math.sqrt(l * l + c * c + h * h + Rt * c * h);
    }

    private static double hueDegrees(double a, double b) {
        if (a == 0.0 && b == 0.0) {
            return 0.0;
        }
        double h = Math.toDegrees(Math.atan2(b, a));
        return h < 0.0 ? h + 360.0 : h;
    }
}

