/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.easttg.artmapworkshop;

import com.easttg.artmapworkshop.ColorMatchMode;
import com.easttg.artmapworkshop.ColorMatcher;
import com.easttg.artmapworkshop.DitherMode;
import com.easttg.artmapworkshop.PaletteColor;
import java.awt.image.BufferedImage;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class DitherProcessor {
    private static final int[][] B4 = new int[][]{{0, 8, 2, 10}, {12, 4, 14, 6}, {3, 11, 1, 9}, {15, 7, 13, 5}};
    private static final int[][] B8 = new int[][]{{0, 32, 8, 40, 2, 34, 10, 42}, {48, 16, 56, 24, 50, 18, 58, 26}, {12, 44, 4, 36, 14, 46, 6, 38}, {60, 28, 52, 20, 62, 30, 54, 22}, {3, 35, 11, 43, 1, 33, 9, 41}, {51, 19, 59, 27, 49, 17, 57, 25}, {15, 47, 7, 39, 13, 45, 5, 37}, {63, 31, 55, 23, 61, 29, 53, 21}};

    private DitherProcessor() {
    }

    public static BufferedImage apply(BufferedImage src, List<PaletteColor> palette, DitherMode mode, ColorMatchMode matchMode) {
        return switch (mode) {
            default -> throw new MatchException(null, null);
            case DitherMode.OFF -> src;
            case DitherMode.FLOYD_STEINBERG -> DitherProcessor.fs(src, palette, matchMode);
            case DitherMode.BAYER_4X4 -> DitherProcessor.ordered(src, B4, 16);
            case DitherMode.BAYER_8X8 -> DitherProcessor.ordered(src, B8, 64);
        };
    }

    private static BufferedImage fs(BufferedImage src, List<PaletteColor> palette, ColorMatchMode matchMode) {
        int w = src.getWidth();
        int h = src.getHeight();
        double[][] r = new double[h][w];
        double[][] g = new double[h][w];
        double[][] b = new double[h][w];
        int[][] a = new int[h][w];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                int c = src.getRGB(x, y);
                a[y][x] = c >>> 24 & 0xFF;
                r[y][x] = c >>> 16 & 0xFF;
                g[y][x] = c >>> 8 & 0xFF;
                b[y][x] = c & 0xFF;
            }
        }
        BufferedImage out = new BufferedImage(w, h, 2);
        for (int y = 0; y < h; ++y) {
            boolean rev = (y & 1) == 1;
            for (int i = 0; i < w; ++i) {
                int x;
                int n = x = rev ? w - 1 - i : i;
                if (a[y][x] == 0) {
                    out.setRGB(x, y, 0);
                    continue;
                }
                int rr = DitherProcessor.clamp((int)Math.round(r[y][x]));
                int gg = DitherProcessor.clamp((int)Math.round(g[y][x]));
                int bb = DitherProcessor.clamp((int)Math.round(b[y][x]));
                PaletteColor p = ColorMatcher.nearest(rr, gg, bb, palette, matchMode);
                out.setRGB(x, y, a[y][x] << 24 | p.r() << 16 | p.g() << 8 | p.b());
                double er = rr - p.r();
                double eg = gg - p.g();
                double eb = bb - p.b();
                if (!rev) {
                    DitherProcessor.spread(r, g, b, x + 1, y, er, eg, eb, 0.4375, w, h);
                    DitherProcessor.spread(r, g, b, x - 1, y + 1, er, eg, eb, 0.1875, w, h);
                    DitherProcessor.spread(r, g, b, x, y + 1, er, eg, eb, 0.3125, w, h);
                    DitherProcessor.spread(r, g, b, x + 1, y + 1, er, eg, eb, 0.0625, w, h);
                    continue;
                }
                DitherProcessor.spread(r, g, b, x - 1, y, er, eg, eb, 0.4375, w, h);
                DitherProcessor.spread(r, g, b, x + 1, y + 1, er, eg, eb, 0.1875, w, h);
                DitherProcessor.spread(r, g, b, x, y + 1, er, eg, eb, 0.3125, w, h);
                DitherProcessor.spread(r, g, b, x - 1, y + 1, er, eg, eb, 0.0625, w, h);
            }
        }
        return out;
    }

    private static BufferedImage ordered(BufferedImage src, int[][] m, int div) {
        int w = src.getWidth();
        int h = src.getHeight();
        int n = m.length;
        BufferedImage out = new BufferedImage(w, h, 2);
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                int c = src.getRGB(x, y);
                int a = c >>> 24 & 0xFF;
                if (a == 0) {
                    out.setRGB(x, y, 0);
                    continue;
                }
                int rr = c >>> 16 & 0xFF;
                int gg = c >>> 8 & 0xFF;
                int bb = c & 0xFF;
                double t = (((double)m[y % n][x % n] + 0.5) / (double)div - 0.5) * 40.0;
                rr = DitherProcessor.clamp((int)Math.round((double)rr + t));
                gg = DitherProcessor.clamp((int)Math.round((double)gg + t));
                bb = DitherProcessor.clamp((int)Math.round((double)bb + t));
                out.setRGB(x, y, a << 24 | rr << 16 | gg << 8 | bb);
            }
        }
        return out;
    }

    private static void spread(double[][] r, double[][] g, double[][] b, int x, int y, double er, double eg, double eb, double f, int w, int h) {
        if (x < 0 || y < 0 || x >= w || y >= h) {
            return;
        }
        double[] dArray = r[y];
        int n = x;
        dArray[n] = dArray[n] + er * f;
        double[] dArray2 = g[y];
        int n2 = x;
        dArray2[n2] = dArray2[n2] + eg * f;
        double[] dArray3 = b[y];
        int n3 = x;
        dArray3[n3] = dArray3[n3] + eb * f;
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
}

