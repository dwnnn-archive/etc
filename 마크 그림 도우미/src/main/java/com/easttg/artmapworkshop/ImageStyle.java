/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.easttg.artmapworkshop;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public enum ImageStyle {
    LOGO("\ub85c\uace0/\ub3c4\ud2b8"),
    ILLUSTRATION("\uc77c\ub7ec\uc2a4\ud2b8/\uc0ac\uc9c4");

    private final String label;

    private ImageStyle(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public ImageStyle next() {
        return this == LOGO ? ILLUSTRATION : LOGO;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public BufferedImage resize(BufferedImage source, int width, int height) {
        BufferedImage output = new BufferedImage(width, height, 2);
        Graphics2D g = output.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this == LOGO ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR : RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, this == LOGO ? RenderingHints.VALUE_RENDER_SPEED : RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(source, 0, 0, width, height, 0, 0, source.getWidth(), source.getHeight(), null);
        }
        finally {
            g.dispose();
        }
        return output;
    }
}

