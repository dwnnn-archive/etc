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
import com.easttg.artmapworkshop.SharpQuantizer;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class ArtImage {
    public static final int CANVAS_SIZE = 32;
    public static final int CELLS_PER_CANVAS = 1024;
    private final int width;
    private final int height;
    private final int tilesX;
    private final int tilesY;
    private final Cell[][] cells;

    private ArtImage(int width, int height, Cell[][] cells) {
        this.width = width;
        this.height = height;
        this.tilesX = (width + 32 - 1) / 32;
        this.tilesY = (height + 32 - 1) / 32;
        this.cells = cells;
    }

    public static ArtImage convert(BufferedImage source, List<PaletteColor> fullPalette, boolean grayscaleOnly, ColorMatchMode matchMode) {
        return ArtImage.convert(source, fullPalette, grayscaleOnly, matchMode, false);
    }

    public static ArtImage convert(BufferedImage source, List<PaletteColor> fullPalette, boolean grayscaleOnly, ColorMatchMode matchMode, boolean sharpClusters) {
        List<PaletteColor> palette = grayscaleOnly ? ArtImage.grayscalePalette(fullPalette) : fullPalette;
        int paddedW = (source.getWidth() + 32 - 1) / 32 * 32;
        int paddedH = (source.getHeight() + 32 - 1) / 32 * 32;
        Cell[][] cells = new Cell[paddedH][paddedW];
        PaletteColor[][] clustered = sharpClusters ? SharpQuantizer.quantizeSharpClusters(source, palette, matchMode) : null;
        for (int y = 0; y < paddedH; ++y) {
            for (int x = 0; x < paddedW; ++x) {
                if (x >= source.getWidth() || y >= source.getHeight()) {
                    cells[y][x] = Cell.empty();
                    continue;
                }
                int argb = source.getRGB(x, y);
                int alpha = argb >>> 24 & 0xFF;
                if (alpha == 0) {
                    cells[y][x] = Cell.empty();
                    continue;
                }
                int r = argb >>> 16 & 0xFF;
                int g = argb >>> 8 & 0xFF;
                int b = argb & 0xFF;
                PaletteColor best = clustered == null ? ColorMatcher.nearest(r, g, b, palette, matchMode) : clustered[y][x];
                cells[y][x] = new Cell(best, alpha);
            }
        }
        return new ArtImage(paddedW, paddedH, cells);
    }

    private static List<PaletteColor> grayscalePalette(List<PaletteColor> full) {
        Set<String> measuredCodes = Set.of("001", "008", "009", "026", "050");
        Set<String> doseCodes = Set.of("D001", "D003", "D007", "D008", "D026", "D028", "D030", "D032", "D044");
        ArrayList<PaletteColor> result = new ArrayList<PaletteColor>();
        for (PaletteColor p : full) {
            if (!measuredCodes.contains(p.code()) && !doseCodes.contains(p.code()) && !p.code().equals("N010") && !p.code().equals("N011")) continue;
            result.add(p);
        }
        return result.isEmpty() ? full : result;
    }

    public int tileCount() {
        return this.tilesX * this.tilesY;
    }

    public int tilesX() {
        return this.tilesX;
    }

    public int tilesY() {
        return this.tilesY;
    }

    public Cell cell(int tileIndex, int x, int y) {
        int tx = tileIndex % this.tilesX;
        int ty = tileIndex / this.tilesX;
        int sx = tx * 32 + x;
        int sy = ty * 32 + y;
        if (sx < 0 || sy < 0 || sx >= this.width || sy >= this.height) {
            return Cell.empty();
        }
        return this.cells[sy][sx];
    }

    public List<String> colorKeysInTile(int tileIndex) {
        LinkedHashSet<String> keys = new LinkedHashSet<String>();
        for (int y = 0; y < 32; ++y) {
            for (int x = 0; x < 32; ++x) {
                Cell c = this.cell(tileIndex, x, y);
                if (c.emptyPixel()) continue;
                keys.add(c.color().key());
            }
        }
        return new ArrayList<String>(keys);
    }

    public List<String> paintCodesInTile(int tileIndex) {
        LinkedHashSet<String> codes = new LinkedHashSet<String>();
        for (int y = 0; y < 32; ++y) {
            for (int x = 0; x < 32; ++x) {
                Cell c = this.cell(tileIndex, x, y);
                if (c.emptyPixel()) continue;
                codes.add(c.color().code());
            }
        }
        return new ArrayList<String>(codes);
    }

    public Map<String, Integer> countsInTile(int tileIndex) {
        LinkedHashMap<String, Integer> counts = new LinkedHashMap<String, Integer>();
        for (int y = 0; y < 32; ++y) {
            for (int x = 0; x < 32; ++x) {
                Cell c = this.cell(tileIndex, x, y);
                if (c.emptyPixel()) continue;
                counts.merge(c.color().key(), 1, Integer::sum);
            }
        }
        return counts;
    }

    @Environment(value=EnvType.CLIENT)
    public record Cell(PaletteColor color, int alpha) {
        public static Cell empty() {
            return new Cell(null, 0);
        }

        public boolean emptyPixel() {
            return this.color == null || this.alpha == 0;
        }
    }
}

