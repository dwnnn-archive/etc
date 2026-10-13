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
import com.easttg.artmapworkshop.PaletteColor;
import com.easttg.artmapworkshop.SourceColorMath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class SharpQuantizer {
    private static final double SHARP_CLUSTER_DELTA_E = 8.0;
    private static final double SHARP_MINOR_MERGE_DELTA_E = 20.0;
    private static final int SHARP_MAX_CLUSTERS = 256;

    private SharpQuantizer() {
    }

    /*
     * WARNING - void declaration
     */
    public static PaletteColor[][] quantizeSharpClusters(BufferedImage image, List<PaletteColor> palette, ColorMatchMode colorMatchMode) {
        void var15_29;
        double d;
        int w = image.getWidth();
        int h = image.getHeight();
        PaletteColor[][] out = new PaletteColor[h][w];
        HashMap<Integer, Integer> histogram = new HashMap<Integer, Integer>();
        int opaquePixels = 0;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                int argb = image.getRGB(x, y);
                if ((argb >>> 24 & 0xFF) == 0) continue;
                int rgb = argb & 0xFFFFFF;
                histogram.merge(rgb, 1, Integer::sum);
                ++opaquePixels;
            }
        }
        if (opaquePixels == 0) {
            return out;
        }
        ArrayList ranked = new ArrayList(histogram.entrySet());
        ranked.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        ArrayList<SourceCluster> clusters = new ArrayList<SourceCluster>();
        HashMap<Integer, SourceCluster> sourceToCluster = new HashMap<Integer, SourceCluster>();
        for (Map.Entry entry : ranked) {
            int rgb = (Integer)entry.getKey();
            int count = (Integer)entry.getValue();
            double[] dArray = SharpQuantizer.lab(rgb);
            SourceCluster nearest = null;
            double nearestDistance = Double.MAX_VALUE;
            for (SourceCluster cluster : clusters) {
                d = SourceColorMath.deltaE2000(dArray, cluster.anchorLab);
                if (!(d < nearestDistance)) continue;
                nearestDistance = d;
                nearest = cluster;
            }
            if (nearest != null && (nearestDistance <= 8.0 || clusters.size() >= 256)) {
                nearest.count += count;
                sourceToCluster.put(rgb, nearest);
                continue;
            }
            SourceCluster created = new SourceCluster(rgb, dArray, count);
            clusters.add(created);
            sourceToCluster.put(rgb, created);
        }
        int majorMinimum = Math.max(4, (int)Math.ceil((double)opaquePixels * 0.0025));
        ArrayList<SourceCluster> arrayList = new ArrayList<SourceCluster>();
        for (SourceCluster c : clusters) {
            if (c.count < majorMinimum) continue;
            arrayList.add(c);
        }
        if (arrayList.isEmpty() && !clusters.isEmpty()) {
            arrayList.add((SourceCluster)clusters.get(0));
        }
        HashMap<SourceCluster, SourceCluster> resolved = new HashMap<SourceCluster, SourceCluster>();
        for (SourceCluster sourceCluster : clusters) {
            if (sourceCluster.count >= majorMinimum) {
                resolved.put(sourceCluster, sourceCluster);
                continue;
            }
            SourceCluster nearestMajor = null;
            double best = Double.MAX_VALUE;
            for (SourceCluster major : arrayList) {
                d = SourceColorMath.deltaE2000(sourceCluster.anchorLab, major.anchorLab);
                if (!(d < best)) continue;
                best = d;
                nearestMajor = major;
            }
            resolved.put(sourceCluster, nearestMajor != null && best <= 20.0 ? nearestMajor : sourceCluster);
        }
        HashMap<SourceCluster, PaletteColor> clusterPalette = new HashMap<SourceCluster, PaletteColor>();
        for (SourceCluster c : clusters) {
            SourceCluster finalCluster = resolved.getOrDefault(c, c);
            clusterPalette.computeIfAbsent(finalCluster, key -> ColorMatcher.nearest(key.anchorRgb >>> 16 & 0xFF, key.anchorRgb >>> 8 & 0xFF, key.anchorRgb & 0xFF, palette, colorMatchMode));
        }
        boolean bl = false;
        while (var15_29 < h) {
            for (int x = 0; x < w; ++x) {
                int argb = image.getRGB(x, (int)var15_29);
                if ((argb >>> 24 & 0xFF) == 0) continue;
                int rgb = argb & 0xFFFFFF;
                SourceCluster original = (SourceCluster)sourceToCluster.get(rgb);
                SourceCluster finalCluster = resolved.getOrDefault(original, original);
                out[var15_29][x] = (PaletteColor)clusterPalette.get(finalCluster);
            }
            ++var15_29;
        }
        SharpQuantizer.collapseTwoColorMinorities(out, image, opaquePixels);
        return out;
    }

    private static void collapseTwoColorMinorities(PaletteColor[][] out, BufferedImage source, int opaquePixels) {
        if (opaquePixels <= 0) {
            return;
        }
        HashMap<PaletteColor, Integer> counts = new HashMap<PaletteColor, Integer>();
        PaletteColor[][] paletteColorArray = out;
        int n = paletteColorArray.length;
        for (int i = 0; i < n; ++i) {
            PaletteColor[] row;
            for (PaletteColor c : row = paletteColorArray[i]) {
                if (c == null) continue;
                counts.merge(c, 1, Integer::sum);
            }
        }
        if (counts.size() <= 2) {
            return;
        }
        ArrayList ranked = new ArrayList(counts.entrySet());
        ranked.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        PaletteColor first = (PaletteColor)((Map.Entry)ranked.get(0)).getKey();
        PaletteColor second = (PaletteColor)((Map.Entry)ranked.get(1)).getKey();
        int dominantCount = (Integer)((Map.Entry)ranked.get(0)).getValue() + (Integer)((Map.Entry)ranked.get(1)).getValue();
        if ((double)dominantCount < Math.ceil((double)opaquePixels * 0.95)) {
            return;
        }
        double[] firstLab = SourceColorMath.lab(first.r(), first.g(), first.b());
        double[] secondLab = SourceColorMath.lab(second.r(), second.g(), second.b());
        for (int y = 0; y < out.length; ++y) {
            for (int x = 0; x < out[y].length; ++x) {
                PaletteColor current = out[y][x];
                if (current == null || current == first || current == second) continue;
                int argb = source.getRGB(x, y);
                double[] srcLab = SourceColorMath.lab(argb >>> 16 & 0xFF, argb >>> 8 & 0xFF, argb & 0xFF);
                double d1 = SourceColorMath.deltaE2000(srcLab, firstLab);
                double d2 = SourceColorMath.deltaE2000(srcLab, secondLab);
                out[y][x] = d1 <= d2 ? first : second;
            }
        }
    }

    private static double[] lab(int rgb) {
        return SourceColorMath.lab(rgb >>> 16 & 0xFF, rgb >>> 8 & 0xFF, rgb & 0xFF);
    }

    @Environment(value=EnvType.CLIENT)
    private static final class SourceCluster {
        final int anchorRgb;
        final double[] anchorLab;
        int count;

        SourceCluster(int anchorRgb, double[] anchorLab, int count) {
            this.anchorRgb = anchorRgb;
            this.anchorLab = anchorLab;
            this.count = count;
        }
    }
}

